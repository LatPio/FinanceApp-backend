package pl.finansepal.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import pl.finansepal.controller.dto.IncomeDTO;
import pl.finansepal.controller.mapper.IncomeMapper;
import pl.finansepal.controller.mapper.UserMapper;
import pl.finansepal.model.Income;
import pl.finansepal.model.SearchCriteria;
import pl.finansepal.repository.IncomeRepository;
import pl.finansepal.security.auth.AuthService;
import pl.finansepal.service.specification.IncomeSpecification;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.data.jpa.domain.Specification.where;
import static pl.finansepal.service.specification.IncomeSpecification.*;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class IncomeService  { //implements CrudService<IncomeDTO, Long>

    private final IncomeRepository incomeRepository;
    private final TagService tagService;

    private final IncomeMapper incomeMapper = Mappers.getMapper(IncomeMapper.class);
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);
    private final AuthService authService;




    public List<IncomeDTO> list(List<SearchCriteria> searchCriteriaList) {
        if (searchCriteriaList == null) {
            return incomeRepository.findAll(belongToUser(authService.getCurrentUser()))//
                    .stream()
                    .map(incomeMapper::map)
                    .collect(Collectors.toList());
        } else {
            IncomeSpecification spec = new IncomeSpecification();
            searchCriteriaList.stream().forEach(searchCriteria -> spec.add(searchCriteria));
            return incomeRepository.findAll(where(spec).and(belongToUser(authService.getCurrentUser())))
                    .stream()
                    .map(incomeMapper::map)
                    .collect(Collectors.toList());
        }

    }


    public IncomeDTO get(Long aLong) {
        return incomeRepository.findByIdAndUser(aLong, authService.getCurrentUser())
                .map(income -> incomeMapper.map(income))
                .orElse(null);
    }


    public IncomeDTO create(IncomeDTO incomeDTO) {
        incomeDTO.setId(null);
        incomeDTO.setUser(userMapper.map(authService.getCurrentUser()));
        Income created = incomeRepository.save(incomeMapper.map(incomeDTO));
        return incomeMapper.map(created);
    }


    public IncomeDTO update(IncomeDTO incomeDTO) {
        IncomeDTO existing = get(incomeDTO.getId());
        if(existing == null){
            return null;
        }
        incomeDTO.setUser(userMapper.map(authService.getCurrentUser()));
        Income updated = incomeRepository.save(incomeMapper.map(incomeDTO));

        return incomeMapper.map(updated);
    }
    public void delete(Long aLong) {
        incomeRepository.deleteByIdAndUser(aLong, authService.getCurrentUser());
    }

    public BigDecimal getAmountByDate(LocalDateTime startDate, LocalDateTime endDate) {
        return incomeRepository.sumOfIncomesByDate(startDate, endDate, belongToUser(authService.getCurrentUser()));
    }

    public Map<String, BigDecimal> getMonthlyAmountsFromLast(Integer numbersOfLastMonth){

        LocalDateTime startMonth = LocalDateTime.now().withDayOfMonth(1);
        LocalDateTime endMonth = LocalDateTime.now().withDayOfMonth(LocalDate.now().getMonth().length(LocalDate.now().isLeapYear()));
        Map<String, BigDecimal> output = new TreeMap<>();
        List<String> labels = new ArrayList<>();
        List<BigDecimal> monthlyIncomes = new ArrayList<>();

        Stream.iterate(numbersOfLastMonth ,n-> n-1).limit(numbersOfLastMonth+1).forEach( integer-> {
            String key = LocalDateTime.now().minusMonths(integer).format(DateTimeFormatter.ofPattern("yyyy - MM"));
            BigDecimal value;
            if( incomeRepository.sumOfIncomesByDate(startMonth.minusMonths(integer), endMonth.minusMonths(integer), belongToUser(authService.getCurrentUser())) == null){
                value = (BigDecimal.valueOf(0));
            } else {
                value = (
                        incomeRepository.sumOfIncomesByDate(
                                startMonth.minusMonths(integer),
                                endMonth.minusMonths(integer),
                                belongToUser(authService.getCurrentUser())
                        ).setScale(2, RoundingMode.HALF_UP));
            }

                    output.put(key,value);
                });


        return output;
    }

    public Map<String, BigDecimal> getAmountsForTagsFromLast(LocalDateTime dateStart, LocalDateTime dateEnd){


        Map<String, BigDecimal> output = new TreeMap<>();
        List<String> labels = new ArrayList<>();
        List<Long> labelsIds = new ArrayList<>();
//        List<BigDecimal> monthlyIncomes = new ArrayList<>();
        tagService.list().stream().forEach(s -> {
            labels.add(s.getName());
            labelsIds.add((s.getId()));
        });
        labelsIds.forEach(aLong -> {
            BigDecimal value;
            String key = tagService.get(aLong).getName();
            if(incomeRepository.sumByTags_IdAndDateBetween(aLong, dateStart, dateEnd, belongToUser(authService.getCurrentUser())) == null) {
                value = BigDecimal.valueOf(0);
            } else  {
                value = incomeRepository.sumByTags_IdAndDateBetween(aLong, dateStart, dateEnd, belongToUser(authService.getCurrentUser())).setScale(2, RoundingMode.HALF_UP);
            }
            output.put(key, value);
        });


        return output;
    }

    public LocalDateTime getFirstEntryByDate(){
        return incomeRepository.findFirstDate(belongToUser(authService.getCurrentUser()));
    }
    public List<Integer> getYears(){
        Integer minYear = incomeRepository.findFirstDate(belongToUser(authService.getCurrentUser())).getYear();
        Integer maxYear = LocalDateTime.now().getYear();
        List<Integer> listOfUsedYears = new ArrayList<>();
        for (int i = minYear; i < maxYear+1; i++) {
            listOfUsedYears.add(i);
        }
        return listOfUsedYears;
    }

    public Map<String, ArrayList<BigDecimal>> yearDetailedInfo(Integer year){

        Map<String, ArrayList<BigDecimal>> output = new TreeMap<>();
        List<String> labels = new ArrayList<>();
        List<Long> labelsIds = new ArrayList<>();
        ArrayList<BigDecimal> monthlyAmounts = new ArrayList<>();
        tagService.list().stream().forEach(s -> {
            labels.add(s.getName());
            labelsIds.add((s.getId()));
        });
        labelsIds.forEach(aLong -> {
            ArrayList<BigDecimal> value= new ArrayList<>();
            String key = tagService.get(aLong).getName();
            Stream.iterate(1, n-> n+1).limit(12).forEach(month ->{
                LocalDateTime initial = LocalDateTime.of(year,month,1,0,0);
                LocalDateTime dateStart = initial.withDayOfMonth(1);
                LocalDateTime dateEnd = initial.withDayOfMonth(initial.getMonth().length(LocalDate.of(year,month,1).isLeapYear()));
                if(incomeRepository.sumByTags_IdAndDateBetween(aLong, dateStart, dateEnd, belongToUser(authService.getCurrentUser())) == null) {
                    value.add(BigDecimal.valueOf(0))  ;
                } else  {
                    value.add(incomeRepository.sumByTags_IdAndDateBetween(aLong, dateStart, dateEnd, belongToUser(authService.getCurrentUser())).setScale(2, RoundingMode.HALF_UP));
                }
            });

            output.put(key, value);
        });


        return output;
    }

}

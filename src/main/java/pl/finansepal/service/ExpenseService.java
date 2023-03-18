package pl.finansepal.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import pl.finansepal.controller.dto.ExpenseDTO;
import pl.finansepal.controller.mapper.ExpenseMapper;
import pl.finansepal.controller.mapper.UserMapper;
import pl.finansepal.model.Expense;
import pl.finansepal.model.SearchCriteria;
import pl.finansepal.repository.ExpenseRepository;
import pl.finansepal.security.auth.AuthService;
import pl.finansepal.service.common.CrudService;
import pl.finansepal.service.specification.ExpenseSpecification;
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
import static pl.finansepal.service.specification.ExpenseSpecification.belongToUser;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class ExpenseService  { //implements CrudService<ExpenseDTO, Long>

    private final ExpenseRepository expenseRepository;
    private final TagService tagService;

    private final ExpenseMapper expenseMapper = Mappers.getMapper(ExpenseMapper.class);
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);
    private final AuthService authService;



    public List<ExpenseDTO> list(List<SearchCriteria> searchCriteriaList) {
        if (searchCriteriaList == null) {
            return expenseRepository.findAll(belongToUser(authService.getCurrentUser()))   //authService.getCurrentUser()
                    .stream()
                    .map(expenseMapper::map)
                    .collect(Collectors.toList());
        } else {
            ExpenseSpecification spec = new ExpenseSpecification();

            searchCriteriaList.stream().forEach(searchCriteria -> spec.add(searchCriteria)); // to check and implement

            return expenseRepository.findAll(where(spec).and(belongToUser(authService.getCurrentUser())))   //authService.getCurrentUser()
                    .stream()
                    .map(expenseMapper::map)
                    .collect(Collectors.toList());
        }
    }


    public ExpenseDTO get(Long id) {
        return expenseRepository.findByIdAndUser(id, authService.getCurrentUser())
                .map(expense -> expenseMapper.map(expense))
                .orElse(null);
    }

    public ExpenseDTO create(ExpenseDTO expenseDTO) {
        expenseDTO.setId(null);
        expenseDTO.setUser(userMapper.map(authService.getCurrentUser()));
        Expense created = expenseRepository.save(expenseMapper.map(expenseDTO));
        return expenseMapper.map(created);
    }

    public ExpenseDTO update(ExpenseDTO expenseDTO) {
        ExpenseDTO existing = get(expenseDTO.getId());
        if(existing == null){
            return null;
        }
        expenseDTO.setUser(userMapper.map(authService.getCurrentUser()));
        Expense updated = expenseRepository.save(expenseMapper.map(expenseDTO));
        return expenseMapper.map(updated);

    }
    public void delete(Long id) {

        expenseRepository.deleteByIdAndUser(id, authService.getCurrentUser());
    }

    public BigDecimal getAmountByDate(LocalDateTime startDate, LocalDateTime endDate) {
        return expenseRepository.sumOfExpenseByDate(startDate, endDate, ExpenseSpecification.belongToUser(authService.getCurrentUser()) );
    }

    public Map<String, BigDecimal> getMonthlyAmountsFromLast(Integer numbersOfLastMonth){

        LocalDateTime startMonth = LocalDateTime.now().withDayOfMonth(1);
        LocalDateTime endMonth = LocalDateTime.now().withDayOfMonth(LocalDate.now().getMonth().length(LocalDate.now().isLeapYear()));
        Map<String, BigDecimal> output = new TreeMap<>();


        Stream.iterate(numbersOfLastMonth , n-> n-1).limit(numbersOfLastMonth+1).forEach(integer-> {
            String key = LocalDateTime.now().minusMonths(integer).format(DateTimeFormatter.ofPattern("yyyy - MM"));
            BigDecimal value;
            if( expenseRepository.sumOfExpenseByDate(startMonth.minusMonths(integer), endMonth.minusMonths(integer), ExpenseSpecification.belongToUser(authService.getCurrentUser())) == null){
                value = (BigDecimal.valueOf(0));
            } else {
                value = (
                        expenseRepository.sumOfExpenseByDate(
                                startMonth.minusMonths(integer),
                                endMonth.minusMonths(integer),
                                ExpenseSpecification.belongToUser(authService.getCurrentUser())
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
        tagService.list().stream().forEach(s -> {
            labels.add(s.getName());
            labelsIds.add((s.getId()));
        });
        labelsIds.forEach(aLong -> {
            BigDecimal value;
            String key = tagService.get(aLong).getName();
            if(expenseRepository.sumByTags_IdAndDateBetween(aLong, dateStart, dateEnd, belongToUser(authService.getCurrentUser())) == null) {
                value = BigDecimal.valueOf(0);
            } else  {
                value = expenseRepository.sumByTags_IdAndDateBetween(aLong, dateStart, dateEnd, belongToUser(authService.getCurrentUser())).setScale(2, RoundingMode.HALF_UP);
            }
            output.put(key, value);
        });


        return output;
    }

    public Map<String, ArrayList<BigDecimal>> yearDetailedInfo(Integer year) {

        Map<String, ArrayList<BigDecimal>> output = new TreeMap<>();
        List<String> labels = new ArrayList<>();
        List<Long> labelsIds = new ArrayList<>();
        ArrayList<BigDecimal> monthlyAmounts = new ArrayList<>();
        tagService.list().stream().forEach(s -> {
            labels.add(s.getName());
            labelsIds.add((s.getId()));
        });
        labelsIds.forEach(aLong -> {
            ArrayList<BigDecimal> value = new ArrayList<>();
            String key = tagService.get(aLong).getName();
            Stream.iterate(1, n -> n + 1).limit(12).forEach(month -> {
                LocalDateTime initial = LocalDateTime.of(year, month, 1, 0, 0);
                LocalDateTime dateStart = initial.withDayOfMonth(1);
                LocalDateTime dateEnd = initial.withDayOfMonth(initial.getMonth().length(LocalDate.of(year, month, 1).isLeapYear()));
                if (expenseRepository.sumByTags_IdAndDateBetween(aLong, dateStart, dateEnd, belongToUser(authService.getCurrentUser())) == null) {
                    value.add(BigDecimal.valueOf(0));
                } else {
                    value.add(expenseRepository.sumByTags_IdAndDateBetween(aLong, dateStart, dateEnd, belongToUser(authService.getCurrentUser())).setScale(2, RoundingMode.HALF_UP));
                }
            });
            output.put(key, value);
        });


        return output;
    }

}

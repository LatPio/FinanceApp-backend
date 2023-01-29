package pl.finansepal.service;

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
import pl.finansepal.service.common.CrudService;
import pl.finansepal.service.specification.ExpenseSpecification;
import pl.finansepal.service.specification.IncomeSpecification;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.jpa.domain.Specification.where;
import static pl.finansepal.service.specification.IncomeSpecification.*;

@Service
@AllArgsConstructor
@Slf4j
public class IncomeService  { //implements CrudService<IncomeDTO, Long>

    private final IncomeRepository incomeRepository;

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
            return incomeRepository.findAll(where(spec).and(belongToUser(authService.getCurrentUser())))//
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
}

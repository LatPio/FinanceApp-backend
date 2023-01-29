package pl.finansepal.service;

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

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.jpa.domain.Specification.where;
import static pl.finansepal.service.specification.ExpenseSpecification.belongToUser;

@Service
@AllArgsConstructor
@Slf4j
public class ExpenseService  { //implements CrudService<ExpenseDTO, Long>

    private final ExpenseRepository expenseRepository;
    private final ExpenseMapper expenseMapper = Mappers.getMapper(ExpenseMapper.class);
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);
    private final AuthService authService;


//    public List<ExpenseDTO> list() {
//        return expenseRepository.findAll(belongToUser(authService.getCurrentUser()))   //authService.getCurrentUser()
//                .stream()
//                .map(expenseMapper::map)
//                .collect(Collectors.toList());
//    }
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

//    public List<ExpenseDTO> list() {
//        return expenseRepository.findAll(belongToUser(authService.getCurrentUser()))   //authService.getCurrentUser()
//                .stream()
//                .map(expenseMapper::map)
//                .collect(Collectors.toList());
//    }
//    public List<ExpenseDTO> list(List<SearchCriteria> searchCriteriaList) {
//        ExpenseSpecification spec = new ExpenseSpecification();
//
//        searchCriteriaList.stream().forEach(searchCriteria -> spec.add(searchCriteria)); // to check and implement
//
//        return expenseRepository.findAll(where(spec).and(belongToUser(authService.getCurrentUser())))   //authService.getCurrentUser()
//                .stream()
//                .map(expenseMapper::map)
//                .collect(Collectors.toList());
//    }
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
}

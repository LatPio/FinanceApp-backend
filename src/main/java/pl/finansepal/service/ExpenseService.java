package pl.finansepal.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.finansepal.controller.dto.ExpenseDTO;
import pl.finansepal.model.Expense;
import pl.finansepal.repository.ExpenseRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public ExpenseDTO save (ExpenseDTO expenseDTO){
        Expense expense = expenseRepository.save(mapExpenseDto(expenseDTO));
        expense.setId(expense.getId());
        return expenseDTO;
    }

    private Expense mapExpenseDto(ExpenseDTO expenseDTO) {
        return Expense.builder()
                .name(expenseDTO.getName())
                .amount(expenseDTO.getAmount())
                .currency(expenseDTO.getCurrency())
                .name(expenseDTO.getName())
                .tags(expenseDTO.getTags())
                .owner(expenseDTO.getOwner())
                .build();
    }

    public List<ExpenseDTO> getAll() {
        return expenseRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private ExpenseDTO mapToDto(Expense expense) {
        return ExpenseDTO.builder()
                .id(expense.getId())
                .amount(expense.getAmount())
                .currency(expense.getCurrency())
                .name(expense.getName())
                .owner(expense.getOwner())
                .tags(expense.getTags())
                .build();
    }
}

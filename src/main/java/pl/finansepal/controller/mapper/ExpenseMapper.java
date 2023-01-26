package pl.finansepal.controller.mapper;

import org.mapstruct.Mapper;
import pl.finansepal.controller.dto.ExpenseDTO;
import pl.finansepal.model.Expense;

import java.util.List;

@Mapper
public interface ExpenseMapper {


    ExpenseDTO map(Expense expense);

    Expense map(ExpenseDTO expenseDTO);

    List<ExpenseDTO> mapToDto (List<Expense> expenseList);


}

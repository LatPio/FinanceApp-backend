package pl.finansepal.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.finansepal.controller.dto.ExpenseDTO;
import pl.finansepal.service.ExpenseService;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@AllArgsConstructor
@Slf4j
public class ExpanseController {

    private final ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<ExpenseDTO> createExpense(@RequestBody ExpenseDTO expenseDTO){

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(expenseService.create(expenseDTO));
    }

    @GetMapping
    public ResponseEntity<List<ExpenseDTO>> getAllExpenses (){
        return ResponseEntity.status(HttpStatus.OK).body(expenseService.list());
    }

}

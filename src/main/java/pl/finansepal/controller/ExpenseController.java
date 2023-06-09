package pl.finansepal.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.finansepal.controller.dto.ExpenseDTO;
import pl.finansepal.model.SearchCriteria;
import pl.finansepal.service.ExpenseService;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@AllArgsConstructor
@Slf4j
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<ExpenseDTO> create(@RequestBody ExpenseDTO expenseDTO){

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(expenseService.create(expenseDTO));
    }

    @GetMapping(path = "/list")
    public ResponseEntity<List<ExpenseDTO>> getAll (@RequestParam(required = false) List<SearchCriteria> searchCriteria){
        return ResponseEntity.status(HttpStatus.OK).body(expenseService.list(searchCriteria));
    }
    @PostMapping(path = "/list")
    public ResponseEntity<List<ExpenseDTO>> getAllWithSpec(@RequestBody List<SearchCriteria> searchCriteria){
        return ResponseEntity.status(HttpStatus.OK).body(expenseService.list(searchCriteria));
    }



    @GetMapping
    public ResponseEntity<ExpenseDTO> get(@RequestParam Long id){
        return ResponseEntity.status(HttpStatus.OK).body(expenseService.get(id));
    }

    @PutMapping
    public ResponseEntity<ExpenseDTO> update(@RequestBody ExpenseDTO expenseDTO){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(expenseService.update(expenseDTO));

    }

    @DeleteMapping
    public ResponseEntity delete(@RequestParam Long id){
        expenseService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }

}

package pl.finansepal.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.finansepal.controller.dto.IncomeDTO;
import pl.finansepal.model.SearchCriteria;
import pl.finansepal.service.IncomeService;

import java.util.List;

@RestController
@RequestMapping("/api/income")
@AllArgsConstructor
@Slf4j
public class IncomeController {

    private final IncomeService incomeService;

    @PostMapping
    public ResponseEntity<IncomeDTO> create(@RequestBody IncomeDTO incomeDTO){

        return ResponseEntity.status(HttpStatus.CREATED).body(incomeService.create(incomeDTO));
    }

    @GetMapping("/list")
    public ResponseEntity<List<IncomeDTO>> getAll (@RequestBody(required = false) List<SearchCriteria> searchCriteria){
        return ResponseEntity.status(HttpStatus.OK).body(incomeService.list(searchCriteria));
    }

    @GetMapping
    public ResponseEntity<IncomeDTO> get(@RequestParam Long id){
        return ResponseEntity.status(HttpStatus.OK).body(incomeService.get(id));
    }

    @PutMapping
    public ResponseEntity<IncomeDTO> update(@RequestBody IncomeDTO incomeDTO){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(incomeService.update(incomeDTO));

    }

    @DeleteMapping
    public ResponseEntity delete(@RequestParam Long id){
        incomeService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }





}

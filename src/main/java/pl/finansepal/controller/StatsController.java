package pl.finansepal.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.finansepal.repository.IncomeRepository;
import pl.finansepal.service.ExpenseService;
import pl.finansepal.service.IncomeService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/stats")
@AllArgsConstructor
@Slf4j
public class StatsController {
    private final IncomeRepository incomeRepository;
    private final IncomeService incomeService;
    private final ExpenseService expenseService;

    @GetMapping("/income")
    public ResponseEntity<BigDecimal> getSumAmount (
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime ednDate){
        return ResponseEntity.status(HttpStatus.OK).body(incomeService.getAmountByDate(startDate,ednDate));
    }

    @GetMapping("/monthly_income")
    public ResponseEntity<Map<String,BigDecimal>> getSumAmountByMonth (
            @RequestParam("numbersOfMonths") Integer numbersOfMonths){
        return ResponseEntity.status(HttpStatus.OK).body(incomeService.getMonthlyAmountsFromLast(numbersOfMonths));
    }

}

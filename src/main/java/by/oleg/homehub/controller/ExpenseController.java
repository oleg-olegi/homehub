package by.oleg.homehub.controller;

import by.oleg.homehub.entity.dto.expense.ExpenseRequestDTO;
import by.oleg.homehub.entity.dto.expense.ExpenseResponseDTO;
import by.oleg.homehub.entity.enums.Category;
import by.oleg.homehub.service.ExpenseService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/expenses")
@AllArgsConstructor
@Slf4j
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping("/create")
    public ResponseEntity<?> createExpense(
            @RequestBody @NonNull ExpenseRequestDTO expense) {
        log.info("REST request to create Expense: {}", expense);
        ExpenseResponseDTO responseDTO = expenseService.createExpense(expense);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    public ResponseEntity<?> getExpenses(
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to,
            @RequestParam(required = false) Category category,
            @PageableDefault(size = 20, sort = "date", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {
        log.info("REST request to get all Expenses");
        return ResponseEntity.ok(expenseService.getExpenses(from, to, category, pageable));
    }

    @GetMapping("/sum")
    public ResponseEntity<?> getExpensesSum(
            @RequestParam short month
    ){
        log.info("REST request to get sum of all Expenses in this month");

        return ResponseEntity.ok(expenseService.getExpensesSum(month)) ;
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteExpense(@PathVariable Long id) {
        log.info("REST request to Delete Expense by ID {}", id);
        expenseService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }
}

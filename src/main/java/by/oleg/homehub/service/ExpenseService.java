package by.oleg.homehub.service;

import by.oleg.homehub.entity.dto.expense.ExpenseRequestDTO;
import by.oleg.homehub.entity.dto.expense.ExpenseResponseDTO;
import by.oleg.homehub.entity.enums.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;


public interface ExpenseService {

    ExpenseResponseDTO createExpense(ExpenseRequestDTO expenseRequestDTO);

    Page<?> getExpenses(LocalDate from,
                        LocalDate to,
                        Category category,
                        Pageable pageable);

    void deleteExpense(Long id);

    BigDecimal getExpensesSum(short month);
}

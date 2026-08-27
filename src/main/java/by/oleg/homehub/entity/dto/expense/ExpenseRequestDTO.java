package by.oleg.homehub.entity.dto.expense;

import by.oleg.homehub.entity.enums.Category;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseRequestDTO(
        BigDecimal amount,
        Category category,
        LocalDate date,
        String description
) {
}

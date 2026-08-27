package by.oleg.homehub.entity.dto.expense;

import by.oleg.homehub.entity.enums.Category;

import java.math.BigDecimal;

public record ExpenseResponseDTO(
        Long id,
        BigDecimal amount,
        Category category,
        String date
) {
}

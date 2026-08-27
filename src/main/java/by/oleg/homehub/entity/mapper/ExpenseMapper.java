package by.oleg.homehub.entity.mapper;

import by.oleg.homehub.entity.Expense;
import by.oleg.homehub.entity.dto.expense.ExpenseResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ExpenseMapper {

    @Mapping(target = "date", source = "date")
    ExpenseResponseDTO expenseToExpenseResponseDTO(Expense expense);
}

package by.oleg.homehub.entity.mapper;

import by.oleg.homehub.entity.Income;
import by.oleg.homehub.entity.dto.income.IncomeResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IncomeMapper {
    IncomeResponseDTO toResponseDto(Income income);
}

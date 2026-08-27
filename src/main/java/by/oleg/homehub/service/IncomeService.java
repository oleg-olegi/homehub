package by.oleg.homehub.service;

import by.oleg.homehub.entity.dto.income.IncomeRequestDTO;
import by.oleg.homehub.entity.dto.income.IncomeResponseDTO;

public interface IncomeService {
   IncomeResponseDTO createIncome(IncomeRequestDTO incomeRequestDTO);

}

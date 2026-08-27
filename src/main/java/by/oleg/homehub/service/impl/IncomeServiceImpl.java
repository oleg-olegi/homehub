package by.oleg.homehub.service.impl;

import by.oleg.homehub.entity.Income;
import by.oleg.homehub.entity.User;
import by.oleg.homehub.entity.dto.income.IncomeRequestDTO;
import by.oleg.homehub.entity.dto.income.IncomeResponseDTO;
import by.oleg.homehub.entity.mapper.ExpenseMapper;
import by.oleg.homehub.entity.mapper.IncomeMapper;
import by.oleg.homehub.repository.ExpenseRepository;
import by.oleg.homehub.repository.IncomeRepository;
import by.oleg.homehub.repository.UserRepository;
import by.oleg.homehub.service.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class IncomeServiceImpl implements IncomeService {
    private final IncomeRepository incomeRepository;
    private final UserRepository userRepository;
    private final IncomeMapper incomeMapper;

    public IncomeResponseDTO createIncome(IncomeRequestDTO incomeRequestDTO) {
        Income income = new Income();
        income.setAmount(incomeRequestDTO.amount());
        income.setDate(LocalDate.now());
        User user = getUser();
        income.setUser(user);

        return incomeMapper.toResponseDto(incomeRepository.save(income));
    }

    private User getUser() {
        String email = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}

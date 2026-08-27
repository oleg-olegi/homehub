package by.oleg.homehub.service.impl;


import by.oleg.homehub.entity.Expense;
import by.oleg.homehub.entity.User;
import by.oleg.homehub.entity.dto.expense.ExpenseRequestDTO;
import by.oleg.homehub.entity.dto.expense.ExpenseResponseDTO;
import by.oleg.homehub.entity.enums.Category;
import by.oleg.homehub.entity.mapper.ExpenseMapper;
import by.oleg.homehub.repository.ExpenseRepository;
import by.oleg.homehub.repository.UserRepository;
import by.oleg.homehub.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final ExpenseMapper expenseMapper;

    @Override
    public ExpenseResponseDTO createExpense(ExpenseRequestDTO requestDTO) {
        Expense expense = new Expense();
        String email = getUsersEmailFromContext();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        expense.setUser(user);
        expense.setAmount(requestDTO.amount());
        expense.setCategory(requestDTO.category());
        expense.setDate(requestDTO.date());
        Expense savedExpense = expenseRepository.save(expense);
        log.info("Expense - {}", savedExpense);
        return expenseMapper.expenseToExpenseResponseDTO(savedExpense);
    }

    @Override
    public Page<?> getExpenses(LocalDate from, LocalDate to, Category category, Pageable pageable) {
        Page<Expense> expenses = expenseRepository.findExpenses(from, to, category, pageable);
        log.info("Found expenses - {}", expenses.getTotalElements());
        return expenses.map(expenseMapper::expenseToExpenseResponseDTO);
    }

    @Override
    public void deleteExpense(Long id) {
        try {
            expenseRepository.deleteById(id);
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("Expense not found");
        }
    }

    @Override
    public BigDecimal getExpensesSum(short month) {
        log.info("getExpensesSum");
        int year = LocalDate.now().getYear();
        return expenseRepository.getExpensesSum(month,  year);
    }

    private static String getUsersEmailFromContext() {
        return Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
    }
}

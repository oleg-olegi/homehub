package by.oleg.homehub.repository;

import by.oleg.homehub.entity.Expense;
import by.oleg.homehub.entity.enums.Category;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    @Query("""
            SELECT e
            FROM Expense e
            WHERE (CAST(:from AS date) IS NULL OR e.date >= :from)
                            AND (CAST(:to AS date) IS NULL OR e.date <= :to)
                            AND (:category IS NULL OR e.category = :category)
            
            """)
    Page<@NonNull Expense> findExpenses(
            @Param("from") LocalDate from,
            @Param("to") LocalDate to,
            @Param("category") Category category,
            Pageable pageable);

    @Query("""
            SELECT SUM (e.amount)
            FROM Expense e 
            WHERE EXTRACT(MONTH FROM e.date) = :month
            AND EXTRACT(YEAR FROM e.date) = :year 
            """
    )
    BigDecimal getExpensesSum(@Param("month") short month,
                              @Param("year") int year);
}
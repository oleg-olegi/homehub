package by.oleg.homehub.controller;

import by.oleg.homehub.entity.dto.income.IncomeRequestDTO;
import by.oleg.homehub.service.IncomeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/income")
@AllArgsConstructor
@Slf4j
public class IncomeController {

    private final IncomeService incomeService;

    @PostMapping("/create")
    public ResponseEntity<?> createIncome(@RequestBody IncomeRequestDTO incomeRequestDTO) {
        log.info("Income request received: {}", incomeRequestDTO);
        return ResponseEntity.ok(incomeService.createIncome(incomeRequestDTO));
    }
}

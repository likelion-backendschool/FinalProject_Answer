package com.ll.exam.final__2022_10_08.app.cash.repository;

import com.ll.exam.final__2022_10_08.app.cash.entity.CashLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CashLogRepository extends JpaRepository<CashLog, Long> {
}

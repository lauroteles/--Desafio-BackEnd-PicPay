package com.picpaysimplificado.repositories;

import com.picpaysimplificado.domain.user.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {
}

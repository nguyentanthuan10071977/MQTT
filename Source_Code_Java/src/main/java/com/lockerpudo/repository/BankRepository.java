package com.lockerpudo.repository;
import com.lockerpudo.domain.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
public interface BankRepository extends JpaRepository<Bank, Long> { }
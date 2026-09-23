package com.lockerpudo.repository;
import com.lockerpudo.domain.SmartLocker;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface SmartLockerRepository extends JpaRepository<SmartLocker, Long> { Optional<SmartLocker> findByCode(String code); }
package com.lockerpudo.repository;
import com.lockerpudo.domain.LockerSlot;
import com.lockerpudo.domain.SlotStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface LockerSlotRepository extends JpaRepository<LockerSlot, Long> { Optional<LockerSlot> findFirstByLockerIdAndStatus(Long lockerId, SlotStatus status); }
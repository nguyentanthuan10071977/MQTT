package com.lockerpudo.repository;
import com.lockerpudo.domain.ParcelOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface ParcelOrderRepository extends JpaRepository<ParcelOrder, Long> {
    Optional<ParcelOrder> findByTrackingCode(String trackingCode);
    List<ParcelOrder> findByRecipientIdOrderByIdDesc(Long recipientId);
}
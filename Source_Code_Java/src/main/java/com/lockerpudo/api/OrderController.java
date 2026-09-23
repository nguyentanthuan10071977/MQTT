package com.lockerpudo.api;

import com.lockerpudo.domain.*;
import com.lockerpudo.repository.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api/orders")
public class OrderController {
    private final ParcelOrderRepository orders; private final UserAccountRepository users; private final SmartLockerRepository lockers; private final LockerSlotRepository slots;
    public OrderController(ParcelOrderRepository orders, UserAccountRepository users, SmartLockerRepository lockers, LockerSlotRepository slots) { this.orders = orders; this.users = users; this.lockers = lockers; this.slots = slots; }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public ParcelOrder create(@Valid @RequestBody CreateOrderRequest r) {
        UserAccount recipient = users.findById(r.recipientId()).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Recipient not found"));
        UserAccount staff = users.findById(r.deliveryStaffId()).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Delivery staff not found"));
        if (recipient.getRole() != UserRole.RECIPIENT || staff.getRole() != UserRole.DELIVERY_STAFF || staff.getStatus() != AccountStatus.ACTIVE) throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid order participants");
        return orders.save(new ParcelOrder(r.trackingCode(), r.itemName(), recipient.getPhone(), recipient, staff));
    }
    @GetMapping("/recipient/{recipientId}") public List<ParcelOrder> byRecipient(@PathVariable Long recipientId) { return orders.findByRecipientIdOrderByIdDesc(recipientId); }
    @GetMapping("/{id}") public ParcelOrder get(@PathVariable Long id) { return orders.findById(id).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Order not found")); }
    @PostMapping("/{id}/deposit") @Transactional public ParcelOrder deposit(@PathVariable Long id, @RequestParam Long lockerId) {
        ParcelOrder order = get(id); if (order.getStatus() != OrderStatus.CREATED) throw new ApiException(HttpStatus.BAD_REQUEST, "Order cannot be deposited in its current state");
        SmartLocker locker = lockers.findById(lockerId).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Locker not found"));
        if (!locker.isOnline()) throw new ApiException(HttpStatus.BAD_REQUEST, "Locker is offline");
        LockerSlot slot = slots.findFirstByLockerIdAndStatus(lockerId, SlotStatus.AVAILABLE).orElseThrow(() -> new ApiException(HttpStatus.CONFLICT, "No available slot"));
        slot.setStatus(SlotStatus.OCCUPIED); order.deposit(locker, slot); return orders.save(order);
    }
    @PostMapping("/{id}/pay") public ParcelOrder pay(@PathVariable Long id) { ParcelOrder order = get(id); order.setPaymentStatus(PaymentStatus.PAID); return orders.save(order); }
    @PostMapping("/{id}/receive") @Transactional public ParcelOrder receive(@PathVariable Long id) { ParcelOrder order = get(id); if (order.getStatus() != OrderStatus.IN_LOCKER || order.getPaymentStatus() != PaymentStatus.PAID) throw new ApiException(HttpStatus.BAD_REQUEST, "Order must be in locker and paid"); order.getSlot().setStatus(SlotStatus.AVAILABLE); order.deliver(); return orders.save(order); }
    @PostMapping("/{id}/request-return") public ParcelOrder requestReturn(@PathVariable Long id) { ParcelOrder order = get(id); order.requestReturn(); return orders.save(order); }
    @PostMapping("/{id}/return") @Transactional public ParcelOrder returnOrder(@PathVariable Long id) { ParcelOrder order = get(id); order.getSlot().setStatus(SlotStatus.AVAILABLE); order.returnToStaff(); return orders.save(order); }
    @PostMapping("/{id}/cancel") public ParcelOrder cancel(@PathVariable Long id) { ParcelOrder order = get(id); order.cancel(); return orders.save(order); }
    public record CreateOrderRequest(@NotBlank String trackingCode, @NotBlank String itemName, Long recipientId, Long deliveryStaffId) { }
}
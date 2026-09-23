package com.lockerpudo.domain;

import jakarta.persistence.*;
import java.time.Instant;

@Entity @Table(name = "parcel_orders", uniqueConstraints = @UniqueConstraint(columnNames = "trackingCode"))
public class ParcelOrder {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, unique = true) private String trackingCode;
    @Column(nullable = false) private String itemName;
    @Column(nullable = false) private String recipientPhone;
    @ManyToOne(optional = false) private UserAccount recipient;
    @ManyToOne(optional = false) private UserAccount deliveryStaff;
    @ManyToOne private SmartLocker locker;
    @ManyToOne private LockerSlot slot;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private OrderStatus status = OrderStatus.CREATED;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private PaymentStatus paymentStatus = PaymentStatus.UNPAID;
    @Column(nullable = false, updatable = false) private Instant createdAt;
    private Instant depositedAt; private Instant completedAt;
    protected ParcelOrder() { }
    public ParcelOrder(String trackingCode, String itemName, String recipientPhone, UserAccount recipient, UserAccount deliveryStaff) {
        this.trackingCode = trackingCode; this.itemName = itemName; this.recipientPhone = recipientPhone;
        this.recipient = recipient; this.deliveryStaff = deliveryStaff;
    }
    @PrePersist void onCreate() { createdAt = Instant.now(); }
    public Long getId() { return id; } public String getTrackingCode() { return trackingCode; }
    public String getItemName() { return itemName; } public String getRecipientPhone() { return recipientPhone; }
    public UserAccount getRecipient() { return recipient; } public UserAccount getDeliveryStaff() { return deliveryStaff; }
    public SmartLocker getLocker() { return locker; } public LockerSlot getSlot() { return slot; }
    public OrderStatus getStatus() { return status; } public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(PaymentStatus v) { paymentStatus = v; } public void deposit(SmartLocker l, LockerSlot s) { locker = l; slot = s; status = OrderStatus.IN_LOCKER; depositedAt = Instant.now(); }
    public void deliver() { status = OrderStatus.DELIVERED; completedAt = Instant.now(); }
    public void requestReturn() { if (status != OrderStatus.DELIVERED) throw new IllegalStateException("Only delivered orders can be returned"); status = OrderStatus.RETURN_REQUESTED; }
    public void returnToStaff() { if (status != OrderStatus.RETURN_REQUESTED) throw new IllegalStateException("Order is not waiting for return"); status = OrderStatus.RETURNED; completedAt = Instant.now(); }
    public void cancel() { if (status != OrderStatus.CREATED) throw new IllegalStateException("Only new orders can be cancelled"); status = OrderStatus.CANCELLED; completedAt = Instant.now(); }
}
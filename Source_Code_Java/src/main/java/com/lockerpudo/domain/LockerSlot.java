package com.lockerpudo.domain;

import jakarta.persistence.*;

@Entity @Table(name = "locker_slots", uniqueConstraints = @UniqueConstraint(columnNames = {"locker_id", "slotNumber"}))
public class LockerSlot {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional = false) private SmartLocker locker;
    @Column(nullable = false) private Integer slotNumber;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private SlotStatus status = SlotStatus.AVAILABLE;
    protected LockerSlot() { }
    public LockerSlot(SmartLocker locker, Integer slotNumber) { this.locker = locker; this.slotNumber = slotNumber; }
    public Long getId() { return id; } public SmartLocker getLocker() { return locker; }
    public Integer getSlotNumber() { return slotNumber; } public SlotStatus getStatus() { return status; }
    public void setStatus(SlotStatus v) { status = v; }
}
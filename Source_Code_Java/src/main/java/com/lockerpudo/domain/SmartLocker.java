package com.lockerpudo.domain;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity @Table(name = "smart_lockers")
public class SmartLocker {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, unique = true) private String code;
    @Column(nullable = false) private String name;
    @Column(nullable = false) private String address;
    private Double latitude; private Double longitude;
    @Column(nullable = false) private boolean online = true;
    @OneToMany(mappedBy = "locker", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LockerSlot> slots = new ArrayList<>();
    protected SmartLocker() { }
    public SmartLocker(String code, String name, String address, Double latitude, Double longitude) {
        this.code = code; this.name = name; this.address = address; this.latitude = latitude; this.longitude = longitude;
    }
    public Long getId() { return id; } public String getCode() { return code; } public String getName() { return name; }
    public void setName(String v) { name = v; } public String getAddress() { return address; } public void setAddress(String v) { address = v; }
    public Double getLatitude() { return latitude; } public Double getLongitude() { return longitude; }
    public boolean isOnline() { return online; } public void setOnline(boolean v) { online = v; }
    public List<LockerSlot> getSlots() { return slots; }
}
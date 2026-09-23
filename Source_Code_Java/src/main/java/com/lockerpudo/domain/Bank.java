package com.lockerpudo.domain;

import jakarta.persistence.*;

@Entity @Table(name = "banks")
public class Bank {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false) private String shortName;
    @Column(nullable = false) private String fullName;
    @Column(nullable = false) private boolean active = true;
    protected Bank() { }
    public Bank(String shortName, String fullName) { this.shortName = shortName; this.fullName = fullName; }
    public Long getId() { return id; } public String getShortName() { return shortName; }
    public void setShortName(String v) { shortName = v; } public String getFullName() { return fullName; }
    public void setFullName(String v) { fullName = v; } public boolean isActive() { return active; }
    public void setActive(boolean v) { active = v; }
}
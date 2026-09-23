package com.lockerpudo.domain;

import jakarta.persistence.*;

@Entity @Table(name = "carriers")
public class Carrier {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false) private String name;
    private String apiUrl;
    @Column(nullable = false) private boolean active = true;
    protected Carrier() { }
    public Carrier(String name, String apiUrl) { this.name = name; this.apiUrl = apiUrl; }
    public Long getId() { return id; } public String getName() { return name; }
    public void setName(String v) { name = v; } public String getApiUrl() { return apiUrl; }
    public void setApiUrl(String v) { apiUrl = v; } public boolean isActive() { return active; }
    public void setActive(boolean v) { active = v; }
}
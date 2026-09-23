package com.lockerpudo.domain;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "user_accounts", uniqueConstraints = {@UniqueConstraint(columnNames = "email"), @UniqueConstraint(columnNames = "phone")})
public class UserAccount {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false) private String fullName;
    @Column(nullable = false) private String email;
    @Column(nullable = false) private String phone;
    @Column(nullable = false) private String passwordHash;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private UserRole role;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private AccountStatus status;
    private String evidenceImageUrl;
    @ManyToOne private Carrier carrier;
    @ManyToOne private Bank bank;
    private String bankAccountNumber;
    @Column(nullable = false, updatable = false) private Instant createdAt;

    protected UserAccount() { }
    public UserAccount(String fullName, String email, String phone, String passwordHash, UserRole role) {
        this.fullName = fullName; this.email = email; this.phone = phone; this.passwordHash = passwordHash;
        this.role = role; this.status = role == UserRole.DELIVERY_STAFF ? AccountStatus.PENDING : AccountStatus.ACTIVE;
    }
    @PrePersist void onCreate() { createdAt = Instant.now(); }
    public Long getId() { return id; } public String getFullName() { return fullName; }
    public void setFullName(String v) { fullName = v; } public String getEmail() { return email; }
    public void setEmail(String v) { email = v; } public String getPhone() { return phone; }
    public void setPhone(String v) { phone = v; } public String getPasswordHash() { return passwordHash; }
    public UserRole getRole() { return role; } public AccountStatus getStatus() { return status; }
    public void setStatus(AccountStatus v) { status = v; } public Carrier getCarrier() { return carrier; }
    public void setCarrier(Carrier v) { carrier = v; } public Bank getBank() { return bank; }
    public void setBank(Bank v) { bank = v; } public String getBankAccountNumber() { return bankAccountNumber; }
    public void setBankAccountNumber(String v) { bankAccountNumber = v; } public String getEvidenceImageUrl() { return evidenceImageUrl; }
    public void setEvidenceImageUrl(String v) { evidenceImageUrl = v; }
}
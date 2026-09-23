package com.lockerpudo.api;

import com.lockerpudo.domain.*;
import com.lockerpudo.repository.UserAccountRepository;
import com.lockerpudo.service.JwtService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/auth")
public class AuthController {
    private final UserAccountRepository users; private final PasswordEncoder encoder; private final JwtService jwt;
    public AuthController(UserAccountRepository users, PasswordEncoder encoder, JwtService jwt) { this.users = users; this.encoder = encoder; this.jwt = jwt; }
    @PostMapping("/register/recipient") @ResponseStatus(HttpStatus.CREATED)
    public UserView registerRecipient(@Valid @RequestBody RegisterRequest r) { return register(r, UserRole.RECIPIENT); }
    @PostMapping("/register/delivery-staff") @ResponseStatus(HttpStatus.CREATED)
    public UserView registerDelivery(@Valid @RequestBody DeliveryRegisterRequest r) {
        if (users.existsByEmail(r.email()) || users.existsByPhone(r.phone())) throw new ApiException(HttpStatus.CONFLICT, "Email or phone already exists");
        UserAccount u = new UserAccount(r.fullName(), r.email(), r.phone(), encoder.encode(r.password()), UserRole.DELIVERY_STAFF);
        u.setEvidenceImageUrl(r.evidenceImageUrl()); return new UserView(users.save(u));
    }
    private UserView register(RegisterRequest r, UserRole role) {
        if (users.existsByEmail(r.email()) || users.existsByPhone(r.phone())) throw new ApiException(HttpStatus.CONFLICT, "Email or phone already exists");
        return new UserView(users.save(new UserAccount(r.fullName(), r.email(), r.phone(), encoder.encode(r.password()), role)));
    }
    @PostMapping("/login") public LoginResponse login(@Valid @RequestBody LoginRequest r) {
        UserAccount u = users.findByEmailOrPhone(r.login(), r.login()).orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));
        if (!encoder.matches(r.password(), u.getPasswordHash()) || u.getStatus() != AccountStatus.ACTIVE) throw new ApiException(HttpStatus.UNAUTHORIZED, "Invalid credentials or inactive account");
        return new LoginResponse(jwt.createToken(u), new UserView(u));
    }
    public record RegisterRequest(@NotBlank String fullName, @Email @NotBlank String email, @NotBlank String phone, @Size(min = 8) String password) { }
    public record DeliveryRegisterRequest(@NotBlank String fullName, @Email @NotBlank String email, @NotBlank String phone, @Size(min = 8) String password, String evidenceImageUrl) { }
    public record LoginRequest(@NotBlank String login, @NotBlank String password) { }
    public record UserView(Long id, String fullName, String email, String phone, UserRole role, AccountStatus status) { UserView(UserAccount u) { this(u.getId(), u.getFullName(), u.getEmail(), u.getPhone(), u.getRole(), u.getStatus()); } }
    public record LoginResponse(String token, UserView user) { }
}
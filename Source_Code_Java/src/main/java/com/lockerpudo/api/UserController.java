package com.lockerpudo.api;

import com.lockerpudo.domain.UserAccount;
import com.lockerpudo.repository.UserAccountRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/users")
public class UserController {
    private final UserAccountRepository users;
    public UserController(UserAccountRepository users) { this.users = users; }
    @GetMapping("/{id}") public AuthController.UserView get(@PathVariable Long id) { return new AuthController.UserView(find(id)); }
    @PatchMapping("/{id}") public AuthController.UserView update(@PathVariable Long id, @Valid @RequestBody ProfileRequest request) {
        UserAccount user = find(id); user.setFullName(request.fullName()); user.setPhone(request.phone()); return new AuthController.UserView(users.save(user));
    }
    private UserAccount find(Long id) { return users.findById(id).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "User not found")); }
    public record ProfileRequest(@NotBlank String fullName, @NotBlank String phone) { }
}
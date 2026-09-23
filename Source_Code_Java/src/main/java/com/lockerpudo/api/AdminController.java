package com.lockerpudo.api;

import com.lockerpudo.domain.*;
import com.lockerpudo.repository.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api/admin")
public class AdminController {
    private final UserAccountRepository users; private final BankRepository banks; private final CarrierRepository carriers;
    public AdminController(UserAccountRepository users, BankRepository banks, CarrierRepository carriers) { this.users = users; this.banks = banks; this.carriers = carriers; }
    @GetMapping("/delivery-staff/pending") public List<AuthController.UserView> pendingStaff() { return users.findAll().stream().filter(u -> u.getRole() == UserRole.DELIVERY_STAFF && u.getStatus() == AccountStatus.PENDING).map(AuthController.UserView::new).toList(); }
    @PatchMapping("/delivery-staff/{id}/approval") public AuthController.UserView approve(@PathVariable Long id, @RequestBody ApprovalRequest request) {
        UserAccount user = users.findById(id).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Delivery staff not found"));
        if (user.getRole() != UserRole.DELIVERY_STAFF || user.getStatus() != AccountStatus.PENDING) throw new ApiException(HttpStatus.BAD_REQUEST, "Account is not pending");
        user.setStatus(request.approved() ? AccountStatus.ACTIVE : AccountStatus.REJECTED); return new AuthController.UserView(users.save(user));
    }
    @GetMapping("/banks") public List<Bank> banks() { return banks.findAll(); }
    @PostMapping("/banks") @ResponseStatus(HttpStatus.CREATED) public Bank createBank(@Valid @RequestBody BankRequest r) { return banks.save(new Bank(r.shortName(), r.fullName())); }
    @PutMapping("/banks/{id}") public Bank updateBank(@PathVariable Long id, @Valid @RequestBody BankRequest r) { Bank b = banks.findById(id).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Bank not found")); b.setShortName(r.shortName()); b.setFullName(r.fullName()); return banks.save(b); }
    @DeleteMapping("/banks/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void deleteBank(@PathVariable Long id) { banks.deleteById(id); }
    @GetMapping("/carriers") public List<Carrier> carriers() { return carriers.findAll(); }
    @PostMapping("/carriers") @ResponseStatus(HttpStatus.CREATED) public Carrier createCarrier(@Valid @RequestBody CarrierRequest r) { return carriers.save(new Carrier(r.name(), r.apiUrl())); }
    @PutMapping("/carriers/{id}") public Carrier updateCarrier(@PathVariable Long id, @Valid @RequestBody CarrierRequest r) { Carrier c = carriers.findById(id).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Carrier not found")); c.setName(r.name()); c.setApiUrl(r.apiUrl()); return carriers.save(c); }
    @DeleteMapping("/carriers/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void deleteCarrier(@PathVariable Long id) { carriers.deleteById(id); }
    public record ApprovalRequest(boolean approved) { }
    public record BankRequest(@NotBlank String shortName, @NotBlank String fullName) { }
    public record CarrierRequest(@NotBlank String name, String apiUrl) { }
}
package com.lockerpudo.api;

import com.lockerpudo.domain.*;
import com.lockerpudo.repository.SmartLockerRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api/lockers")
public class LockerController {
    private final SmartLockerRepository lockers;
    public LockerController(SmartLockerRepository lockers) { this.lockers = lockers; }
    @GetMapping public List<SmartLocker> list() { return lockers.findAll(); }
    @GetMapping("/{id}") public SmartLocker get(@PathVariable Long id) { return lockers.findById(id).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Locker not found")); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public SmartLocker create(@Valid @RequestBody LockerRequest r) {
        SmartLocker locker = new SmartLocker(r.code(), r.name(), r.address(), r.latitude(), r.longitude());
        for (int i = 1; i <= r.slotCount(); i++) locker.getSlots().add(new LockerSlot(locker, i));
        return lockers.save(locker);
    }
    @PatchMapping("/{id}/connection") public SmartLocker connection(@PathVariable Long id, @RequestParam boolean online) { SmartLocker l = get(id); l.setOnline(online); return lockers.save(l); }
    public record LockerRequest(@NotBlank String code, @NotBlank String name, @NotBlank String address, Double latitude, Double longitude, @Min(1) int slotCount) { }
}
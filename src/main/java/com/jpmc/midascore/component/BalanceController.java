package com.jpmc.midascore.component;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Balance;
import com.jpmc.midascore.repository.UserRepository;

@RestController
public class BalanceController {
    private final UserRepository userRepository;

    public BalanceController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/balance")
    public Balance getBalance(@RequestParam Long userId) {
        // FIX: Add .orElse(null) to handle the Optional type
        UserRecord user = userRepository.findById(userId).orElse(null);
        
        if (user == null) {
            // Requirement: return 0 if user does not exist
            return new Balance(0f);
        }
        return new Balance(user.getBalance());
    }
}
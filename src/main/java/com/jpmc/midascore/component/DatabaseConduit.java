package com.jpmc.midascore.component;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.UserRepository;

@Component
public class DatabaseConduit {
    private final UserRepository userRepository;
    private final String incentiveApiUrl = "http://localhost:8082/incentive";
    private final RestTemplate restTemplate = new RestTemplate();

    public DatabaseConduit(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // FIX: Ensure this method exists so UserPopulator can call it
    public void save(UserRecord userRecord) {
        userRepository.save(userRecord);
    }

   public void save(Transaction transaction) {
    // FIX: Removed .orElse(null) because findById returns UserRecord directly
    UserRecord sender = userRepository.findById(transaction.getSenderId());
    UserRecord recipient = userRepository.findById(transaction.getRecipientId());

    if (sender != null && recipient != null && sender.getBalance() >= transaction.getAmount()) {
        float bonus = 0.0f; 
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer midas-core-token");
            HttpEntity<Transaction> request = new HttpEntity<>(transaction, headers);

            Incentive incentive = restTemplate.postForObject(incentiveApiUrl, request, Incentive.class);
            if (incentive != null) {
                bonus = incentive.getAmount();
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        sender.setBalance(sender.getBalance() - transaction.getAmount());
        recipient.setBalance(recipient.getBalance() + transaction.getAmount() + bonus);

        userRepository.save(sender);
        userRepository.save(recipient);
    }
}
}
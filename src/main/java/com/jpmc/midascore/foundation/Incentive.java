package com.jpmc.midascore.foundation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Incentive {
    private float amount;

    public Incentive() {} // Required for Jackson

    public float getAmount() { return amount; }
    public void setAmount(float amount) { this.amount = amount; }
}
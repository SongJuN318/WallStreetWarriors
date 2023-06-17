package com.example.registration_login_demo.service;

public class ThresholdRequest {
    private double profitThreshold;
    private double lossThreshold;

    public double getProfitThreshold() {
        return profitThreshold;
    }

    public void setProfitThreshold(double profitThreshold) {
        this.profitThreshold = profitThreshold;
    }

    public double getLossThreshold() {
        return lossThreshold;
    }

    public void setLossThreshold(double lossThreshold) {
        this.lossThreshold = lossThreshold;
    }
}
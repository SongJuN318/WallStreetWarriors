package com.example.registration_login_demo.dto;

public class BuyPendingOrderDTO {

    private String userId;
    private String symbol;
    private int lots;
    private double buyPrice;

    // Constructors, getters, and setters

    public BuyPendingOrderDTO(String userId, String symbol, int lots, double buyPrice) {
        this.userId = userId;
        this.symbol = symbol;
        this.lots = lots;
        this.buyPrice = buyPrice;
    }

    public BuyPendingOrderDTO() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getLots() {
        return lots;
    }

    public void setLots(int lots) {
        this.lots = lots;
    }

    public double getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(double buyPrice) {
        this.buyPrice = buyPrice;
    }
   
    
}

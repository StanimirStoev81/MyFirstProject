package org.example.myfirstproject.Models.DTO;

import java.math.BigDecimal;

public class EndReservationDTO {
    private Long id;
    private BigDecimal totalPrice;
    private BigDecimal totalOfferingPrice;
    private BigDecimal amount;

    public EndReservationDTO() {
    }

    public EndReservationDTO(Long id, BigDecimal totalPrice, BigDecimal totalOfferingPrice) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.totalOfferingPrice = totalOfferingPrice;
        this.amount = totalPrice.add(totalOfferingPrice); // Автоматично изчисляваме общата сума
    }

    // Getters and Setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getTotalOfferingPrice() {
        return totalOfferingPrice;
    }

    public void setTotalOfferingPrice(BigDecimal totalOfferingPrice) {
        this.totalOfferingPrice = totalOfferingPrice;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}

package org.example.myfirstproject.Models.DTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ReservationDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private BigDecimal totalPrice;
    private BigDecimal totalOfferingPrice;
    private List<OfferingDTO> offerings;

    public ReservationDTO() {
    }

    public ReservationDTO(String firstName, String lastName, String email, String phoneNumber, LocalDateTime startDate, LocalDateTime endDate, BigDecimal totalPrice, BigDecimal totalOfferingPrice, List<OfferingDTO> offerings) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalPrice = totalPrice;
        this.totalOfferingPrice = totalOfferingPrice;
        this.offerings = offerings;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
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

    public List<OfferingDTO> getOfferings() {
        return offerings;
    }

    public void setOfferings(List<OfferingDTO> offerings) {
        this.offerings = offerings;
    }
}

package org.example.myfirstproject.Models.DTO;

import org.example.myfirstproject.Models.Enums.PaymentMethodEnum;
import org.example.myfirstproject.Models.Enums.PaymentStatusEnum;

import java.math.BigDecimal;

public class PaymentDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private BigDecimal amount;
    private PaymentMethodEnum method;
    private PaymentStatusEnum status;

    // Конструктори, гетъри и сетъри
    public PaymentDTO(Long id, String firstName, String lastName, BigDecimal amount, PaymentMethodEnum method, PaymentStatusEnum status) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.amount = amount;
        this.method = method;
        this.status = status;
    }

    // Гетъри и сетъри
    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public PaymentMethodEnum getMethod() {
        return method;
    }

    public PaymentStatusEnum getStatus() {
        return status;
    }
}

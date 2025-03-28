package com.example.payment_service.model;

import com.example.payment_service.enums.PaymentStatus;
import com.example.payment_service.enums.PaymentType;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "payments")
public class Payment implements Serializable {

    @Id
    private String id;

    private String customerId;

    private String orderId;

    private String cargoId;

    private int quantity;

    private Double amount;

    private PaymentStatus paymentStatus = PaymentStatus.PENDING;


    private PaymentType paymentType;

    private LocalDateTime paymentDate;

    @PostConstruct
    private void init() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }
}

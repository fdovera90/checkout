package com.example.checkoutbackend.repository;

import com.example.checkoutbackend.model.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import com.example.checkoutbackend.model.PaymentMethodType;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {
    Optional<PaymentMethod> findByType(PaymentMethodType type);
}

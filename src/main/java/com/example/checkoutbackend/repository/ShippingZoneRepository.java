package com.example.checkoutbackend.repository;

import com.example.checkoutbackend.model.ShippingZone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShippingZoneRepository extends JpaRepository<ShippingZone, String> {
}

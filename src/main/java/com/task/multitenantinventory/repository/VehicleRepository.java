package com.task.multitenantinventory.repository;

import com.task.multitenantinventory.model.Vehicle;
import com.task.multitenantinventory.model.enums.SubscriptionType;
import com.task.multitenantinventory.model.enums.VehicleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, UUID> {
    Optional<Vehicle> findByIdAndTenantId(UUID id, UUID tenantId);

    @Query("""
    SELECT v FROM Vehicle v
    WHERE v.tenantId = :tenantId
    AND (:model IS NULL OR LOWER(v.model) LIKE LOWER(CONCAT('%', :model, '%')))
    AND (:status IS NULL OR v.status = :status)
    AND (:priceMin IS NULL OR v.price >= :priceMin)
    AND (:priceMax IS NULL OR v.price <= :priceMax)
""")
    Page<Vehicle> filterVehicles(
            String model,
            VehicleStatus status,
            BigDecimal priceMin,
            BigDecimal priceMax,
            UUID tenantId,
            Pageable pageable
    );

    @Query("""
    SELECT v FROM Vehicle v
    JOIN Dealer d ON v.dealerId = d.id
    WHERE d.subscriptionType = :subscription
    AND v.tenantId = :tenantId
""")
    Page<Vehicle> findBySubscriptionAndTenant(
            SubscriptionType subscription,
            UUID tenantId,
            Pageable pageable
    );
}

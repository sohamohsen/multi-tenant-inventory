package com.task.multitenantinventory.repository;

import com.task.multitenantinventory.model.Vehicle;
import com.task.multitenantinventory.model.enums.SubscriptionType;
import com.task.multitenantinventory.model.enums.VehicleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, UUID> {

    Optional<Vehicle> findByIdAndTenantId(UUID id, UUID tenantId);

    @Query("""
    SELECT v FROM Vehicle v
    JOIN Dealer d ON v.dealerId = d.id
    WHERE v.tenantId = :tenantId
    AND (:subscription IS NULL OR d.subscriptionType = :subscription)
    AND (CAST(:model AS string) IS NULL OR LOWER(v.model) LIKE LOWER(CONCAT('%', CAST(:model AS string), '%')))
    AND (:status IS NULL OR v.status = :status)
    AND (:priceMin IS NULL OR v.price >= :priceMin)
    AND (:priceMax IS NULL OR v.price <= :priceMax)
    """)
    Page<Vehicle> filterVehicles(
            @Param("model") String model,
            @Param("status") VehicleStatus status,
            @Param("priceMin") BigDecimal priceMin,
            @Param("priceMax") BigDecimal priceMax,
            @Param("subscription") SubscriptionType subscription,
            @Param("tenantId") UUID tenantId,
            Pageable pageable
    );
}
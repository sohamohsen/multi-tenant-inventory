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

    @Query(value = """
SELECT * FROM vehicles v
WHERE v.tenant_id = :tenantId
AND (:model IS NULL OR LOWER(v.model) LIKE LOWER(CONCAT('%', :model, '%')))
AND (:status IS NULL OR v.status = :status)
AND (:priceMin IS NULL OR v.price >= :priceMin)
AND (:priceMax IS NULL OR v.price <= :priceMax)
""",
            countQuery = """
SELECT count(*) FROM vehicles v
WHERE v.tenant_id = :tenantId
AND (:model IS NULL OR LOWER(v.model) LIKE LOWER(CONCAT('%', :model, '%')))
AND (:status IS NULL OR v.status = :status)
AND (:priceMin IS NULL OR v.price >= :priceMin)
AND (:priceMax IS NULL OR v.price <= :priceMax)
""",
            nativeQuery = true)
    Page<Vehicle> filterVehicles(
            @Param("model") String model,
            @Param("status") String status,
            @Param("priceMin") BigDecimal priceMin,
            @Param("priceMax") BigDecimal priceMax,
            @Param("tenantId") UUID tenantId,
            Pageable pageable
    );

    @Query("""
    SELECT v FROM Vehicle v
    JOIN Dealer d ON v.dealerId = d.id
    WHERE d.subscriptionType = :subscription
    AND v.tenantId = :tenantId
""")
    Page<Vehicle> findVehiclesBySubscriptionAndTenant(
            @Param("subscription") SubscriptionType subscription,
            @Param("tenantId") UUID tenantId,
            Pageable pageable
    );
}
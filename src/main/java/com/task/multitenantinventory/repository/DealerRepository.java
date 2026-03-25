package com.task.multitenantinventory.repository;

import com.task.multitenantinventory.model.Dealer;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DealerRepository extends JpaRepository<Dealer, UUID> {
    boolean existsByEmailAndTenantId(@NotBlank(message = "Email is required.") @Email(message = "Invalid email format") String email, UUID tenantId);

    Page<Dealer> findAllByTenantId(UUID tenantId, Pageable pageable);

    Optional<Dealer> findByIdAndTenantId(UUID id, UUID tenantId);

    @Query("""
        SELECT d.subscriptionType, COUNT(d)
        FROM Dealer d
        GROUP BY d.subscriptionType
    """)
    List<Object[]> countDealersBySubscription();

}

package com.task.multitenantinventory.service;

import com.task.multitenantinventory.common.PageResponse;
import com.task.multitenantinventory.common.exception.ResourceNotFoundException;
import com.task.multitenantinventory.common.tenant.TenantContext;
import com.task.multitenantinventory.dto.CreateVehicleRequest;
import com.task.multitenantinventory.dto.VehicleResponse;
import com.task.multitenantinventory.model.Dealer;
import com.task.multitenantinventory.model.Vehicle;
import com.task.multitenantinventory.model.enums.SubscriptionType;
import com.task.multitenantinventory.model.enums.VehicleStatus;
import com.task.multitenantinventory.repository.DealerRepository;
import com.task.multitenantinventory.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final DealerRepository dealerRepository;

    public VehicleResponse createVehicle(CreateVehicleRequest request) {

        UUID tenantId = TenantContext.getTenant();

        Dealer dealer = dealerRepository
                .findByIdAndTenantId(request.getDealerId(), tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Dealer not found"));

        Vehicle vehicle = Vehicle.builder()
                .id(UUID.randomUUID())
                .tenantId(tenantId)
                .dealerId(dealer.getId())
                .model(request.getModel())
                .price(request.getPrice())
                .status(request.getStatus())
                .build();

        Vehicle saved = vehicleRepository.save(vehicle);

        return mapToResponse(saved);
    }

    public VehicleResponse getVehicleById(UUID id) {

        UUID tenantId = TenantContext.getTenant();

        Vehicle vehicle = vehicleRepository
                .findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));

        return mapToResponse(vehicle);
    }

    public PageResponse<VehicleResponse> getVehicles(
            String model,
            VehicleStatus status,
            BigDecimal priceMin,
            BigDecimal priceMax,
            SubscriptionType subscription,
            Pageable pageable) {

        UUID tenantId = TenantContext.getTenant();

        Page<Vehicle> vehicles;

        // 🔥 subscription filter
        if (subscription != null) {

            vehicles = vehicleRepository
                    .findBySubscriptionAndTenant(subscription, tenantId, pageable);

        } else {

            vehicles = vehicleRepository
                    .filterVehicles(model, status, priceMin, priceMax, tenantId, pageable);
        }

        Page<VehicleResponse> mapped = vehicles.map(this::mapToResponse);

        return PageResponse.<VehicleResponse>builder()
                .content(mapped.getContent())
                .page(mapped.getNumber())
                .size(mapped.getSize())
                .totalElements(mapped.getTotalElements())
                .totalPages(mapped.getTotalPages())
                .last(mapped.isLast())
                .build();
    }
    private VehicleResponse mapToResponse(Vehicle vehicle) {
        return VehicleResponse.builder()
                .id(vehicle.getId())
                .dealerId(vehicle.getDealerId())
                .model(vehicle.getModel())
                .price(vehicle.getPrice())
                .status(vehicle.getStatus())
                .build();
    }
}

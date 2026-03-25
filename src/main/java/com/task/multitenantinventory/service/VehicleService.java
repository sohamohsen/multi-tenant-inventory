package com.task.multitenantinventory.service;

import com.task.multitenantinventory.common.PageResponse;
import com.task.multitenantinventory.common.exception.ResourceNotFoundException;
import com.task.multitenantinventory.common.tenant.TenantContext;
import com.task.multitenantinventory.dto.CreateVehicleRequest;
import com.task.multitenantinventory.dto.UpdateVehicleRequest;
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
import java.util.List;
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

        Page<Vehicle> vehicles = vehicleRepository.filterVehicles(
                model,
                status,
                priceMin,
                priceMax,
                subscription,
                tenantId,
                pageable
        );

        return mapToPageResponse(vehicles);
    }
    public VehicleResponse updateVehicle(UUID id, UpdateVehicleRequest request) {

        UUID tenantId = TenantContext.getTenant();

        Vehicle vehicle = vehicleRepository
                .findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));

        if (request.getModel() != null && !request.getModel().isBlank()) {
            vehicle.setModel(request.getModel());
        }

        if (request.getPrice() != null) {
            vehicle.setPrice(request.getPrice());
        }

        if (request.getStatus() != null) {
            vehicle.setStatus(request.getStatus());
        }

        Vehicle updated = vehicleRepository.save(vehicle);

        return mapToResponse(updated);
    }

    public void deleteVehicle(UUID id) {

        UUID tenantId = TenantContext.getTenant();

        Vehicle vehicle = vehicleRepository
                .findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));

        vehicleRepository.delete(vehicle);
    }

    private PageResponse<VehicleResponse> mapToPageResponse(Page<Vehicle> vehicles) {

        List<VehicleResponse> content = vehicles.getContent()
                .stream()
                .map(this::mapToResponse)
                .toList();

        return new PageResponse<>(
                content,
                vehicles.getNumber(),
                vehicles.getSize(),
                vehicles.getTotalElements(),
                vehicles.getTotalPages(),
                vehicles.isLast()
        );
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

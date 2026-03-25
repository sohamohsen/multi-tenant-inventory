package com.task.multitenantinventory.controller;

import com.task.multitenantinventory.common.PageResponse;
import com.task.multitenantinventory.dto.CreateVehicleRequest;
import com.task.multitenantinventory.dto.UpdateVehicleRequest;
import com.task.multitenantinventory.dto.VehicleResponse;
import com.task.multitenantinventory.model.enums.SubscriptionType;
import com.task.multitenantinventory.model.enums.VehicleStatus;
import com.task.multitenantinventory.service.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@Tag(name = "Vehicle APIs")
@RestController
@RequestMapping("/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @Operation(summary = "Create a new vehicle")
    @PostMapping
    public ResponseEntity<VehicleResponse> createVehicle(
            @Valid @RequestBody CreateVehicleRequest request) {

        VehicleResponse response = vehicleService.createVehicle(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Get vehicle by ID")
    @GetMapping("/{id}")
    public ResponseEntity<VehicleResponse> getVehicleById(@PathVariable UUID id) {

        VehicleResponse response = vehicleService.getVehicleById(id);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get vehicles with filtering, pagination, and subscription")
    @GetMapping
    public ResponseEntity<PageResponse<VehicleResponse>> getVehicles(
            @RequestParam(required = false) String model,
            @RequestParam(required = false) VehicleStatus status,
            @RequestParam(required = false) BigDecimal priceMin,
            @RequestParam(required = false) BigDecimal priceMax,
            @RequestParam(required = false) SubscriptionType subscription,
            Pageable pageable) {

        return ResponseEntity.ok(
                vehicleService.getVehicles(model, status, priceMin, priceMax, subscription, pageable)
        );
    }

    @Operation(summary = "Update vehicle")
    @PatchMapping("/{id}")
    public ResponseEntity<VehicleResponse> updateVehicle(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateVehicleRequest request) {

        return ResponseEntity.ok(vehicleService.updateVehicle(id, request));
    }

    @Operation(summary = "Delete vehicle")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable UUID id) {

        vehicleService.deleteVehicle(id);

        return ResponseEntity.noContent().build();
    }
}
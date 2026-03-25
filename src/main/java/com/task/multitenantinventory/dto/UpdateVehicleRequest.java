package com.task.multitenantinventory.dto;

import com.task.multitenantinventory.model.enums.VehicleStatus;
import jakarta.validation.constraints.DecimalMin;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class UpdateVehicleRequest {

    private String model;

    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;

    private VehicleStatus status;
}
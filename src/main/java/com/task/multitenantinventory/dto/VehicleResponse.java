package com.task.multitenantinventory.dto;

import com.task.multitenantinventory.model.enums.VehicleStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VehicleResponse {

    private UUID id;

    private UUID dealerId;

    private String model;

    private BigDecimal price;

    private VehicleStatus status;
}
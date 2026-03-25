package com.task.multitenantinventory.controller;

import com.task.multitenantinventory.common.PageResponse;
import com.task.multitenantinventory.dto.DealerRequest;
import com.task.multitenantinventory.dto.DealerResponse;
import com.task.multitenantinventory.dto.UpdateDealerRequest;
import com.task.multitenantinventory.service.DealerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Dealer APIs")
@RestController
@RequestMapping("/dealers")
@RequiredArgsConstructor
public class DealerController {
    private final DealerService dealerService;

    @Operation(
            summary = "Create a new dealer",
            description = "Creates a dealer for the current tenant"

    )
    @PostMapping()
    public ResponseEntity<DealerResponse> createDealer(@Valid @RequestBody DealerRequest request){
        DealerResponse response = dealerService.createDealer(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Get dealer by ID")
    @GetMapping("/{id}")
    public ResponseEntity<DealerResponse> getDealerById(@PathVariable UUID id){
        DealerResponse response = dealerService.getDealerById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get all dealers with pagination and sorting")
    @GetMapping
    public ResponseEntity<PageResponse<DealerResponse>> getAllDealers(
            Pageable pageable) {

        PageResponse<DealerResponse> response =
                dealerService.getAllDealers(pageable);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update dealer")
    @PatchMapping("/{id}")
    public ResponseEntity<DealerResponse> updateDealer(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateDealerRequest request) {

        return ResponseEntity.ok(dealerService.updateDealer(id, request));
    }

    @Operation(summary = "Delete dealer")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDealer(@PathVariable UUID id) {

        dealerService.deleteDealer(id);

        return ResponseEntity.noContent().build();
    }

}

package com.task.multitenantinventory.controller;

import com.task.multitenantinventory.common.PageResponse;
import com.task.multitenantinventory.dto.DealerRequest;
import com.task.multitenantinventory.dto.DealerResponse;
import com.task.multitenantinventory.dto.UpdateDealerRequest;
import com.task.multitenantinventory.service.DealerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/dealers")
@RequiredArgsConstructor
public class DealerController {
    private final DealerService dealerService;

    @PostMapping()
    public ResponseEntity<DealerResponse> createDealer(@Valid @RequestBody DealerRequest request){
        DealerResponse response = dealerService.createDealer(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DealerResponse> getDealerById(@PathVariable UUID id){
        DealerResponse response = dealerService.getDealerById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<PageResponse<DealerResponse>> getAllDealers(
            Pageable pageable) {

        PageResponse<DealerResponse> response =
                dealerService.getAllDealers(pageable);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DealerResponse> updateDealer(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateDealerRequest request) {

        return ResponseEntity.ok(dealerService.updateDealer(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDealer(@PathVariable UUID id) {

        dealerService.deleteDealer(id);

        return ResponseEntity.noContent().build();
    }

}

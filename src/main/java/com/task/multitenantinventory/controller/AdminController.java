package com.task.multitenantinventory.controller;

import com.task.multitenantinventory.service.DealerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Tag(name = "Admin APIs")
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final DealerService dealerService;

    @Operation(summary = "Count dealers by subscription (GLOBAL ADMIN)")
    @GetMapping("/dealers/countBySubscription")
    public ResponseEntity<Map<String, Long>> countDealersBySubscription() {

        return ResponseEntity.ok(dealerService.countDealersBySubscription());
    }
}
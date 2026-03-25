package com.task.multitenantinventory.dto;

import com.task.multitenantinventory.model.enums.SubscriptionType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class DealerResponse {
    private UUID id;
    private String name;
    private String email;
    private SubscriptionType subscriptionType;
    private LocalDateTime createAt;
    private LocalDateTime updatedAt;
}

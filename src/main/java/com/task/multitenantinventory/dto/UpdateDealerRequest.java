package com.task.multitenantinventory.dto;

import com.task.multitenantinventory.model.enums.SubscriptionType;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateDealerRequest {

    private String name;

    @Email(message = "Invalid email format")
    private String email;

    private SubscriptionType subscriptionType;
}
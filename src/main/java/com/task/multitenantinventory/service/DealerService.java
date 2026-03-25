package com.task.multitenantinventory.service;

import com.task.multitenantinventory.common.PageResponse;
import com.task.multitenantinventory.common.exception.BadRequestException;
import com.task.multitenantinventory.common.exception.ForbiddenException;
import com.task.multitenantinventory.common.exception.ResourceNotFoundException;
import com.task.multitenantinventory.common.tenant.TenantContext;
import com.task.multitenantinventory.dto.DealerRequest;
import com.task.multitenantinventory.dto.DealerResponse;
import com.task.multitenantinventory.dto.UpdateDealerRequest;
import com.task.multitenantinventory.model.Dealer;
import com.task.multitenantinventory.repository.DealerRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DealerService {
    private final DealerRepository dealerRepository;

    public DealerResponse createDealer(@Valid DealerRequest request) {
        UUID tenantId = TenantContext.getTenant();

        if(tenantId == null){
            throw new BadRequestException("Missing X-Tenant-Id header");
        }

        if (dealerRepository.existsByEmailAndTenantId(request.getEmail(), tenantId)) {
            throw new BadRequestException("Dealer with this email already exists");
        }

        Dealer dealer = Dealer.builder()
                .id(UUID.randomUUID())
                .tenantId(tenantId)
                .name(request.getName())
                .email(request.getEmail())
                .subscriptionType(request.getSubscriptionType())
                .build();

        Dealer saved = dealerRepository.save(dealer);
        return mapToResponse(saved);
    }

    public DealerResponse getDealerById(UUID id){
        UUID tenantId = TenantContext.getTenant();
        Dealer dealer = dealerRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Dealer not found."));

        if(!dealer.getTenantId().equals(tenantId)){
            throw new ForbiddenException("Access denied");
        }

        return mapToResponse(dealer);
    }

    public PageResponse<DealerResponse> getAllDealers(Pageable pageable) {

        UUID tenantId = TenantContext.getTenant();

        Page<Dealer> dealers = dealerRepository
                .findAllByTenantId(tenantId, pageable);

        Page<DealerResponse> mapped = dealers.map(this::mapToResponse);

        return PageResponse.<DealerResponse>builder()
                .content(mapped.getContent())
                .page(mapped.getNumber())
                .size(mapped.getSize())
                .totalElements(mapped.getTotalElements())
                .totalPages(mapped.getTotalPages())
                .last(mapped.isLast())
                .build();
    }

    private DealerResponse mapToResponse(Dealer dealer) {
        return DealerResponse.builder()
                .id(dealer.getId())
                .name(dealer.getName())
                .email(dealer.getEmail())
                .subscriptionType(dealer.getSubscriptionType())
                .createAt(dealer.getCreateAt())
                .updatedAt(dealer.getUpdateAt())
                .build();
    }

    public DealerResponse updateDealer(UUID id, UpdateDealerRequest request) {

        UUID tenantId = TenantContext.getTenant();

        Dealer dealer = dealerRepository
                .findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Dealer not found"));

        //  name
        if (request.getName() != null && !request.getName().isBlank()) {
            dealer.setName(request.getName());
        }

        //  email
        if (request.getEmail() != null && !request.getEmail().isBlank()) {

            if (!dealer.getEmail().equals(request.getEmail())) {

                if (dealerRepository.existsByEmailAndTenantId(request.getEmail(), tenantId)) {
                    throw new BadRequestException("Email already exists");
                }

                dealer.setEmail(request.getEmail());
            }
        }

        //  subscription
        if (request.getSubscriptionType() != null) {
            dealer.setSubscriptionType(request.getSubscriptionType());
        }

        Dealer updated = dealerRepository.save(dealer);

        return mapToResponse(updated);
    }

    public void deleteDealer(UUID id) {

        UUID tenantId = TenantContext.getTenant();

        Dealer dealer = dealerRepository
                .findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Dealer not found"));

        dealerRepository.delete(dealer);
    }
}

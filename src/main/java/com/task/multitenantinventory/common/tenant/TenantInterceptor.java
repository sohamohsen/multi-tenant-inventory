package com.task.multitenantinventory.common.tenant;

import com.task.multitenantinventory.common.exception.BadRequestException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@Component
public class TenantInterceptor implements HandlerInterceptor {

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        String tenantHeader = request.getHeader("X-Tenant-Id");
        if(tenantHeader == null || tenantHeader.isEmpty()){
            throw new BadRequestException("Missing X_Tenant_Id header");
        }
        try {
            UUID tenantId = UUID.fromString(tenantHeader);
            TenantContext.setTenant(tenantId);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid tenant ID format");
        }
        return true;
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex){
        TenantContext.clear();
    }
}

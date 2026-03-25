package com.task.multitenantinventory.common.tenant;

import com.task.multitenantinventory.common.exception.BadRequestException;
import com.task.multitenantinventory.common.exception.ForbiddenException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@Component
public class TenantInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        String uri = request.getRequestURI();

        // 🔥 استثناء Swagger + static resources
        if (uri.startsWith("/swagger-ui")
                || uri.startsWith("/v3/api-docs")
                || uri.startsWith("/swagger-resources")
                || uri.startsWith("/webjars")) {
            return true;
        }

        // 👑 Admin protection
        if (uri.startsWith("/admin")) {

            String role = request.getHeader("X-Role");

            if (!"GLOBAL_ADMIN".equals(role)) {
                throw new ForbiddenException("Access denied");
            }

            return true;
        }

        // 🔐 Tenant logic
        String tenantHeader = request.getHeader("X-Tenant-Id");

        if (tenantHeader == null || tenantHeader.isEmpty()) {
            throw new BadRequestException("Missing X-Tenant-Id header");
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

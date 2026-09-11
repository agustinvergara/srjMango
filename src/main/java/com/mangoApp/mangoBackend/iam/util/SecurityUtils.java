package com.mangoApp.mangoBackend.iam.util;

import com.mangoApp.mangoBackend.iam.security.UserPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {
    
    public static Long getCurrentTenantId() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal principal) {
            return principal.tenantId();
        }
        throw new IllegalStateException("No hay contexto de seguridad o tenant disponible");
    }
}
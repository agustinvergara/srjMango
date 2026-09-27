package com.mangoApp.mangoBackend.iam.service;

import com.mangoApp.mangoBackend.iam.model.Tenant;
import com.mangoApp.mangoBackend.iam.model.User;
import com.mangoApp.mangoBackend.iam.model.dto.LoginRequest;
import com.mangoApp.mangoBackend.iam.model.dto.RegisterRequest;
import com.mangoApp.mangoBackend.iam.repository.TenantRepository;
import com.mangoApp.mangoBackend.iam.repository.UserRepository;
import com.mangoApp.mangoBackend.iam.security.JwtService;
import com.mangoApp.mangoBackend.marketplace.logistica.model.Vehicle;
import com.mangoApp.mangoBackend.marketplace.logistica.repository.VehicleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final TenantRepository tenantRepository;
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(TenantRepository tenantRepository, UserRepository userRepository, VehicleRepository vehicleRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.tenantRepository = tenantRepository;
        this.userRepository = userRepository;
        this.vehicleRepository = vehicleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public String[] registerUser(RegisterRequest req) {
        String reqRole = req.role() != null ? req.role().toLowerCase() : "";
        String businessType = "productor".equals(reqRole) ? "PRODUCER" : "CARRIER";
        String dbRole = "productor".equals(reqRole) ? "FARMER" : "DRIVER";

        String farmName = null, province = null, crops = null;
        Boolean hasRefrigeration = false;

        if (req.profile() != null) {
            farmName = req.profile().farmName();
            province = req.profile().province();
            crops = req.profile().crops();
            hasRefrigeration = req.profile().hasRefrigeration();
        }

        Tenant tenant = new Tenant(
            null, 
            req.companyName(), 
            businessType, 
            req.taxId(), 
            true, 
            null, 
            province, 
            farmName, 
            crops, 
            hasRefrigeration
        );
        Long tenantId = tenantRepository.save(tenant);

        String passwordHash = passwordEncoder.encode(req.password()); 

        User user = new User(
            null,
            tenantId,
            req.email(),
            passwordHash,
            dbRole,
            true,
            req.fullName(),
            req.phone()
        );
        userRepository.save(user);

        if ("CARRIER".equals(businessType) && req.profile() != null) {
            Integer capacityUnits = (req.profile().capacityKg() != null) ? req.profile().capacityKg() / 20 : 0;
            String vType = "SMALL_TRUCK";
            if ("Pick-up".equalsIgnoreCase(req.profile().vehicleType())) vType = "PICKUP";
            if (Boolean.TRUE.equals(req.profile().refrigerated())) vType = "REFRIGERATED_TRUCK";

            Vehicle vehicle = new Vehicle(
                null,
                tenantId,
                req.profile().plate(),
                vType,
                capacityUnits,
                true,
                null, null, null
            );
            vehicleRepository.save(vehicle);
        }

        String token = jwtService.generateToken(tenantId, req.email(), reqRole);
        return new String[]{token, String.valueOf(tenantId), reqRole};
    }

    public String[] loginUser(LoginRequest req) {
        User user = userRepository.findByEmail(req.email())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
        if (!passwordEncoder.matches(req.password(), user.passwordHash())) {
            throw new RuntimeException("Credenciales inválidas");
        }
        
        String reqRole = "FARMER".equals(user.role()) ? "productor" : "transportista";
        String token = jwtService.generateToken(user.tenantId(), user.email(), reqRole);
        return new String[]{token, String.valueOf(user.tenantId()), reqRole};
    }
}

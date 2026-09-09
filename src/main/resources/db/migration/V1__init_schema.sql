-- 1. IAM & Multitenancy (La base de todo)
CREATE TABLE tenants (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    business_type ENUM('PRODUCER', 'BUYER', 'CARRIER', 'HUB') NOT NULL,
    ruc VARCHAR(50) UNIQUE NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tenant_id BIGINT NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'DRIVER', 'FARMER', 'STORE_MANAGER') NOT NULL,
    is_verified BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_users_tenant FOREIGN KEY (tenant_id) REFERENCES tenants(id) ON DELETE CASCADE
);

-- 2. Marketplace de Perecederos
CREATE TABLE products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tenant_id BIGINT NOT NULL, -- El productor dueño
    name VARCHAR(100) NOT NULL,
    category ENUM('FRUIT', 'VEGETABLE', 'ROOT', 'OTHER') NOT NULL,
    requires_refrigeration BOOLEAN DEFAULT FALSE,
    base_price_per_unit DECIMAL(10, 2) NOT NULL,
    unit_type VARCHAR(20) DEFAULT 'CANASTILLA_20KG', -- Estandarización Ninjacart
    stock_available INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_products_tenant FOREIGN KEY (tenant_id) REFERENCES tenants(id)
);

-- 3. Marketplace de Logística (Directorio de Transporte)
CREATE TABLE vehicles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tenant_id BIGINT NOT NULL, -- El transportista dueño
    plate_number VARCHAR(20) UNIQUE NOT NULL,
    vehicle_type ENUM('PICKUP', 'SMALL_TRUCK', 'REFRIGERATED_TRUCK') NOT NULL,
    capacity_in_units INT NOT NULL, -- Cuántas canastillas caben
    is_available BOOLEAN DEFAULT TRUE,
    current_lat DECIMAL(10, 8), -- Integración con motor de rutas
    current_lng DECIMAL(11, 8),
    last_location_update TIMESTAMP,
    CONSTRAINT fk_vehicles_tenant FOREIGN KEY (tenant_id) REFERENCES tenants(id)
);

-- Índices para optimizar las búsquedas multitenant y espaciales
CREATE INDEX idx_users_tenant ON users(tenant_id);
CREATE INDEX idx_products_tenant ON products(tenant_id);
CREATE INDEX idx_vehicles_tenant ON vehicles(tenant_id);

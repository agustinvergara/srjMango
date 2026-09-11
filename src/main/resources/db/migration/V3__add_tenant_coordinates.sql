-- Agregar latitud y longitud a las fincas y comercios
ALTER TABLE tenants
ADD COLUMN lat DECIMAL(10,8) NULL AFTER is_active,
ADD COLUMN lng DECIMAL(11,8) NULL AFTER lat;

-- Ubicar al Productor en Cerro Azul
UPDATE tenants SET lat = 9.17640000, lng = -79.41440000 WHERE id = 1;

-- Ubicar al Comprador (Minisuper) en Merca Panamá
UPDATE tenants SET lat = 9.04300000, lng = -79.53900000 WHERE id = 2;
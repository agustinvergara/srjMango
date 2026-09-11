package com.mangoApp.mangoBackend.rutas.service;

import com.graphhopper.GraphHopper;
import com.graphhopper.config.Profile;
import com.graphhopper.util.CustomModel;
import com.graphhopper.json.Statement; // Importante para definir las reglas
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.ResponsePath;
import com.mangoApp.mangoBackend.rutas.model.RouteResult;

@Service
public class RoutingEngineService {

    private GraphHopper hopper;

    @PostConstruct
    public void init() {
        hopper = new GraphHopper();
        
        // 1. Apunta al archivo OSM descargado
        hopper.setOSMFile("panama-260909.osm.pbf");
        
        // 2. Carpeta donde GraphHopper guarda su caché
        hopper.setGraphHopperLocation("graph-cache");
        
        // 3. ¡La pieza clave para GH 9.1! Le decimos qué atributos extraer del mapa
        hopper.setEncodedValuesString("car_access, car_average_speed, road_access, max_speed");
        
        // 4. Creamos el modelo y le agregamos la regla base de velocidad
        CustomModel carModel = new CustomModel();
        carModel.addToSpeed(Statement.If("true", Statement.Op.LIMIT, "car_average_speed"));
        
        // 5. Asignamos el perfil
        hopper.setProfiles(new Profile("car").setCustomModel(carModel));
        
        // 6. Cargar en memoria (Esto tomará unos segundos la primera vez que compile)
        hopper.importOrLoad();
    }
    
    public GraphHopper getHopper() {
        return hopper;
    }
    
    public RouteResult calculateRoute(double fromLat, double fromLng, double toLat, double toLng) {
        GHRequest req = new GHRequest(fromLat, fromLng, toLat, toLng).setProfile("car");
        GHResponse rsp = hopper.route(req);

        if (rsp.hasErrors()) {
            throw new RuntimeException("Error calculando ruta de GraphHopper: " + rsp.getErrors());
        }

        ResponsePath path = rsp.getBest();
        
        // GraphHopper devuelve la distancia en metros y el tiempo en milisegundos
        double distanceKm = path.getDistance() / 1000.0;
        long timeMinutes = path.getTime() / 60000;
        
        return new RouteResult(distanceKm, timeMinutes);
    }
}
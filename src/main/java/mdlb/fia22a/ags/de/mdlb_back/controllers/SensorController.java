package mdlb.fia22a.ags.de.mdlb_back.controllers;

import mdlb.fia22a.ags.de.mdlb_back.models.Sensor;
import mdlb.fia22a.ags.de.mdlb_back.services.SensorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sensors")
public class SensorController {

    private final SensorService sensorService;

    public SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @GetMapping
    public List<Sensor> getAllSensors() {
        return this.sensorService.getAllSensors();
    }

    @GetMapping("/{id}")
    public Sensor getSensorById(@PathVariable Integer id) {
        return this.sensorService.getSensorById(id);
    }

    @PutMapping("/{id}/max-temperature")
    public ResponseEntity<Sensor> updateMaxTemperature(@PathVariable Integer id, @RequestBody Map<String, Float> payload) {
        Float maxTemperature = payload.get("maxTemperature");
        Sensor updatedSensor = this.sensorService.updateSensorMaxTemperature(id, maxTemperature);
        return ResponseEntity.ok(updatedSensor);
    }
}

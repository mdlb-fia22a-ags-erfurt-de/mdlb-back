package mdlb.fia22a.ags.de.mdlb_back.services;

import mdlb.fia22a.ags.de.mdlb_back.exceptions.ResourceNotFoundException;
import mdlb.fia22a.ags.de.mdlb_back.models.Sensor;
import mdlb.fia22a.ags.de.mdlb_back.repositories.SensorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SensorService {
    private final SensorRepository sensorRepository;

    public SensorService(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    public Sensor getSensor(int sensorId) {
        return this.sensorRepository.findById(sensorId).orElse(null);
    }

    public void deleteSensor(int sensorId) {
        this.sensorRepository.deleteById(sensorId);
    }

    public List<Sensor> getAllSensors() {
        return this.sensorRepository.findAll();
    }

    public Sensor getSensorById(Integer id) {
        return this.sensorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Sensor not found"));
    }

    @Transactional
    public Sensor updateSensorMaxTemperature(Integer id, Float maxTemperature) {
        Sensor sensor = getSensorById(id);
        sensor.setMaxTemperature(maxTemperature);
        return this.sensorRepository.save(sensor);
    }
}

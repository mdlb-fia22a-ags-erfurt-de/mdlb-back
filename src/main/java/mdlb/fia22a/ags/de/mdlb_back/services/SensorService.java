package mdlb.fia22a.ags.de.mdlb_back.services;

import mdlb.fia22a.ags.de.mdlb_back.models.Sensor;
import mdlb.fia22a.ags.de.mdlb_back.repositories.SensorRepository;
import org.springframework.stereotype.Service;

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
}

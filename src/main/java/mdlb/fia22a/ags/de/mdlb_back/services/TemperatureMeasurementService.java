package mdlb.fia22a.ags.de.mdlb_back.services;

import mdlb.fia22a.ags.de.mdlb_back.repositories.TemperatureMeasurementRepository;
import org.springframework.stereotype.Service;

@Service
public class TemperatureMeasurementService {
    private final TemperatureMeasurementRepository temperatureMeasurementRepository;

    public TemperatureMeasurementService(TemperatureMeasurementRepository temperatureMeasurementRepository) {
        this.temperatureMeasurementRepository = temperatureMeasurementRepository;
    }

    public void deleteTemperatureMeasurement(int measurementId) {
        this.temperatureMeasurementRepository.deleteById(measurementId);
    }
}

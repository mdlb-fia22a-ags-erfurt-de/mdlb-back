package mdlb.fia22a.ags.de.mdlb_back.repositories;

import mdlb.fia22a.ags.de.mdlb_back.models.TemperatureMeasurement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemperatureMeasurementRepository extends JpaRepository<TemperatureMeasurement, Integer> { }

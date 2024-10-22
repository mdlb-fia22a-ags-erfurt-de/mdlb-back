package mdlb.fia22a.ags.de.mdlb_back.models;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "temperaturemeasurement")
public class TemperatureMeasurement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer measurementId;

    @ManyToOne
    @JoinColumn(name = "sensor_id")
    private Sensor sensor;

    private Float temperature;
    private Date timestamp;


    public TemperatureMeasurement(Integer measurementId, Sensor sensor, Float temperature, Date timestamp) {
        this.measurementId = measurementId;
        this.sensor = sensor;
        this.temperature = temperature;
        this.timestamp = timestamp;
    }

    public TemperatureMeasurement() {
    }

    public Integer getMeasurementId() {
        return this.measurementId;
    }

    public void setMeasurementId(Integer measurementId) {
        this.measurementId = measurementId;
    }

    public Sensor getSensor() {
        return this.sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public Float getTemperature() {
        return this.temperature;
    }

    public void setTemperature(Float temperature) {
        this.temperature = temperature;
    }

    public Date getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}

package mdlb.fia22a.ags.de.mdlb_back.models;

import jakarta.persistence.*;

@Entity
@Table(name = "sensor")
public class Sensor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer sensorId;

    private String manufacturer;

    private String model;

    private String location;

    private Float maxTemperature;

    public Sensor(Integer sensorId, String manufacturer, String model, String location, Float maxTemperature) {
        this.sensorId = sensorId;
        this.manufacturer = manufacturer;
        this.model = model;
        this.location = location;
        this.maxTemperature = maxTemperature;
    }

    public Sensor() {}


    public Integer getSensorId() {
        return this.sensorId;
    }

    public void setSensorId(Integer sensorId) {
        this.sensorId = sensorId;
    }

    public String getManufacturer() {
        return this.manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return this.model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Float getMaxTemperature() {
        return this.maxTemperature;
    }

    public void setMaxTemperature(Float maxTemperature) {
        this.maxTemperature = maxTemperature;
    }
}

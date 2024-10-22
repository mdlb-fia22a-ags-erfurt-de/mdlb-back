package mdlb.fia22a.ags.de.mdlb_back.models;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "log")
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer logId;

    @ManyToOne
    @JoinColumn(name = "sensor_id")
    private Sensor sensor;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Float oldMaxTemperature;
    private Float newMaxTemperature;
    private Date modificationTime;


    public Log(Integer logId, Sensor sensor, User user, Float oldMaxTemperature, Float newMaxTemperature, Date modificationTime) {
        this.logId = logId;
        this.sensor = sensor;
        this.user = user;
        this.oldMaxTemperature = oldMaxTemperature;
        this.newMaxTemperature = newMaxTemperature;
        this.modificationTime = modificationTime;
    }

    public Log() {  }

    public Integer getLogId() {
        return this.logId;
    }

    public void setLogId(Integer logId) {
        this.logId = logId;
    }

    public Sensor getSensor() {
        return this.sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Float getOldMaxTemperature() {
        return this.oldMaxTemperature;
    }

    public void setOldMaxTemperature(Float oldMaxTemperature) {
        this.oldMaxTemperature = oldMaxTemperature;
    }

    public Float getNewMaxTemperature() {
        return this.newMaxTemperature;
    }

    public void setNewMaxTemperature(Float newMaxTemperature) {
        this.newMaxTemperature = newMaxTemperature;
    }

    public Date getModificationTime() {
        return this.modificationTime;
    }

    public void setModificationTime(Date modificationTime) {
        this.modificationTime = modificationTime;
    }
}

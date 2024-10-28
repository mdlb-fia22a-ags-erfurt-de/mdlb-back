package mdlb.fia22a.ags.de.mdlb_back.services;

import mdlb.fia22a.ags.de.mdlb_back.models.Log;
import mdlb.fia22a.ags.de.mdlb_back.models.Sensor;
import mdlb.fia22a.ags.de.mdlb_back.repositories.LogRepository;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class LogService {
    private final LogRepository logRepository;

    public LogService(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public List<Log> getAllLogs() {
        return this.logRepository.findAll();
    }

    public String getFormattedLogs() {
        List<Log> logs = getAllLogs();
        if (logs.isEmpty()) {
            return "No logs found.";
        }

        StringBuilder sb = new StringBuilder("Logs:\n");

        for (Log log : logs) {
            sb.append(String.format("ID: %d, Old Max Temp: %.2f, New Max Temp: %.2f, Modified: %s\n",
                    log.getLogId(), log.getOldMaxTemperature(), log.getNewMaxTemperature(),
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(log.getModificationTime())));
        }
        return sb.toString();
    }

    public void logMaxTemperatureChange(Sensor sensor, Float oldMaxTemperature, Float newMaxTemperature) {
        Log log = new Log();
        log.setSensor(sensor);
        log.setOldMaxTemperature(oldMaxTemperature);
        log.setNewMaxTemperature(newMaxTemperature);
        // log.setModificationTime(LocalDateTime.now());
        logRepository.save(log);
    }
}

package mdlb.fia22a.ags.de.mdlb_back.services;

import mdlb.fia22a.ags.de.mdlb_back.models.Log;
import mdlb.fia22a.ags.de.mdlb_back.repositories.LogRepository;
import org.springframework.stereotype.Service;

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
}

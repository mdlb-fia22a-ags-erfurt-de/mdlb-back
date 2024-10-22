package mdlb.fia22a.ags.de.mdlb_back.commands;

import mdlb.fia22a.ags.de.mdlb_back.models.Log;
import mdlb.fia22a.ags.de.mdlb_back.models.Sensor;
import mdlb.fia22a.ags.de.mdlb_back.models.User;
import mdlb.fia22a.ags.de.mdlb_back.services.LogService;
import mdlb.fia22a.ags.de.mdlb_back.services.SensorService;
import mdlb.fia22a.ags.de.mdlb_back.services.TemperatureMeasurementService;
import mdlb.fia22a.ags.de.mdlb_back.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.List;

@ShellComponent
public class AdminCommands {
    private final UserService userService;
    private final LogService logService;
    private final TemperatureMeasurementService temperatureService;
    private final SensorService sensorService;
    private boolean isLoggedIn = false;

    @Autowired
    public AdminCommands(UserService userService, LogService logService,
                         TemperatureMeasurementService temperatureService,
                         SensorService sensorService) {
        this.userService = userService;
        this.logService = logService;
        this.temperatureService = temperatureService;
        this.sensorService = sensorService;
    }

    @ShellMethod(value = "Login as admin", key = "login")
    public String login(@ShellOption String username, @ShellOption String password) {
        if (this.userService.authenticateAdmin(username, password)) {
            this.isLoggedIn = true;
            return "Logged in successfully as admin.";
        }
        return "Invalid credentials.";
    }

    @ShellMethod(value = "View log table", key = "view-logs")
    public String viewLogs() {
        if (!this.isLoggedIn) return "Please login first.";
        List<Log> logs = this.logService.getAllLogs();
        StringBuilder sb = new StringBuilder();
        for (Log log : logs) {
            sb.append(log.toString()).append("\n");
        }
        return sb.toString();
    }

    @ShellMethod(value = "Manage users", key = "manage-users")
    public String manageUsers(
            @ShellOption String action,
            @ShellOption String username,
            @ShellOption(defaultValue = "") String password,
            @ShellOption(defaultValue = "false") boolean isAdmin
    ) {
        if (!this.isLoggedIn) return "Please login first.";
        return switch (action) {
            case "view" -> {
                User user = this.userService.getUser(username);
                yield user != null ? user.toString() : "User not found.";
            }
            case "create" -> {
                User newUser = this.userService.createUser(username, password, isAdmin);
                yield "User created: " + newUser.toString();
            }
            case "delete" -> {
                this.userService.deleteUser(username);
                yield "User deleted";
            }
            default -> "Invalid action.";
        };
    }

    @ShellMethod(value = "Delete temperature data", key = "delete-temp")
    public String deleteTemperatureData(@ShellOption int measurementId) {
        if (!this.isLoggedIn) return "Please login first.";
        this.temperatureService.deleteTemperatureMeasurement(measurementId);
        return "Temperature data deleted.";
    }

    @ShellMethod(value = "Manage sensor data", key = "manage-sensor")
    public String manageSensorData(@ShellOption String action, @ShellOption int sensorId) {
        if (!isLoggedIn) return "Please login first.";
        return switch (action) {
            case "view" -> {
                Sensor sensor = this.sensorService.getSensor(sensorId);
                yield sensor != null ? sensor.toString() : "Sensor not found.";
            }
            case "delete" -> {
                this.sensorService.deleteSensor(sensorId);
                yield "Sensor deleted.";
            }
            default -> "Invalid action.";
        };
    }
}

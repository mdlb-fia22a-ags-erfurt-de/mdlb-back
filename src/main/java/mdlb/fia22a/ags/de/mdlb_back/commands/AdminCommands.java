package mdlb.fia22a.ags.de.mdlb_back.commands;

import jakarta.annotation.PostConstruct;
import mdlb.fia22a.ags.de.mdlb_back.models.Sensor;
import mdlb.fia22a.ags.de.mdlb_back.models.User;
import mdlb.fia22a.ags.de.mdlb_back.services.LogService;
import mdlb.fia22a.ags.de.mdlb_back.services.SensorService;
import mdlb.fia22a.ags.de.mdlb_back.services.TemperatureMeasurementService;
import mdlb.fia22a.ags.de.mdlb_back.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;

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

    @PostConstruct
    public void init() {
        System.out.println("=======================================================");
        System.out.println("Welcome to the Temperature Monitoring System CLI!");
        System.out.println("Type 'help' to see available commands.");
        System.out.println("Please use 'login <username> <password>' to access admin features.");
        System.out.println("=======================================================");
    }

    @ShellMethod(value = "Login as admin", key = "login")
    public String login(
            @ShellOption(value = {"-u", "--username"}, help = "Admin username") String username,
            @ShellOption(value = {"-p", "--password"}, help = "Admin password") String password) {
        if (this.userService.authenticateAdmin(username, password)) {
            this.isLoggedIn = true;
            return "Logged in successfully as " + username + ". Type 'help' to see available commands.";
        }
        return "Invalid credentials. Please try again.";
    }

    @ShellMethod(value = "View logs", key = "view-logs")
    public String viewLogs() {
        if (!this.isLoggedIn) return "Please login first.";
        return this.logService.getFormattedLogs();
    }

    @ShellMethod(value = "Manage users", key = "manage-users")
    public String manageUsers(
            @ShellOption(help = "Action to perform: view, create, delete") String action,
            @ShellOption(help = "Username of the user") String username,
            @ShellOption(defaultValue = "", help = "Password for new user (required for 'create' action)") String password,
            @ShellOption(defaultValue = "false", help = "Set to true for admin privileges") boolean isAdmin
    ) {
        if (!this.isLoggedIn) return "Please login first.";
        return switch (action) {
            case "view" -> {
                User user = this.userService.getUser(username);
                if (user != null) {
                    yield String.format("User found:\n" +
                                    "  ID: %d\n" +
                                    "  Username: %s\n" +
                                    "  Is Admin: %s",
                            user.getUserId(), user.getUsername(),
                            user.isAdmin() ? "Yes" : "No");
                } else {
                    yield "User not found.";
                }
            }
            case "create" -> {
                if (password.isEmpty()) {
                    yield "Password is required for creating a new user.";
                }
                User newUser = this.userService.createUser(username, password, isAdmin);
                yield String.format("""
                                User created:
                                  ID: %d
                                  Username: %s
                                  Is Admin: %s""",
                        newUser.getUserId(), newUser.getUsername(),
                        newUser.isAdmin() ? "Yes" : "No");
            }
            case "delete" -> {
                this.userService.deleteUser(username);
                yield "User deleted: " + username;
            }
            default -> "Invalid action. Use 'view', 'create', or 'delete'.";
        };
    }

    @ShellMethod(value = "Delete temperature data", key = "delete-temp")
    public String deleteTemperatureData(
            @ShellOption(help = "ID of the temperature measurement to delete") int measurementId) {
        if (!this.isLoggedIn) return "Please login first.";
        this.temperatureService.deleteTemperatureMeasurement(measurementId);
        return "Temperature data deleted.";
    }

    @ShellMethod(value = "Manage sensor data", key = "manage-sensor")
    public String manageSensorData(
            @ShellOption(help = "Action to perform: view or delete") String action,
            @ShellOption(help = "ID of the sensor") int sensorId) {
        if (!this.isLoggedIn) return "Please login first.";
        return switch (action) {
            case "view" -> {
                Sensor sensor = this.sensorService.getSensor(sensorId);
                if (sensor != null) {
                    yield String.format("""
                                    Sensor found:
                                      ID: %d
                                      Manufacturer: %s
                                      Model: %s
                                      Location: %s
                                      Max Temperature: %.2f""",
                            sensor.getSensorId(), sensor.getManufacturer(),
                            sensor.getModel(), sensor.getLocation(),
                            sensor.getMaxTemperature());
                } else {
                    yield "Sensor not found.";
                }
            }
            case "delete" -> {
                this.sensorService.deleteSensor(sensorId);
                yield "Sensor deleted.";
            }
            default -> "Invalid action. Use 'view' or 'delete'.";
        };
    }

    @ShellMethod(value = "Logout", key = "logout")
    public String logout() {
        if (this.isLoggedIn) {
            this.isLoggedIn = false;
            return "Logged out successfully.";
        }
        return "You are not logged in.";
    }

    @ShellMethodAvailability({"view-logs", "manage-users", "delete-temp", "manage-sensor"})
    public Availability adminCommandAvailability() {
        return isLoggedIn
                ? Availability.available()
                : Availability.unavailable("You must be logged in to use this command.");
    }
}

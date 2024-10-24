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
                yield user != null ? user.toString() : "User not found.";
            }
            case "create" -> {
                if (password.isEmpty()) {
                    yield "Password is required for creating a new user.";
                }
                User newUser = this.userService.createUser(username, password, isAdmin);
                yield "User created: " + newUser.toString();
            }
            case "delete" -> {
                this.userService.deleteUser(username);
                yield "User deleted";
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
                yield sensor != null ? "Sensor found: " + sensor : "Sensor not found.";
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

    /* @ShellMethod(value = "Display help information", key = "help")
    public String displayHelp() {
        if (!this.isLoggedIn) {
            return """
                    Available commands:
                    login <username> <password> - Log in as an admin
                    help - Display this help message
                    exit - Exit the application""";
        }
        return """
                Available commands:
                view-logs - View the log table
                manage-users <action> <username> [password] [isAdmin] - Manage users (actions: view, create, update, delete)
                delete-temp <measurementId> - Delete temperature data
                manage-sensor <action> <sensorId> - Manage sensor data (actions: view, delete)
                help - Display this help message
                logout - Log out of the admin account
                exit - Exit the application""";
    } */

    @ShellMethodAvailability({"view-logs", "manage-users", "delete-temp", "manage-sensor"})
    public Availability adminCommandAvailability() {
        return isLoggedIn
                ? Availability.available()
                : Availability.unavailable("You must be logged in to use this command.");
    }
}

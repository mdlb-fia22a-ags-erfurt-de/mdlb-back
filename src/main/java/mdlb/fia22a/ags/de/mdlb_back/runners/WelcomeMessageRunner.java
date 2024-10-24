package mdlb.fia22a.ags.de.mdlb_back.commands;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class WelcomeMessageRunner implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        Thread.sleep(100); // Small delay
        System.out.println("=======================================================");
        System.out.println("Welcome to the Temperature Monitoring System CLI!");
        System.out.println("Type 'help' to see available commands.");
        System.out.println("Please use 'login <username> <password>' to access admin features.");
        System.out.println("=======================================================");
    }
}

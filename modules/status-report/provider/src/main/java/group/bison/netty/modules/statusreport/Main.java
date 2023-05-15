package group.bison.netty.modules.statusreport;

import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "group.bison.netty.modules.statusreport")
public class Main {
    public static void main(String[] args) {
        System.setProperty("spring.application.name", "statusreport");
        SpringApplication.run(Main.class, args);
    }
}

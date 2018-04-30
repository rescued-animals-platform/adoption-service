package ec.animal.adoption;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AnimalAdoptionApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(AnimalAdoptionApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }
}

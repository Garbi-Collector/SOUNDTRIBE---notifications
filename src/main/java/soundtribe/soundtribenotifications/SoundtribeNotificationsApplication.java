package soundtribe.soundtribenotifications;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SoundtribeNotificationsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SoundtribeNotificationsApplication.class, args);
    }

}

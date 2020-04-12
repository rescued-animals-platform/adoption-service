package ec.animal.adoption.configuration;

import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.cloud.storage.contrib.nio.testing.LocalStorageHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Configuration
public class ApplicationConfiguration {

    @Bean
    @Primary
    @Profile("!local & !docker")
    public Storage storage() {
        return StorageOptions.getDefaultInstance().getService();
    }

    @Bean
    @Profile({"local", "docker"})
    public Storage dummyStorage() {
        return LocalStorageHelper.getOptions().getService();
    }
}

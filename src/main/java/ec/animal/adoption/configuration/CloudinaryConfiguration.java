package ec.animal.adoption.configuration;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class CloudinaryConfiguration {

    @Value("${cloudinary.cloud-name}")
    private String cloudName;

    @Value("${cloudinary.api-key}")
    private String apiKey;

    @Value("${cloudinary.api-secret}")
    private String apiSecret;

    @Value("${cloudinary.upload-prefix}")
    private String uploadPrefix;

    @Bean
    @Primary
    public Cloudinary storage() {
        Map<String, String> config = new ConcurrentHashMap<>();
        config.put("cloud_name", cloudName);
        config.put("api_key", apiKey);
        config.put("api_secret", apiSecret);
        Optional.ofNullable(uploadPrefix).ifPresent(uploadPrefix -> config.put("upload_prefix", uploadPrefix));

        return new Cloudinary(config);
    }
}

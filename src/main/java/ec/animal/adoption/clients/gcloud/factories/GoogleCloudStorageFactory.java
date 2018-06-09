package ec.animal.adoption.clients.gcloud.factories;

import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.stereotype.Component;

@Component
public class GoogleCloudStorageFactory {

    public Storage get() {
        return StorageOptions.getDefaultInstance().getService();
    }
}
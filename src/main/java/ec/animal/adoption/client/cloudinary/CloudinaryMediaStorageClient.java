package ec.animal.adoption.client.cloudinary;

import com.cloudinary.Cloudinary;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import ec.animal.adoption.client.MediaStorageClient;
import ec.animal.adoption.domain.exception.ImageStorageException;
import ec.animal.adoption.domain.exception.MediaStorageException;
import ec.animal.adoption.domain.media.ImagePicture;
import ec.animal.adoption.domain.media.LinkPicture;
import ec.animal.adoption.domain.media.MediaLink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class CloudinaryMediaStorageClient implements MediaStorageClient {

    private final static Logger LOGGER = LoggerFactory.getLogger(CloudinaryMediaStorageClient.class);

    private final Cloudinary cloudinary;

    @Autowired
    public CloudinaryMediaStorageClient(final Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    @HystrixCommand(defaultFallback = "circuitBreakerFallback", ignoreExceptions = {MediaStorageException.class})
    public LinkPicture save(final ImagePicture imagePicture) {
        try {
            MediaLink largeMediaLink = storeMedia(imagePicture.getLargeImageContent());
            MediaLink smallMediaLink = storeMedia(imagePicture.getSmallImageContent());

            return new LinkPicture(
                    imagePicture.getName(),
                    imagePicture.getPictureType(),
                    largeMediaLink,
                    smallMediaLink
            );
        } catch (IOException exception) {
            LOGGER.error("An exception occurred when communicating to Cloudinary: {}", exception.getMessage());
            throw new ImageStorageException(exception);
        }
    }

    private MediaLink storeMedia(final byte[] content) throws IOException {
        Map<String, String> options = Map.of("folder", "default-organization");
        String url = cloudinary.uploader().upload(content, options).get("url").toString();
        return new MediaLink(url);
    }

    @SuppressWarnings("PMD.UnusedPrivateMethod")
    private LinkPicture circuitBreakerFallback() {
        throw new MediaStorageException();
    }
}

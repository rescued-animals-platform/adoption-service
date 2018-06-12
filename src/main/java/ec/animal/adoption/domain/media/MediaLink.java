package ec.animal.adoption.domain.media;

public class MediaLink implements Media {

    private final String url;
    private final MediaMetadata mediaMetadata;

    public MediaLink(String url, MediaMetadata mediaMetadata) {
        this.url = url;
        this.mediaMetadata = mediaMetadata;
    }

    public String getUrl() {
        return this.url;
    }

    public MediaMetadata getMediaMetadata() {
        return this.mediaMetadata;
    }
}

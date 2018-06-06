package ec.animal.adoption.domain.picture;

import java.net.URI;

public class Url implements PictureRepresentation {

    private final URI uri;

    public Url(URI uri) {
        this.uri = uri;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Url url = (Url) o;

        return uri != null ? uri.equals(url.uri) : url.uri == null;
    }

    @Override
    public int hashCode() {
        return uri != null ? uri.hashCode() : 0;
    }
}

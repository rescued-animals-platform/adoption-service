package ec.animal.adoption.domain.picture;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = MediaLink.class)
public interface PictureRepresentation {

}

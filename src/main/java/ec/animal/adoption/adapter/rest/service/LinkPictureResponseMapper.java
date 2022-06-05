package ec.animal.adoption.adapter.rest.service;

import ec.animal.adoption.adapter.rest.model.media.LinkPictureResponse;
import ec.animal.adoption.domain.model.media.LinkPicture;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LinkPictureResponseMapper {

    LinkPictureResponseMapper MAPPER = Mappers.getMapper(LinkPictureResponseMapper.class);

    @Mapping(source = "largeImageUrl", target = "largeImageMediaLink.url")
    @Mapping(source = "smallImageUrl", target = "smallImageMediaLink.url")
    LinkPictureResponse toLinkPictureResponse(LinkPicture linkPicture);
}

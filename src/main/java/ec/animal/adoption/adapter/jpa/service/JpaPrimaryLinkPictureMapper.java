package ec.animal.adoption.adapter.jpa.service;

import ec.animal.adoption.adapter.jpa.model.JpaAnimal;
import ec.animal.adoption.adapter.jpa.model.JpaPrimaryLinkPicture;
import ec.animal.adoption.domain.model.media.LinkPicture;
import ec.animal.adoption.domain.model.media.MediaLink;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface JpaPrimaryLinkPictureMapper {

    JpaPrimaryLinkPictureMapper MAPPER = Mappers.getMapper(JpaPrimaryLinkPictureMapper.class);

    @Mapping(source = "id", target = "identifier")
    @Mapping(source = "jpaPrimaryLinkPicture", target = "largeImageMediaLink", qualifiedByName = "toLargeImageMediaLink")
    @Mapping(source = "jpaPrimaryLinkPicture", target = "smallImageMediaLink", qualifiedByName = "toSmallImageMediaLink")
    @Mapping(target = "pictureType", constant = "PRIMARY")
    @Mapping(target = "updateWith", ignore = true)
    LinkPicture toLinkPicture(JpaPrimaryLinkPicture jpaPrimaryLinkPicture);

    @Named("toLargeImageMediaLink")
    default MediaLink toLargeImageMediaLink(JpaPrimaryLinkPicture jpaPrimaryLinkPicture) {
        return new MediaLink(jpaPrimaryLinkPicture.getLargeImagePublicId(), jpaPrimaryLinkPicture.getLargeImageUrl());
    }

    @Named("toSmallImageMediaLink")
    default MediaLink toSmallImageMediaLink(JpaPrimaryLinkPicture jpaPrimaryLinkPicture) {
        return new MediaLink(jpaPrimaryLinkPicture.getSmallImagePublicId(), jpaPrimaryLinkPicture.getSmallImageUrl());
    }

    @Mapping(source = "linkPicture.identifier", target = "id", defaultExpression = "java( UUID.randomUUID() )")
    @Mapping(source = "linkPicture.registrationDate", target = "registrationDate", defaultExpression = "java( LocalDateTime.now() )")
    @Mapping(source = "linkPicture.name", target = "name")
    @Mapping(source = "jpaAnimal", target = "jpaAnimal")
    @Mapping(source = "linkPicture.largeImagePublicId", target = "largeImagePublicId")
    @Mapping(source = "linkPicture.largeImageUrl", target = "largeImageUrl")
    @Mapping(source = "linkPicture.smallImagePublicId", target = "smallImagePublicId")
    @Mapping(source = "linkPicture.smallImageUrl", target = "smallImageUrl")
    JpaPrimaryLinkPicture toJpaPrimaryLinkPicture(LinkPicture linkPicture, JpaAnimal jpaAnimal);
}

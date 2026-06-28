package ru.ural.users.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import ru.ural.users.dto.AvatarDto;
import ru.ural.users.dto.AvatarRequest;
import ru.ural.users.entities.Avatar;
import ru.ural.users.models.AvatarModel;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AvatarMapper {

    @Mapping(target = "id", ignore = true)
    AvatarModel toModel(AvatarRequest avatarRequest);

    AvatarModel toModel(Avatar avatar);

    AvatarDto toDto(AvatarModel avatarModel);

    AvatarModel toModel(AvatarDto avatarDto);

    @Mapping(target = "user", ignore = true)
    Avatar toEntity(AvatarModel avatarModel);

    @Mapping(target = "id", ignore = true)
    void mapModelToEntity(@MappingTarget Avatar avatar, AvatarModel avatarModel);

}

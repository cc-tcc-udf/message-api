package org.campus.connect.message.links;

import org.campus.connect.message.utils.EntityMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LinksMapper extends EntityMapper<LinksDTO, Links> {
}

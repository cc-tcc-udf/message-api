package org.campus.connect.message.message;

import org.campus.connect.message.utils.EntityMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MessageMapper extends EntityMapper<MessageDTO, Message> {
}

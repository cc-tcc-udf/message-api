package org.campus.connect.message.files;


import org.campus.connect.message.utils.EntityMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ArquivoMapper extends EntityMapper<ArquivoDTO, Arquivo> {
}

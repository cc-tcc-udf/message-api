package org.campus.connect.message.message.view;

import org.campus.connect.message.message.mobile.MsgViewDTO;
import org.campus.connect.message.utils.GenericService;

import java.util.List;
import java.util.UUID;

public interface ViewService extends GenericService<ViewDTO> {

  List<ViewDTO> findAll();

  ViewDTO create(ViewDTO linksDTO) throws Exception;

  List<ViewDTO> findByIdUser(UUID id);

  ViewDTO setView(MsgViewDTO vw) throws Exception;

  List<ViewDTO> getByIdMsg(UUID id);
}

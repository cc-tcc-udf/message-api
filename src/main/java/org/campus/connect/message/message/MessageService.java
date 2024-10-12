package org.campus.connect.message.message;

import org.campus.connect.message.utils.GenericService;

import java.util.List;

public interface MessageService extends GenericService<MessageDTO> {

  List<MessageDTO> findAll();

  MessageDTO create(MessageDTO message) throws Exception;

  MessageDTO findMsgById(Long id);
}

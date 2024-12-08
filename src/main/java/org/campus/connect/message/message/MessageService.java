package org.campus.connect.message.message;

import org.campus.connect.message.message.mobile.InfosDTO;
import org.campus.connect.message.utils.GenericService;
import org.campus.connect.message.utils.dtos.PageableDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface MessageService extends GenericService<MessageDTO> {

  List<MessageDTO> findAll();

  MessageDTO create(MessageDTO message) throws Exception;

  MessageDTO findMsgById(UUID id);

  MessageDTO send(final MessageDTO msg) throws Exception;

  List<MessageDTO> findAllResp(UUID id);

  List<MessageDTO> findByIdCourseMobile(UUID id);

  List<MessageDTO> findByIdCourse(UUID id);

  List<MessageDTO> findByListIdResp(UUID id);

  InfosDTO findInfosAluno(UUID alunoId);

  List<MessageDTO> findMessagesByFlagAndStudent(UUID alunoId, String flag);

  MessageDTO findMsgByIdMobile(UUID idMsg, UUID alunoId);

  Page<MessageDTO> searchMessages(PageableDTO pageableDTO);
}

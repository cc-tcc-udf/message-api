package org.campus.connect.message.message;

import org.campus.connect.message.constants.Enums.Status;
import org.campus.connect.message.files.FileService;
import org.campus.connect.message.links.LinksService;
import org.campus.connect.message.utils.GenericServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl extends GenericServiceImpl<Message, MessageDTO> implements MessageService {

  private final MessageRepository repository;
  private final MessageMapper mapper;
  private final FileService fileService;
  private final LinksService linksService;

  public MessageServiceImpl(
    final MessageRepository repository,
    final MessageMapper mapper, final FileService fileService, final LinksService linksService) {
    super(repository, mapper);
    this.repository = repository;
    this.mapper = mapper;
    this.fileService = fileService;
    this.linksService = linksService;
  }

  @Override
  public List<MessageDTO> findAll() {
    List<MessageDTO> list = this.mapper.toDto(this.repository.findAll());
    list.forEach(m -> {
      m.setLinks(this.linksService.findByIdMsg(m.getId()));
      m.setAttachments(this.fileService.findAllByIdExt(m.getId()));
    });
    return list;
  }

  @Override
  public MessageDTO create(final MessageDTO message) throws Exception {
    this.save(message);
    message.setStatus(Status.NAO_ENVIADO);
    if (message.getLinks() != null) {
      message.getLinks().forEach(link -> {
        link.setId_msg(message.getId());
        try {
          this.linksService.create(link);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      });
    }
    if (message.getAttachments() != null) {
      message.getAttachments().forEach(attachment -> {
        attachment.setId_ext(message.getId());
        try {
          this.fileService.save(attachment);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      });
    }
    return message;
  }
}

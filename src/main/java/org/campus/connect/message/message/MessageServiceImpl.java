package org.campus.connect.message.message;

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
    MessageDTO msg = this.save(message);
    message.getLinks().forEach(link -> {
      link.setId_msg(msg.getId());
      try {
        this.linksService.create(link);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
    message.getAttachments().forEach(attachment -> {
      attachment.setId_ext(msg.getId());
      try {
        this.fileService.save(attachment);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
    return message;
  }
}

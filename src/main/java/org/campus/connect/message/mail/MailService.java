package org.campus.connect.message.mail;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;

@Service
public class MailService {
  private final JavaMailSender mailSender;
  private final SpringTemplateEngine templateEngine;

  public MailService(
    final JavaMailSender mailSender,
    final SpringTemplateEngine templateEngine
  ) {
    this.mailSender = mailSender;
    this.templateEngine = templateEngine;
  }

  public void sendWelcomeEmail(final MailDTO mail) throws MessagingException {
    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
      StandardCharsets.UTF_8.name());

    Context context = new Context();
    context.setVariable("nome", mail.getName());
    context.setVariable("email", mail.getEmail());
    context.setVariable("pass", mail.getPass());
    context.setVariable("link_de_acesso", mail.getLink());

    String htmlContent = templateEngine.process("mail-create", context);

    // Enviar o e-mail
    helper.setTo(mail.getTo());
    helper.setSubject("Bem-vindo ao Campus Connect!");
    helper.setText(htmlContent, true);

    // Enviar o e-mail
    mailSender.send(message);
  }

  public void sendActiveEmail(final MailDTO mail) throws MessagingException {
    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
      StandardCharsets.UTF_8.name());

    Context context = new Context();
    context.setVariable("nome", mail.getName());
    context.setVariable("link_de_acesso", mail.getLink());

    String htmlContent = templateEngine.process("mail-create", context);

    // Enviar o e-mail
    helper.setTo(mail.getTo());
    helper.setSubject("Bem-vindo ao Campus Connect!");
    helper.setText(htmlContent, true);

    // Enviar o e-mail
    mailSender.send(message);
  }
}

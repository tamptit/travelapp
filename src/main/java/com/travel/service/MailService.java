package com.travel.service;

import com.travel.dto.UserForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.RestController;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@RestController
public class MailService {

    private JavaMailSender javaMailSender;

    @Autowired
    public MailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendEmail( SimpleMailMessage mail) throws MailException {
        javaMailSender.send(mail);
    }


    public void sendEmailWithAttachment(UserForm userForm) throws MailException, MessagingException {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setTo(userForm.getEmail());
        helper.setSubject("Testing Mail API with Attachment");
        helper.setText("Please find the attached document below.");

        javaMailSender.send(mimeMessage);
    }

}

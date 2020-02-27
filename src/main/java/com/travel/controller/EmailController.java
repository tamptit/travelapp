package com.travel.controller;

import com.travel.dto.UserForm;
import com.travel.model.User;
import com.travel.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

@RestController
//@RequestMapping(value = "/api/auth/mail")
//@RequestMapping(value = "/mail")
public class EmailController {

    @Autowired
    private MailService notificationService;

    @Autowired
    private User user;


    @PostMapping(value = "/reset-password")
    public String send(@RequestBody String email  ) {

       // user.setEmailAddress(userForm.getEmail());  //Receiver's email address


        try {
            //notificationService.sendEmail(email);
        } catch (MailException mailException) {
            System.out.println(mailException);
        }
        return "Congratulations! Your mail has been send to the user.";
    }

    @PostMapping(value = "/send-attachment")
    public String sendWithAttachment(@RequestBody UserForm userForm) throws MessagingException {
        user.setEmailAddress(userForm.getEmail()); //Receiver's email address
        try {
            notificationService.sendEmailWithAttachment(userForm);
        } catch (MailException mailException) {
            System.out.println(mailException);
        }
        return "Congratulations! Your mail has been send to the user.";
    }


}

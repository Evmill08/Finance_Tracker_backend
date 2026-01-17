package com.example.finance_tracker.Services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.finance_tracker.Models.EmailResult;

import com.sendgrid.*;

import java.io.IOException;

import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;


@Service
public class EmailService {

    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;

    public void sendVerificationCodeEmail(String toEmail, String subject, String code) throws IOException {
        Email from = new Email("no-reply@financeTracker.com");
        Email to = new Email(toEmail);
        Content content = new Content("text/plain", "Your verification code is: " + code);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);

            
        } catch (Exception e) {

        }

        
    }

    public EmailResult sendVerificationEmail(String email, String code){
        try{
            String subject = "Verify your email";

        } catch (IOException ex) {
            throw ex;
        }
    }

    public EmailResult sendPasswordResetEmail(String email, String code){

    }
}

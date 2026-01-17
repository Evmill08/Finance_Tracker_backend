package com.example.finance_tracker.Services;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.finance_tracker.Models.EmailResult;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;


@Service
public class EmailService {

    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;

    public EmailResult sendVerificationCodeEmail(String toEmail, String subject, String code){
        Email from = new Email("financetracker471@gmail.com");
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
            return new EmailResult(true, response.getBody());

        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    public EmailResult sendVerificationEmail(String email, String code){
        return sendVerificationCodeEmail(email, "Verify your email", code);
    }

    public EmailResult sendPasswordResetEmail(String email, String code){
        return sendVerificationCodeEmail(email, "Reset your password", code);
    }
}

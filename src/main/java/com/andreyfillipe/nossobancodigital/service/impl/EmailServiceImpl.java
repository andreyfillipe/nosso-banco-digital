package com.andreyfillipe.nossobancodigital.service.impl;

import com.andreyfillipe.nossobancodigital.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class EmailServiceImpl implements EmailService {

    @Value("${application.email-default.remetente}")
    private String remetente;

    private JavaMailSender javaMailSender;

    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Async
    public void enviarEmails(String assunto, String mensagem, List<String> ListaEmails) {
        //Enviar email para uma lista de emails
        String[] emails = ListaEmails.toArray(new String[ListaEmails.size()]);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(this.remetente);
        mailMessage.setSubject(assunto);
        mailMessage.setText(mensagem);
        mailMessage.setTo(emails);

        this.javaMailSender.send(mailMessage);
    }

    @Async
    public void enviarEmail(String assunto, String mensagem, String email) {
        //Enviar email para apenas um email
        List<String> emails = Arrays.asList(email);
        this.enviarEmails(assunto, mensagem, emails);
    }
}

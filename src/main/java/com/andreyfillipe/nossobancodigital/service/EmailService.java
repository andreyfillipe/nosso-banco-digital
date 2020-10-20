package com.andreyfillipe.nossobancodigital.service;

import java.util.List;

public interface EmailService {

    void enviarEmails(String assunto, String mensagem, List<String> ListaEmails);

    void enviarEmail(String assunto, String mensagem, String email);
}

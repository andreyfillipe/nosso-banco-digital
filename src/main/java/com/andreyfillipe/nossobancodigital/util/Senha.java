package com.andreyfillipe.nossobancodigital.util;

import org.passay.*;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Component
public class Senha {

    private static int tamanhoMinimo = 8;
    private static int tamanhoMaximo = 8;
    private static int quantidadeLetras = 2;
    private static int quantidadeDigitos = 2;

    public static List<String> validarSenhaForte(String senha) {
        //Regra de tamanho mínimo/máximo
        LengthRule lr = new LengthRule(tamanhoMinimo, tamanhoMaximo);
        //Regra de espaços não permitidos
        WhitespaceRule wr = new WhitespaceRule();
        //Regra de caracter alfabético obrigatório
        AlphabeticalCharacterRule ac = new AlphabeticalCharacterRule (quantidadeLetras);
        //Regra de dígitos obrigatórios
        DigitCharacterRule dc = new DigitCharacterRule(quantidadeDigitos);
        //Regra de caracteres especiais obrigatórios
        SpecialCharacterRule nac = new SpecialCharacterRule ();
        //Regra de caracter maiúsculo obrigatório
        UppercaseCharacterRule uc = new UppercaseCharacterRule();

        List<Rule> ruleList = new ArrayList<>();
        ruleList.add(lr);
        ruleList.add(wr);
        ruleList.add(ac);
        ruleList.add(dc);
        ruleList.add(nac);
        ruleList.add(uc);

        try {
            Properties props = new Properties();
            //Buscar arquivo de mensagens para traduzir mensagens de retorno
            props.load(new FileInputStream("./src/main/resources/messages.properties"));
            MessageResolver messageResolver = new PropertiesMessageResolver(props);
            PasswordValidator passwordValidator = new PasswordValidator(messageResolver, ruleList);
            PasswordData passwordData = new PasswordData(new String(senha));
            //Validar senha
            RuleResult result = passwordValidator.validate(passwordData);
            if (!result.isValid()) {
                return passwordValidator.getMessages(result);
            }
            return null;
        }
        catch (Exception ex) {
            throw new RuntimeException("Erro ao buscar arquivo messages.properties: " + ex.getMessage());
        }
    }

    public static String gerarSenhaHash(String senha) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            return messageDigest.digest(senha.getBytes("UTF-8")).toString();
        }
        catch (Exception ex) {
            throw new RuntimeException("Erro ao gerar senha hash: " + ex.getMessage());
        }
    }
}

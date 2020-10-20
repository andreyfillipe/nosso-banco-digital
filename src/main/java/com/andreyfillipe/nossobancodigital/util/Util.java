package com.andreyfillipe.nossobancodigital.util;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class Util {

    public boolean validarMaiorIdade(int idade, LocalDate DataNascimento) {
        LocalDate data = LocalDate.now().plusYears(-idade);
        if (data.isBefore(DataNascimento)) {
            return false;
        }
        return true;
    }

    public boolean validarCep(String cep) {
        if ((cep.length() == 9) &&
            (cep.substring(0,5).matches("[0-9]+")) &&
            (cep.substring(5,6).matches("-")) &&
            (cep.substring(6,9).matches("[0-9]+"))) {
            return true;
        }
        return false;
    }
}

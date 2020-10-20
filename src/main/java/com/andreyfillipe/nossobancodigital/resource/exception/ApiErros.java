package com.andreyfillipe.nossobancodigital.resource.exception;

import com.andreyfillipe.nossobancodigital.service.exception.EntidadeNaoProcessavelException;
import com.andreyfillipe.nossobancodigital.service.exception.NaoEncontradoException;
import com.andreyfillipe.nossobancodigital.service.exception.RegraNegocioException;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ApiErros {

    private List<String> erros;

    public ApiErros(BindingResult bindingResult) {
        this.erros = new ArrayList<>();
        bindingResult.getAllErrors().forEach(erro -> this.erros.add(erro.getDefaultMessage()));
    }

    public ApiErros(RegraNegocioException ex) {
        this.erros = Arrays.asList(ex.getMessage());
    }

    public ApiErros(NaoEncontradoException ex) {
        this.erros = Arrays.asList(ex.getMessage());
    }

    public ApiErros(EntidadeNaoProcessavelException ex) {
        this.erros = Arrays.asList(ex.getMessage());
    }

    public List<String> getErros() {
        return this.erros;
    }
}

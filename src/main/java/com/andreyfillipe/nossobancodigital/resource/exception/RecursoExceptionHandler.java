package com.andreyfillipe.nossobancodigital.resource.exception;

import com.andreyfillipe.nossobancodigital.service.exception.EntidadeNaoProcessavelException;
import com.andreyfillipe.nossobancodigital.service.exception.NaoEncontradoException;
import com.andreyfillipe.nossobancodigital.service.exception.RegraNegocioException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class RecursoExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErros handleValidationExceptions(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        List<ObjectError> allErrors = bindingResult.getAllErrors();
        return new ApiErros(bindingResult);
    }

    @ExceptionHandler(RegraNegocioException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErros handleRegraNegocioException(RegraNegocioException ex) {
        return new ApiErros(ex);
    }

    @ExceptionHandler(NaoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErros handleNaoEncontradoException(NaoEncontradoException ex) {
        return new ApiErros(ex);
    }

    @ExceptionHandler(EntidadeNaoProcessavelException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ApiErros handleEntidadeNaoProcessavelException(EntidadeNaoProcessavelException ex) {
        return new ApiErros(ex);
    }
}

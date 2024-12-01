package com.example.backendDesafioSenai.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Objects;

@ControllerAdvice
public class TratamentoGlobalDeExcecoes {

    // Tratamento para parâmetros vazios ou inválidos
    @ExceptionHandler(CampoObrigatorioException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(CampoObrigatorioException ex) {
        ErrorResponse erro = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(erro, HttpStatus.BAD_REQUEST);
    }

    //Tratamento para cadastro de registros duplicados
    @ExceptionHandler(RegistroDuplicadoException.class)
    public ResponseEntity<ErrorResponse> handleRegistroDuplicado(RegistroDuplicadoException ex) {
        ErrorResponse erro = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(erro, HttpStatus.CONFLICT);
    }

//    @ExceptionHandler(DataBaseException.class)
//    public ResponseEntity<ErrorResponse> handleDataBaseException(DataBaseException ex) {
//        ErrorResponse erro = new ErrorResponse(
//                HttpStatus.INTERNAL_SERVER_ERROR.value(),
//                ex.getMessage(),
//                LocalDateTime.now()
//        );
//        return new ResponseEntity<>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
//    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        ErrorResponse erro = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(erro, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Erro de validação de campos!",
                LocalDateTime.now()
        );

        // Adicionando os detalhes dos erros de validação
        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errorResponse.getErrors().put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    // Tratamento genérico para qualquer exceção não mapeada
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        String msgErro = Objects.isNull(ex.getMessage()) ?  "Erro interno no servidor" : ex.getMessage();
        ErrorResponse erro = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                msgErro,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

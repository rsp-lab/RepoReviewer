package pl.radek.exception;

import com.fasterxml.jackson.core.JsonParseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.radek.dto.ErrorResponse;

import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler
{
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleValidationErrors(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(" "));
        log.warn(message);
        return new ErrorResponse(String.valueOf(HttpStatus.BAD_REQUEST.value()), message);
    }
    
    @ExceptionHandler(GitHubCloneException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ResponseBody
    public ErrorResponse handleGitHubCloneException(GitHubCloneException ex) {
        log.warn(ex.getMessage());
        return new ErrorResponse(
                String.valueOf(HttpStatus.SERVICE_UNAVAILABLE.value()),
                ex.getMessage()
        );
    }
    
    @ExceptionHandler(AiAnalyzeException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ResponseBody
    public ErrorResponse handleAiException(AiAnalyzeException ex) {
        log.warn(ex.getMessage());
        return new ErrorResponse(
                String.valueOf(HttpStatus.SERVICE_UNAVAILABLE.value()),
                ex.getMessage()
        );
    }
    
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleNotFoundException(NotFoundException ex) {
        log.warn(ex.getMessage());
        return new ErrorResponse(
                String.valueOf(HttpStatus.NOT_FOUND.value()),
                ex.getMessage()
        );
    }
    
    @ExceptionHandler(FileSystemException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResponse handleFileSystemException(FileSystemException ex) {
        log.warn(ex.getMessage());
        return new ErrorResponse(
                String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                ex.getMessage()
        );
    }
    
    @ExceptionHandler(JsonParseException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResponse handleJsonParseException(JsonParseException ex) {
        log.warn(ex.getMessage());
        return new ErrorResponse(
                String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                ex.getMessage()
        );
    }
}

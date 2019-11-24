package de.rrob.spring.error;

import org.hibernate.exception.JDBCConnectionException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // Let Spring handle the exception, we just override the status code
    /*@ExceptionHandler(TeamNotFoundException.class)
    public void springHandleNotFound(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.NOT_FOUND.value());
    }*/
    
    @ExceptionHandler(TeamNotFoundException.class)
    public ResponseEntity<Object> handleTeamIdNotFound(Exception ex,
            WebRequest request) 
    {
        Map<String,Object> body = new HashMap<>();

        body.put("errorId",Long.valueOf(201));
        body.put("state",HttpStatus.SERVICE_UNAVAILABLE.value());
        body.put("message", "internal failure");
        body.put("time", new Date().toString());
        return new ResponseEntity<Object>(body, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(JDBCConnectionException.class)
    public ResponseEntity<Object> dbError(Exception ex,
            WebRequest request) 
    {
        Map<String,Object> body = new HashMap<>();

        body.put("errorId",Long.valueOf(201));
        body.put("state",HttpStatus.SERVICE_UNAVAILABLE.value());
        body.put("message", "internal failure");
        body.put("time", new Date().toString());
        return new ResponseEntity<Object>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    
    // error handle for @Valid
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
	              HttpHeaders headers,
	              HttpStatus status,
	              WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", status.value());

        //Get all errors
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> x.getDefaultMessage())
                .collect(Collectors.toList());

        body.put("errors", errors);

        return new ResponseEntity<>(body, headers, status);

    }
}

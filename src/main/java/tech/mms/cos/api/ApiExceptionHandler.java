package tech.mms.cos.api;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import org.zalando.problem.ThrowableProblem;
import tech.mms.cos.exception.AppResourceNotFoundException;
import tech.mms.cos.exception.AppValidationException;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(AppValidationException.class)
    public ResponseEntity<Problem> handleValidationException(AppValidationException ex, HttpServletRequest request) {
        Problem problem = Problem.builder()
                .withStatus(Status.BAD_REQUEST)
                .withTitle("Validation error")
                .withDetail(ex.getMessage())
                .build();

        return ResponseEntity.status(problem.getStatus().getStatusCode()).body(problem);
    }

    @ExceptionHandler(AppResourceNotFoundException.class)
    public ResponseEntity<Problem> handleResourceNotFoundException(AppResourceNotFoundException ex, HttpServletRequest request) {
        Problem problem = Problem.builder()
                .withStatus(Status.NOT_FOUND)
                .withTitle("Resource not found")
                .withDetail(ex.getMessage())
                .build();

        return ResponseEntity.status(problem.getStatus().getStatusCode()).body(problem);
    }


}

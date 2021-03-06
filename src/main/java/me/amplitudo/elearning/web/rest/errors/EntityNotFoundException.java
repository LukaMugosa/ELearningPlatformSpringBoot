package me.amplitudo.elearning.web.rest.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.zalando.problem.AbstractThrowableProblem;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends AbstractThrowableProblem {

    private String message;

    public EntityNotFoundException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }



}

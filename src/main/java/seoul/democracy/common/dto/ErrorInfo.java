package seoul.democracy.common.dto;

import lombok.Data;
import lombok.Getter;
import org.springframework.web.bind.MethodArgumentNotValidException;
import seoul.democracy.common.exception.BadRequestException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ErrorInfo {

    final private String url;
    final private String msg;
    final private List<FieldError> fieldErrors;

    public ErrorInfo(String url, Exception ex) {
        this.url = url;
        this.msg = ex.getLocalizedMessage();
        if (ex instanceof BadRequestException) {
            this.fieldErrors = Collections.singletonList(new FieldError(((BadRequestException) ex).getField(), ex.getLocalizedMessage()));
        } else if (ex instanceof MethodArgumentNotValidException) {
            this.fieldErrors = ((MethodArgumentNotValidException) ex).getBindingResult().getFieldErrors().stream()
                                   .map(error -> new FieldError(error.getField(), error.getDefaultMessage()))
                                   .collect(Collectors.toList());
        } else this.fieldErrors = null;
    }

    @Data
    public static class FieldError {
        final private String fieldName;
        final private String fieldError;
    }
}

package seoul.democracy.common.exception;

import egovframework.rte.fdl.cmmn.exception.BaseRuntimeException;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class BadRequestException extends BaseRuntimeException {

    final private String field;
    final private String errorCode;

    public BadRequestException(String field, String errorCode, String defaultMessage) {
        super(defaultMessage);
        this.field = field;
        this.errorCode = errorCode;
    }
}

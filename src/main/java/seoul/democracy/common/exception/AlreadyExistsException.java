package seoul.democracy.common.exception;

import egovframework.rte.fdl.cmmn.exception.BaseRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class AlreadyExistsException extends BaseRuntimeException {
    public AlreadyExistsException(String defaultMessage) {
        super(defaultMessage);
    }
}

package seoul.democracy.common.exception;

import egovframework.rte.fdl.cmmn.exception.BaseRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class NotFoundException extends BaseRuntimeException {
    public NotFoundException(String defaultMessage) {
        super(defaultMessage);
    }
}

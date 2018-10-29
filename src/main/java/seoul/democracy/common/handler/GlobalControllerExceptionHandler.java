package seoul.democracy.common.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import seoul.democracy.common.config.ApiPathRequestMatcher;
import seoul.democracy.common.dto.ErrorInfo;

import javax.servlet.http.HttpServletRequest;

/**
 * Exception 전역 처리
 */
@ControllerAdvice
public class GlobalControllerExceptionHandler {

    final private ApiPathRequestMatcher apiPathRequestMatcher;

    @Autowired
    public GlobalControllerExceptionHandler(ApiPathRequestMatcher apiPathRequestMatcher) {
        this.apiPathRequestMatcher = apiPathRequestMatcher;
    }

    @ExceptionHandler(Exception.class)
    public Object handleError(HttpServletRequest request, Exception ex) {

        if (apiPathRequestMatcher.matches(request)) return handleExceptionWithApiPath(request, ex);

        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", ex);
        mav.addObject("url", request.getRequestURL());

        return mav;
    }

    private ResponseEntity<ErrorInfo> handleExceptionWithApiPath(HttpServletRequest req, Exception ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        if (AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class) != null) {
            status = AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class).value();
        } else if (ex instanceof AccessDeniedException) {
            status = HttpStatus.UNAUTHORIZED;
        } else if (ex instanceof MethodArgumentNotValidException) {
            status = HttpStatus.BAD_REQUEST;
        }

        return ResponseEntity.status(status).contentType(MediaType.APPLICATION_JSON_UTF8)
                   .body((new ErrorInfo(req.getRequestURL().toString(), ex)));
    }
}

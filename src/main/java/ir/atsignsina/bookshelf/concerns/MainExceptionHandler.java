package ir.atsignsina.bookshelf.concerns;

import ir.atsignsina.bookshelf.concerns.exception.ResponseException;
import ir.atsignsina.bookshelf.concerns.exception.ResponseExceptionType;
import ir.atsignsina.bookshelf.concerns.utils.NilUtils;
import org.springframework.dao.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.datasource.init.ScriptException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class MainExceptionHandler {
  /**
   * Check ResponseExceptionType and make appropriate http status code.
   *
   * @param responseExceptionType exception type for creating http status based on
   * @return http status integer code
   */
  private static HttpStatus httpStatus(ResponseExceptionType responseExceptionType) {
    switch (responseExceptionType) {
      case DATA_NOT_FOUND:
        return HttpStatus.NO_CONTENT;
      case REQUEST_BODY_ERROR:
      case REQUIREMENTS_DATA_NOT_FOUND:
        return HttpStatus.BAD_REQUEST;
      case UNKNOWN:
      default:
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
  }

  @ExceptionHandler({ResponseException.class, DataAccessException.class})
  public final ResponseEntity<ErrorPrototype> handleException(Exception ex) {
    if (ex instanceof ResponseException) {
      return handleResponseException((ResponseException) ex);
    } else if (ex instanceof DataAccessException) {
      return handleDataAccessException((DataAccessException) ex);
    } else {
      return handleUnknownException(ex);
    }
  }

  /**
   * handle data access exception
   *
   * @param ex exception which raised during querying database
   * @return resposne entity with made errorPrototype body
   */
  private ResponseEntity<ErrorPrototype> handleDataAccessException(DataAccessException ex) {
    if (ex instanceof DataIntegrityViolationException) {
      return makeError("DATABASE_INTEGRITY_ERROR", null, HttpStatus.CONFLICT);
    } else if (ex instanceof NonTransientDataAccessException || ex instanceof ScriptException) {
      return makeError("DATABASE_PERMANENT_ERROR", null, HttpStatus.INTERNAL_SERVER_ERROR);
    } else if (ex instanceof RecoverableDataAccessException
        || ex instanceof TransientDataAccessException) {
      return makeError("DATABASE_TRANSIENT_ERROR", null, HttpStatus.SERVICE_UNAVAILABLE);
    } else {
      return makeError("DATABASE_UNKNOWN_ERROR", null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Handle response excepuon and make appropriate response entity
   *
   * @param ex exception which raised by application
   * @return response entity with made errorPrototype body
   */
  private ResponseEntity<ErrorPrototype> handleUnknownException(Exception ex) {
    return makeError("UNKNOWN", ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  /**
   * Make appropriate ResponseEntity with body of ErrorPrototype
   *
   * @param e exception which thrown by services
   * @return handleResponseException entity with different status based on type of exception and
   *     same body structure.
   */
  private ResponseEntity<ErrorPrototype> handleResponseException(ResponseException e) {
    return makeError(e.getType().name(), e.getMessage(), httpStatus(e.getType()));
  }

  /**
   * create response entity with error prototype body
   *
   * @param type type of error
   * @param message message of error
   * @param status http status of response
   * @return response entity with made error prototype
   */
  private ResponseEntity<ErrorPrototype> makeError(String type, String message, HttpStatus status) {
    return ResponseEntity.status(status)
        .body(
            new ErrorPrototype(
                type, NilUtils.checkNullOrEmpty(message) ? type : message, new Date()));
  }
}

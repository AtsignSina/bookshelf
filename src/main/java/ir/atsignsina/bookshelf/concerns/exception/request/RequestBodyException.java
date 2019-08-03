package ir.atsignsina.bookshelf.concerns.exception.request;

import ir.atsignsina.bookshelf.concerns.exception.ResponseException;
import ir.atsignsina.bookshelf.concerns.exception.ResponseExceptionType;

public class RequestBodyException extends ResponseException {
  public RequestBodyException() {
    this(ResponseExceptionType.REQUEST_BODY_ERROR.name());
  }

  public RequestBodyException(String mcode) {
    this(mcode, null);
  }

  public RequestBodyException(String mcode, Exception e) {
    super(ResponseExceptionType.REQUEST_BODY_ERROR, mcode, e);
  }
}

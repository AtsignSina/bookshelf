package ir.atsignsina.bookshelf.concerns.exception.data;

import ir.atsignsina.bookshelf.concerns.exception.ResponseException;
import ir.atsignsina.bookshelf.concerns.exception.ResponseExceptionType;

public class DataException extends ResponseException {
  DataException(ResponseExceptionType type, String mcode, Exception e) {
    super(type, mcode, e);
  }

  DataException(ResponseExceptionType type, String mcode) {
    super(type, mcode);
  }
}

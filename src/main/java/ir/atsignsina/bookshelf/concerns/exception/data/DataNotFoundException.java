package ir.atsignsina.bookshelf.concerns.exception.data;

import ir.atsignsina.bookshelf.concerns.exception.ResponseExceptionType;

public class DataNotFoundException extends DataException {
  public DataNotFoundException() {
    this(ResponseExceptionType.DATA_NOT_FOUND.name());
  }

  public DataNotFoundException(String mcode) {
    this(mcode, null);
  }

  public DataNotFoundException(String mcode, Exception e) {
    super(ResponseExceptionType.DATA_NOT_FOUND, mcode, e);
  }
}

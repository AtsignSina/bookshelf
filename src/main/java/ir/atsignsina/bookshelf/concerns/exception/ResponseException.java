package ir.atsignsina.bookshelf.concerns.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseException extends RuntimeException {
  private ResponseExceptionType type;
  private Exception realException;

  public ResponseException(String mcode, Exception e) {
    this(ResponseExceptionType.UNKNOWN, mcode, e);
  }

  public ResponseException(ResponseExceptionType type, String mcode, Exception e) {
    super(mcode != null && !mcode.isEmpty() ? mcode : (type != null ? type.name() : "UNKNOWN"));
    setType(type != null ? type : ResponseExceptionType.valueOf(this.getMessage()));
    setRealException(e);
  }

  public ResponseException(ResponseExceptionType type) {
    this(type, "");
  }

  public ResponseException(ResponseExceptionType type, String mcode) {
    this(type, mcode, null);
  }

  public ResponseException(String mcode) {
    this(ResponseExceptionType.UNKNOWN, mcode);
  }

  public ResponseException(Exception e) {
    this(ResponseExceptionType.UNKNOWN, e);
  }

  public ResponseException(ResponseExceptionType type, Exception e) {
    this(type, type.name(), e);
  }

  public ResponseException() {
    this(ResponseExceptionType.UNKNOWN, "");
  }
}

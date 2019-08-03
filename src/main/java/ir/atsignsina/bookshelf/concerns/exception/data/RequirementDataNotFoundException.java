package ir.atsignsina.bookshelf.concerns.exception.data;

import ir.atsignsina.bookshelf.concerns.exception.ResponseExceptionType;

public class RequirementDataNotFoundException extends DataException {
  public RequirementDataNotFoundException() {
    this(ResponseExceptionType.REQUIREMENTS_DATA_NOT_FOUND.name());
  }

  public RequirementDataNotFoundException(String mcode) {
    this(mcode, null);
  }

  public RequirementDataNotFoundException(String mcode, Exception e) {
    super(ResponseExceptionType.REQUIREMENTS_DATA_NOT_FOUND, mcode, e);
  }
}

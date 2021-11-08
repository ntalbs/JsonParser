package ntalbs.json;

import java.util.List;

public class ScanException extends RuntimeException {
  private List<Error> errors;

  public ScanException(List<Error> errors) {
    this.errors = errors;
  }

  public List<Error> errors() {
    return errors;
  }
}

package ntalbs.json;

import java.util.List;

public class JsonException extends RuntimeException {
  private List<Error> errors;

  public JsonException(List<Error> errors) {
    this.errors = errors;
  }

  public List<Error> errors() {
    return errors;
  }
}

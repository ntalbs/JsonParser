package ntalbs.json;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ntalbs.json.Json.Type.ARR;
import static ntalbs.json.Json.Type.LITERAL;

public abstract class Json {
  private static final String INDENT = "  ";

  enum Type {
    OBJ,
    ARR,
    LITERAL
  }

  abstract Type type();

  abstract String str(int level);

  private static String indent(int level) {
    return INDENT.repeat(level);
  }

  static class Obj extends Json {
    List<Member> members = new ArrayList<>();

    @Override
    Type type() {
      return Type.OBJ;
    }

    @Override
    String str(int level) {
      String open = "{\n";
      String body = members.stream()
        .map(m -> m.str(level + 1))
        .collect(Collectors.joining(",\n"));

      String close = "}";
      return indent(level) + open
        + body + (body.isEmpty() ? "": "\n")
        + indent(level) + close;
    }
  }

  static class Arr extends Json {
    List<Json> elements = new ArrayList<>();

    @Override
    Type type() {
      return ARR;
    }

    @Override
    String str(int level) {
      String open = "[\n";
      String body = elements.stream()
        .map(e -> e.str(level + 1))
        .collect(Collectors.joining(",\n"));
      String close = "]";
      return open
        + body + (body.isEmpty() ? "": "\n")
        + indent(level) + close;
    }
  }

  static class Member {
    String key;
    Json val;

    Member(String key, Json val) {
      this.key = key;
      this.val = val;
    }

    public String str(int level) {
      return indent(level) + key + ": " + switch (val.type()) {
        case LITERAL -> val.str(0);
        case OBJ -> val.str(level).trim();
        case ARR -> val.str(level);
      };
    }
  }

  static class Literal extends Json {
    Object val;

    Literal(Object val) {
      this.val = val;
    }

    @Override
    Type type() {
      return LITERAL;
    }

    @Override
    public String str(int level) {
      return indent(level) + String.valueOf(val);
    }
  }

  public String toString() {
    return str(0);
  }
}

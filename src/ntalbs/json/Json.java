package ntalbs.json;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Json {
  static class Obj extends Json {
    List<Member> members = new ArrayList<>();
  }

  static class Arr extends Json {
    List<Json> elements = new ArrayList<>();

    public String toString() {
      return Arrays.toString(elements.toArray());
    }
  }

  static class Member {
    String key;
    Json val;

    Member(String key, Json val) {
      this.key = key;
      this.val = val;
    }

    public String toString() {
      return key + " : " + val;
    }
  }

  static class Literal extends Json {
    Object val;
    Literal(Object val) {
      this.val = val;
    }

    public String toString() {
      return String.valueOf(val);
    }
  }
}

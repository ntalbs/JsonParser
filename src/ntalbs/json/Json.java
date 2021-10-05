package ntalbs.json;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Json {
  static class Obj extends Json {
    Map<String, Json> keyVals = new HashMap<>();
  }

  static class Arr extends Json {
    List<Json> elements = new ArrayList<>();

    public String toString() {
      return Arrays.toString(elements.toArray());
    }
  }

  static class KeyVal {
    String key;
    Json val;

    KeyVal(String key, Json val) {
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

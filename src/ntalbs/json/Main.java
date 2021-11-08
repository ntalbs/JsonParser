package ntalbs.json;

import java.util.List;

public class Main {
  public static void main(String[] args) {
    String input = """
      {
        "a": -10,
        "b": true,
        "c": "hello",
        "d": null,
        "e": [1,2,3,4,5],
        "f": {
          "f1": "hello",
          "f2": "world",
          "f3": [{"a":10}, {"b":20}],
          "f4": {
            "f4-1": true,
            "f4-2": 1234,
            "f4-3": [1, 2, 3],
            "f4-4": {
              "hello": "world"
            }
          }
        },
        "n": [-10, 20.0, 0.003, -0.04],
        "emptyObj": {},
        "emptyArr": []
      }
      """;

    System.out.println(">>>");
    System.out.println(input);
    System.out.println("<<<");

    Scanner scanner = new Scanner(input);
    List<Token> tokens = null;
    try {
      tokens = scanner.scanTokens();

      for (Token t : tokens) {
        System.out.println(t);
      }
    } catch (ScanException x) {
      for (Error e : x.errors()) {
        System.err.println(e);
      }
      System.exit(-1);
    }

    Parser parser = new Parser(tokens);
    Json json = parser.parse();

    System.out.println(json);
  }
}

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
          "arr": [{"a":10}, {"b":20}]
        },
        "n": [-10, 20.0, 0.003, -0.04],
        "emptyObj": {},
        "emptyArr": []
      }
      """;
    Scanner scanner = new Scanner(input);
    List<Token> tokens = scanner.scanTokens();
    for (Token t : tokens) {
      System.out.println(t);
    }

    Parser parser = new Parser(tokens);
    Json json = parser.parse();

    System.out.println(json);
  }
}

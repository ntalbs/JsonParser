package ntalbs.json;

import java.util.ArrayList;
import java.util.List;

import static ntalbs.json.TokenType.COLON;
import static ntalbs.json.TokenType.COMMA;
import static ntalbs.json.TokenType.EOF;
import static ntalbs.json.TokenType.FALSE;
import static ntalbs.json.TokenType.LEFT_BRACE;
import static ntalbs.json.TokenType.LEFT_BRACKET;
import static ntalbs.json.TokenType.NULL;
import static ntalbs.json.TokenType.NUMBER;
import static ntalbs.json.TokenType.RIGHT_BRACE;
import static ntalbs.json.TokenType.RIGHT_BRACKET;
import static ntalbs.json.TokenType.STRING;
import static ntalbs.json.TokenType.TRUE;

public class Parser {
  private final List<Token> tokens;
  private List<Error> errors;
  private int current = 0;

  Parser(List<Token> tokens) {
    this.tokens = tokens;
    this.errors = new ArrayList<>();
  }

  Json parse() {
    Json json = json();
    if (errors.isEmpty()) {
      return json;
    } else {
      throw new JsonException(errors);
    }
  }

  Json json() {
    if (match(LEFT_BRACE)) return obj();
    if (match(LEFT_BRACKET)) return arr();
    if (match(STRING, NUMBER, TRUE, FALSE, NULL)) return literal();
    throw new RuntimeException("Invalid Json");
  }

  Json obj() {
    Json.Obj obj = new Json.Obj();

    if (check(RIGHT_BRACE)) {
      advance();
      return obj;
    }

    Json.Member member = member();
    if (member != null) {
      obj.members.add(member);
    }

    while (match(COMMA)) {
      member = member();
      obj.members.add(member);
    }

    consume(RIGHT_BRACE, "Invalid token: expected '}'");
    return obj;
  }

  Json arr() {
    Json.Arr arr = new Json.Arr();

    if (check(RIGHT_BRACKET)) {
      advance();
      return arr;
    }

    arr.elements.add(json());

    while (match(COMMA)) {
      arr.elements.add(json());
    }

    consume(RIGHT_BRACKET, "Invalue token: expected ']'");
    return arr;
  }

  Json.Member member() {
    String key = (String) string().val;
    if (!match(COLON)) throw new RuntimeException("Invalid token: expected `:` ");
    Json val = json();
    return new Json.Member(key, val);
  }

  private Json.Literal string() {
    if (match(STRING)) return new Json.Literal(previous().literal());
    throw new RuntimeException("Invalid token: expected string");
  }

  private Json.Literal literal() {
    return new Json.Literal(previous().literal());
  }

  private boolean match(TokenType... types) {
    for (TokenType type : types) {
      if (check(type)) {
        advance();
        return true;
      }
    }
    return false;
  }

  private Token advance() {
    if (!isAtEnd()) current++;
    return previous();
  }

  private boolean check(TokenType type) {
    if (isAtEnd()) return false;
    return peek().type() == type;
  }

  private boolean isAtEnd() {
    return peek().type() == EOF;
  }

  private Token peek() {
    return tokens.get(current);
  }

  private Token previous() {
    return tokens.get(current - 1);
  }

  private Token consume(TokenType type, String message) {
    if (check(type)) return advance();
    throw new RuntimeException(message);
  }
}

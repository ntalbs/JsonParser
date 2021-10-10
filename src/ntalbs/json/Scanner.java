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

public class Scanner {
  private final String source;
  private final List<Token> tokens;

  private int start = 0;
  private int current = 0;

  public Scanner(String source) {
    this.source = source;
    this.tokens = new ArrayList<>();
  }

  List<Token> scanTokens() {
    while (!isAtEnd()) {
      start = current;
      scanToken();
    }
    tokens.add(new Token(EOF, "", null));
    return tokens;
  }

  private boolean isAtEnd() {
    return current >= source.length();
  }

  private boolean isDigit(char c) {
    return c >= '0' && c <= '9';
  }

  private boolean isAlpha(char c) {
    return (c >= 'A' && c <= 'Z') || (c >= 'a' && c<= 'z');
  }

  private void scanToken() {
    char c = advance();
    switch (c) {
      case '{' -> addToken(LEFT_BRACE);
      case '}' -> addToken(RIGHT_BRACE);
      case '[' -> addToken(LEFT_BRACKET);
      case ']' -> addToken(RIGHT_BRACKET);
      case ':' -> addToken(COLON);
      case ',' -> addToken(COMMA);
      case '"' -> string();
      case ' ', '\n', '\t', '\r' -> {} // ignore whitespace
      default -> {
        if (isDigit(c) || (c == '-' && isDigit(peekNext()))) {
          number();
        } else if (isAlpha(c)) {
          keyword();
        } else {
          throw new RuntimeException("Unexpected character.");
        }
      }
    }
  }

  private char advance() {
    return source.charAt(current++);
  }

  private char peek() {
    if (isAtEnd()) return '\0';
    return source.charAt(current);
  }

  private char peekNext() {
    if (current + 1 >= source.length()) return '\0';
    return source.charAt(current + 1);
  }

  private void string() {
    while (peek() != '"' && !isAtEnd()) {
      advance();
    }
    if (isAtEnd()) {
      throw new RuntimeException("Unterminated string");
    }
    advance();
    String value = source.substring(start + 1, current - 1);
    addToken(STRING, value);
  }

  private void number() {
    if (peek() == '-') {
      advance();
    }
    while (isDigit(peek())) {
      advance();
    }
    if (peek() == '.' && isDigit(peekNext())) {
      advance();
    }
    while (isDigit(peek())) {
      advance();
    }
    addToken(NUMBER, Double.parseDouble(source.substring(start, current)));
  }

  private void keyword() {
    while (isAlpha(peek())) {
      advance();
    }
    String text = source.substring(start, current);
    switch (text) {
      case "true" -> addToken(TRUE, true);
      case "false" -> addToken(FALSE, false);
      case "null" -> addToken(NULL);
      default -> throw new RuntimeException("Unexpected token: " + text);
    };
  }

  private void addToken(TokenType type) {
    addToken(type, null);
  }

  private void addToken(TokenType type, Object literal) {
    String text = source.substring(start, current);
    tokens.add(new Token(type, text, literal));
  }
}

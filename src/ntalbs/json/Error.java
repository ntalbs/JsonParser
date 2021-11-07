package ntalbs.json;

record Error(String message, int line, int pos) {
  public String toString() {
    return String.format("%d:%d %s", line, pos, message);
  }
}

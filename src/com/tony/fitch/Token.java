package com.tony.fitch;

public class Token {
	final TokenType type;
	final String lexeme;
	final Object literal;
	final int line;
	final int pos;

	Token(TokenType type, String lexeme, Object literal, int line, int pos) {
		this.type = type;
		this.lexeme = lexeme;
		this.literal = literal;
		this.line = line;
		this.pos = pos;
	}

	public String toString() {
		return "[" + type + "] '" + lexeme + "' " + literal +" " + pos + " " + line;
	}
}

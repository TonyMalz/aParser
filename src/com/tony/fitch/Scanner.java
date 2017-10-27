package com.tony.fitch;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tony.fitch.TokenType.*;

public class Scanner {
	private final String source;
	private final List<Token> tokens = new ArrayList<>();
	private int start = 0;
	private int current = 0;
	private int line = 1;

	private static final Map<String, TokenType> keywords;

	static {
		keywords = new HashMap<>();
		keywords.put("and", AND);
		keywords.put("or", OR);
		keywords.put("not", NOT);
		keywords.put("FA", FOR_ALL);
		keywords.put("EX", EXISTS);
		keywords.put("bottom", BOTTOM);
		keywords.put("false", BOTTOM);
		keywords.put("iff", BI_IMPL);
	}

	Scanner(String source) {
		this.source = source;
	}

	List<Token> scanTokens() {
		while (!isAtEnd()) {
			// We are at the beginning of the next lexeme.
			start = current;
			scanToken();
		}

		tokens.add(new Token(EOF, "", null, line));
		return tokens;
	}

	private boolean isAtEnd() {
		return current >= source.length();
	}

	private void scanToken() {
		char c = advance();
		switch (c) {
			case '(' :
				addToken(LEFT_PAREN);
				break;
			case ')' :
				addToken(RIGHT_PAREN);
				break;
			case ',' :
				addToken(COMMA);
				break;
			case '-' :
				addToken(match('>') ? IMPL : MINUS);
				break;
			case '+' :
				addToken(PLUS);
				break;
			case ';' :
				addToken(SEMICOLON);
				break;
			case '*' :
				addToken(STAR);
				break;
			case '!' :
				addToken(NOT);
				break;
			case '∧' :
				addToken(AND);
				break;
			case '∨' :
				addToken(OR);
				break;
			case '¬' :
				addToken(NOT);
				break;
			case '→' :
				addToken(IMPL);
				break;
			case '∀' :
				addToken(FOR_ALL);
				break;
			case '∃' :
				addToken(EXISTS);
				break;
			case '⊥' :
				addToken(BOTTOM);
				break;
			case '↔' :
				addToken(BI_IMPL);
				break;
			case '⇔' :
				addToken(BI_IMPL);
				break;
			case '=' :
				addToken(match('>') ? IMPL : EQUAL);
				break;
			case '<' :
				if (match('=')) {
					if (match('>'))
						addToken(BI_IMPL);
					else
						addToken(LESS_EQUAL);
				} else if (match('-') && match('>') )
					addToken(BI_IMPL);
				else
					addToken(LESS);
				break;
			case '>' :
				addToken(match('=') ? GREATER_EQUAL : GREATER);
				break;
			case '/' :
				if (match('/')) {
					// A comment goes until the end of the line.
					while (peek() != '\n' && !isAtEnd())
						advance();
				} else {
					addToken(SLASH);
				}
				break;

			case ' ' :
			case '\r' :
			case '\t' :
				// Ignore whitespace.
				break;

			case '\n' :
				line++;
				break;

			default :
				if (isDigit(c)) {
					number();
				} else if (isAlpha(c)) {
					identifier();
				} else {
					// TODO: coalesce all invalid characters?
					Fitch.error(line, "Unexpected character.");
				}
				break;
		}
	}
	private boolean isDigit(char c) {
		return c >= '0' && c <= '9';
	}

	private void identifier() {
		while (isAlphaNumeric(peek()))
			advance();

		// See if the identifier is a reserved word.
		String text = source.substring(start, current);

		TokenType type = keywords.get(text);
		if (type == null)
			type = IDENTIFIER;
		addToken(type);
	}

	private boolean isAlpha(char c) {
		return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_';
	}

	private boolean isAlphaNumeric(char c) {
		return isAlpha(c) || isDigit(c);
	}

	private void number() {
		while (isDigit(peek()))
			advance();

		// Look for a fractional part.
		if (peek() == '.' && isDigit(peekNext())) {
			// Consume the "."
			advance();

			while (isDigit(peek()))
				advance();
		}

		addToken(NUMBER, Double.parseDouble(source.substring(start, current)));
	}


	private char advance() {
		return source.charAt(current++);
	}

	private void addToken(TokenType type) {
		addToken(type, null);
	}

	private void addToken(TokenType type, Object literal) {
		String text = source.substring(start, current);
		tokens.add(new Token(type, text, literal, line));
	}

	private boolean match(char expected) {
		if (isAtEnd())
			return false;
		if (source.charAt(current) != expected)
			return false;

		current++;
		return true;
	}

	private char peek() {
		if (isAtEnd())
			return '\0';
		return source.charAt(current);
	}
	private char peekNext() {
		if (current + 1 >= source.length())
			return '\0';
		return source.charAt(current + 1);
	}
}

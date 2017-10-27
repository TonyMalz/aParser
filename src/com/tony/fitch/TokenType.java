package com.tony.fitch;

public enum TokenType {
	// Single-character tokens.
	LEFT_PAREN, RIGHT_PAREN, COMMA, MINUS, PLUS, SEMICOLON, SLASH, STAR,

	// One or two character tokens.
	EQUAL, GREATER, GREATER_EQUAL, LESS, LESS_EQUAL,

	// Literals.
	IDENTIFIER, NUMBER,

	// Logical tokens
	AND, OR, NOT, BOTTOM, IMPL, BI_IMPL, FOR_ALL, EXISTS,

	EOF
}

package com.tony.fitch;

import java.util.ArrayList;
import java.util.List;
import static com.tony.fitch.TokenType.*;

public class Parser {
    private static class ParseError extends RuntimeException {}
    private final List<Token> tokens;
    private int current = 0;

    Parser(List<Token> tokens){
        this.tokens = tokens;
    }

    boolean match(TokenType... types){
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    Token advance(){
        if (!isAtEnd()) current++;
        return previous();
    }

    Token peek(){
        return tokens.get(current);
    }

    Token previous() {
        return tokens.get(current-1);
    }

    Token consume(TokenType type, String message) {
        if (check(type)) return advance();
        throw error(peek(), message);
    }

    boolean isAtEnd() {
        return peek().type == EOF;
    }

    boolean check(TokenType tokenType) {
        if (isAtEnd()) return false;
        return peek().type == tokenType;
    }

    // error handling
    private ParseError error(Token token, String message) {
        Fitch.error(token, message);
        return new ParseError();
    }

    private void synchronize() {
        advance();
        while (!isAtEnd()) {
            if (previous().type == SEMICOLON) return;
            advance();
        }
    }

    public Formula parse(){
        try {
            return formula();
        } catch (ParseError error) {
            return null;
        }
    }

    // parse grammar (recursive decent)

    Formula formula() {
        if (match(NOT)) {
            Token operator = previous();
            Formula right = formula();
            return new Formula.Unary(operator, right);
        }

        if (match(LEFT_PAREN)) {
            Formula left = formula();
            if (match(IMPL, BI_IMPL, AND, OR )) {
                Token connective = previous();
                Formula right = formula();
                left = new Formula.Binary(left, connective, right);
            }
            consume(RIGHT_PAREN, "Expected ')' after expression.");
            return left;
        }

        if (match(EXISTS,FOR_ALL)) {
            Token quantifier = previous();
            if(match(IDENTIFIER)) {
                Token variable = previous();
                Formula right = formula();
                return new Formula.Quantified(quantifier, variable, right);
            }
            throw error(peek(),"Expected a variable after quantification.");
        }

        Formula term = term();
        if (match(EQUAL)){
            Token connective = previous();
            Formula right = term();
            return new Formula.Binary(term,connective,right);
        }

        if (match(AND, OR)){
            Token connective = previous();
            Formula right = formula();
            return new Formula.Binary(term, connective, right);
        }
        return term;
    }

    Formula term(){
        if(match(BOTTOM,NUMBER)){
            return new Formula.Constant(previous());
        }
        if(match(IDENTIFIER)){
            Token identifier = previous();
            if (match(LEFT_PAREN)){
                List<Formula> terms = term_list();
                consume(RIGHT_PAREN,"Expected ')' after argument list.");
                return new Formula.Func(identifier,terms);
            }
            return new Formula.Variable(identifier);
        }
        throw error(peek(), "Expected a term.");
    }

    List<Formula> term_list(){
        List<Formula> terms = new ArrayList<>();
        Formula term = term();
        terms.add(term);
        while (match(COMMA)){
            terms.add(term());
        }
        return terms;
    }
}

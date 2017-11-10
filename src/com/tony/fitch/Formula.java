package com.tony.fitch;

import java.util.List;

abstract class Formula {
  interface Visitor<R> {
    R visitUnaryFormula(Unary formula);
    R visitNotFormula(Not formula);
    R visitBinaryFormula(Binary formula);
    R visitImplFormula(Impl formula);
    R visitBIImplFormula(BIImpl formula);
    R visitQuantifiedFormula(Quantified formula);
    R visitEqualityFormula(Equality formula);
    R visitAndFormula(And formula);
    R visitOrFormula(Or formula);
    R visitVariableFormula(Variable formula);
    R visitConstantFormula(Constant formula);
    R visitFuncFormula(Func formula);
  }

  static class Unary extends Formula {
    Unary(Token operator, Formula right) {
      this.operator = operator;
      this.right = right;
    }

    <R> R accept(Visitor<R> visitor) {
      return visitor.visitUnaryFormula(this);
    }

    final Token operator;
    final Formula right;
  }

  static class Not extends Formula {
    Not(Token operator, Formula right) {
      this.operator = operator;
      this.right = right;
    }

    <R> R accept(Visitor<R> visitor) {
      return visitor.visitNotFormula(this);
    }

    final Token operator;
    final Formula right;
  }

  static class Binary extends Formula {
    Binary(Formula left, Token connective, Formula right) {
      this.left = left;
      this.connective = connective;
      this.right = right;
    }

    <R> R accept(Visitor<R> visitor) {
      return visitor.visitBinaryFormula(this);
    }

    final Formula left;
    final Token connective;
    final Formula right;
  }

  static class Impl extends Formula {
    Impl(Formula left, Token connective, Formula right) {
      this.left = left;
      this.connective = connective;
      this.right = right;
    }

    <R> R accept(Visitor<R> visitor) {
      return visitor.visitImplFormula(this);
    }

    final Formula left;
    final Token connective;
    final Formula right;
  }

  static class BIImpl extends Formula {
    BIImpl(Formula left, Token connective, Formula right) {
      this.left = left;
      this.connective = connective;
      this.right = right;
    }

    <R> R accept(Visitor<R> visitor) {
      return visitor.visitBIImplFormula(this);
    }

    final Formula left;
    final Token connective;
    final Formula right;
  }

  static class Quantified extends Formula {
    Quantified(Token quantifier, Token variable, Formula right) {
      this.quantifier = quantifier;
      this.variable = variable;
      this.right = right;
    }

    <R> R accept(Visitor<R> visitor) {
      return visitor.visitQuantifiedFormula(this);
    }

    final Token quantifier;
    final Token variable;
    final Formula right;
  }

  static class Equality extends Formula {
    Equality(Formula left, Token connective, Formula right) {
      this.left = left;
      this.connective = connective;
      this.right = right;
    }

    <R> R accept(Visitor<R> visitor) {
      return visitor.visitEqualityFormula(this);
    }

    final Formula left;
    final Token connective;
    final Formula right;
  }

  static class And extends Formula {
    And(List<Formula> terms, List<Token> connectives) {
      this.terms = terms;
      this.connectives = connectives;
    }

    <R> R accept(Visitor<R> visitor) {
      return visitor.visitAndFormula(this);
    }

    final List<Formula> terms;
    final List<Token> connectives;
  }

  static class Or extends Formula {
    Or(List<Formula> terms, List<Token> connectives) {
      this.terms = terms;
      this.connectives = connectives;
    }

    <R> R accept(Visitor<R> visitor) {
      return visitor.visitOrFormula(this);
    }

    final List<Formula> terms;
    final List<Token> connectives;
  }

  static class Variable extends Formula {
    Variable(Token name) {
      this.name = name;
    }

    <R> R accept(Visitor<R> visitor) {
      return visitor.visitVariableFormula(this);
    }

    final Token name;
  }

  static class Constant extends Formula {
    Constant(Token name) {
      this.name = name;
    }

    <R> R accept(Visitor<R> visitor) {
      return visitor.visitConstantFormula(this);
    }

    final Token name;
  }

  static class Func extends Formula {
    Func(Token name, List<Formula> args) {
      this.name = name;
      this.args = args;
    }

    <R> R accept(Visitor<R> visitor) {
      return visitor.visitFuncFormula(this);
    }

    final Token name;
    final List<Formula> args;
  }

  abstract <R> R accept(Visitor<R> visitor);
}

package com.tony.tool;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

public class GenerateAstFitch {

	public static void main(String[] args) throws IOException {

		String outputDir = "./src/com/tony/fitch";

		defineAst(outputDir, "Formula", Arrays.asList(
				"Unary    : Token operator, Formula right",
				"Not    : Token operator, Formula right",
				"Binary   : Formula left, Token connective, Formula right",
				"Impl   : Formula left, Token connective, Formula right",
				"BIImpl   : Formula left, Token connective, Formula right",
				"Quantified : Token quantifier, Token variable, Formula right",
				"Equality : Formula left, Token connective, Formula right",
				"And : List<Formula> terms, List<Token> connectives",
				"Or  : List<Formula> terms, List<Token> connectives",
				"Variable : Token name",
				"Constant : Token name",
				"Func : Token name, List<Formula> args"
				)
		);
		System.out.println("Done generating Formula classes");


	}
	private static void defineAst(String outputDir, String baseName,
			List<String> types) throws IOException {
		String path = outputDir + "/" + baseName + ".java";
		PrintWriter writer = new PrintWriter(path, "UTF-8");

		writer.println("package com.tony.fitch;");
		writer.println("");
		writer.println("import java.util.List;");
		writer.println("");
		writer.println("abstract class " + baseName + " {");
		defineVisitor(writer, baseName, types);
		// The AST classes.
		for (String type : types) {
			String className = type.split(":")[0].trim();
			String fields = type.split(":")[1].trim();
			defineType(writer, baseName, className, fields);
		}

		// The base accept() method.
		writer.println("");
		writer.println("  abstract <R> R accept(Visitor<R> visitor);");

		writer.println("}");
		writer.close();
	}
	private static void defineVisitor(PrintWriter writer, String baseName,
			List<String> types) {
		writer.println("  interface Visitor<R> {");

		for (String type : types) {
			String typeName = type.split(":")[0].trim();
			writer.println("    R visit" + typeName + baseName + "(" + typeName
					+ " " + baseName.toLowerCase() + ");");
		}

		writer.println("  }");
	}
	private static void defineType(PrintWriter writer, String baseName,
			String className, String fieldList) {
		writer.println("");
		writer.println("  static class " + className + " extends " + baseName
				+ " {");
		// Constructor
		writer.println("    " + className + "(" + fieldList + ") {");
		// Store parameters in fields
		String[] fields = fieldList.split(", ");
		for (String field : fields) {
			String name = field.split(" ")[1];
			writer.println("      this." + name + " = " + name + ";");
		}
		writer.println("    }");

		// Visitor pattern.
		writer.println();
		writer.println("    <R> R accept(Visitor<R> visitor) {");
		writer.println("      return visitor.visit" + className + baseName
				+ "(this);");
		writer.println("    }");

		// Fields
		writer.println();
		for (String field : fields) {
			writer.println("    final " + field + ";");
		}
		writer.println("  }");
	}
}

package clone.detection.main;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;

public class MethodParser extends ASTVisitor {

	CompilationUnit compilationUnit;
	private char[] source;
	ArrayList<String> reservedWord = new ArrayList<String>();
	HashMap<String, Integer> userDefinedNameMap = new HashMap<String, Integer>();

	public MethodParser(CompilationUnit compilationUnit, char[] source) {
		super();
		this.compilationUnit = compilationUnit;
		this.source = source;
		initReservedWordList();
	}

	public boolean visit(MethodDeclaration node) {
		if (node.isConstructor()) {
			return super.visit(node);
		}
		int startLine = compilationUnit.getLineNumber(node.getName().getStartPosition());
		int endLine = compilationUnit.getLineNumber(node.getStartPosition() + node.getLength());
		String methodName = node.getName().toString();

		System.out.println("--");
		System.out.println(node.toString());
		System.out.println("--");

		ArrayList<String> tokenList = new ArrayList<String>();
		tokenList.add(node.getReturnType2().toString());
		tokenList.add(getNormalizedUserDefinedName(methodName));

		return super.visit(node);
	}

	private String getNormalizedUserDefinedName(String token) {
		int value;
		if (userDefinedNameMap.containsKey(token)) {
			value = userDefinedNameMap.get(token);
		} else {
			value = userDefinedNameMap.size();
		}
		return "$" + value;
	}

	private void initReservedWordList() {
		reservedWord.add("assert");
		reservedWord.add("boolean");
		reservedWord.add("break");
		reservedWord.add("byte");
		reservedWord.add("case");
		reservedWord.add("catch");
		reservedWord.add("char");
		reservedWord.add("class");
		reservedWord.add("continue");
		reservedWord.add("default");
		reservedWord.add("do");
		reservedWord.add("double");
		reservedWord.add("else");
		reservedWord.add("enum");
		reservedWord.add("extends");
		reservedWord.add("finally");
		reservedWord.add("float");
		reservedWord.add("for");
		reservedWord.add("goto");
		reservedWord.add("if");
		reservedWord.add("implements");
		reservedWord.add("instanceof");
		reservedWord.add("int");
		reservedWord.add("interface");
		reservedWord.add("long");
		reservedWord.add("new");
		reservedWord.add("return");
		reservedWord.add("short");
		reservedWord.add("super");
		reservedWord.add("switch");
		reservedWord.add("this");
		reservedWord.add("throw");
		reservedWord.add("throws");
		reservedWord.add("try");
		reservedWord.add("void");
		reservedWord.add("while");
		reservedWord.add("true");
		reservedWord.add("false");
		reservedWord.add("null");
	}

}

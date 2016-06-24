package clone.detection.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;

public class MethodParser extends ASTVisitor {

	CompilationUnit compilationUnit;
	String fileName;
	String targetFileName;
	int targetLine;
	ArrayList<String> reservedWord = new ArrayList<String>();
	HashMap<String, Integer> userDefinedNameMap;

	public MethodParser(CompilationUnit compilationUnit, String fileName, String targetFileName, int targetLine) {
		super();
		this.compilationUnit = compilationUnit;
		this.fileName = fileName;
		this.targetFileName = targetFileName;
		this.targetLine = targetLine;
		initReservedWordList();
	}

	public boolean visit(MethodDeclaration node) {
		userDefinedNameMap = new HashMap<String, Integer>();
		if (node.isConstructor()) {
			return super.visit(node);
		}
		int startLine = compilationUnit.getLineNumber(node.getName().getStartPosition());
		int endLine = compilationUnit.getLineNumber(node.getStartPosition() + node.getLength());
		String methodName = node.getName().toString();

		boolean isTargetMethod = false;
		if (fileName.equals(targetFileName) && startLine <= targetLine && targetLine <= endLine) {
			isTargetMethod = true;
		}

		//		System.out.println("--");
		//		System.out.println(node.toString());
		//		System.out.println("--");

		ArrayList<String> tokenList = new ArrayList<String>();
		ArrayList<String> userDefinedNameList = new ArrayList<String>();
		//		tokenList.add(node.getReturnType2().toString());
		//		tokenList.add(getNormalizedUserDefinedName(methodName));

		String methodTokens = node.toString();
		methodTokens = removeUnnecessaryTokens(methodTokens);
		//System.out.println(methodTokens);
		StringTokenizer st = new StringTokenizer(methodTokens, " !#$%&()-=^~|[{]}+:;*,<.>/?\n\r", true);
		//System.out.println(compilationUnit.getRoot().toString());
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (isWhiteSpace(token)) {
				// do nothing
			} else if (isReservedWord(token)) {
				tokenList.add(token);
			} else if (isSymbol(token)) {
				tokenList.add(token);
			} else if (isModifier(token)) {
				// do nothing
			} else {
				userDefinedNameList.add(token);
				String normalizedUserDefinedName = getNormalizedUserDefinedName(token);
				tokenList.add(normalizedUserDefinedName);
			}
		}

		int hash = tokenList.hashCode();
		Method method = new Method(methodTokens, methodName, startLine, endLine, tokenList, userDefinedNameList, hash);
		if (isTargetMethod) {
			CloneDetectionMain.targetMethod = method;
		} else {
			addMethodToMap(hash, method);
		}
		return super.visit(node);
	}

	private synchronized void addMethodToMap(int hash, Method method) {
		if (CloneDetectionMain.map.containsKey(hash)) {
			ArrayList<Method> list = CloneDetectionMain.map.get(hash);
			list.add(method);
			CloneDetectionMain.map.put(hash, list);
		} else {
			ArrayList<Method> list = new ArrayList<Method>();
			list.add(method);
			CloneDetectionMain.map.put(hash, list);
		}
	}

	private String removeUnnecessaryTokens(String methodTokens) {
		//		String lineCommentExpression = "//.*\n";
		//		methodTokens = methodTokens.replaceAll(lineCommentExpression, "");
		//		String blockCommentExpression = "/\\*([^*]|[\\r\\n]|(\\*+([^*/]|[\\r\\n])))*\\*+/";
		//		methodTokens = methodTokens.replaceAll(blockCommentExpression, "");
		String commentExpression = "(/\\*([^*]|[\\r\\n]|(\\*+([^*/]|[\\r\\n])))*\\*+/)|(//.*)";
		methodTokens = methodTokens.replaceAll(commentExpression, "");
		String doubleQuoteStringExpression = "\".*\"";
		methodTokens = methodTokens.replaceAll(doubleQuoteStringExpression, "doubleQuoteString");
		String singleQuoteStringExpression = "\'.*\'";
		methodTokens = methodTokens.replaceAll(singleQuoteStringExpression, "singleQuoteString");
		return methodTokens;
	}

	private String getNormalizedUserDefinedName(String token) {
		int value;
		if (userDefinedNameMap.containsKey(token)) {
			value = userDefinedNameMap.get(token);
		} else {
			value = userDefinedNameMap.size();
			userDefinedNameMap.put(token, value);
		}
		return "$" + value;
	}

	private boolean isReservedWord(String str) {
		if (reservedWord.contains(str)) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isModifier(String str) {
		if (str.equals("public") || str.equals("protected") || str.equals("private") || str.equals("static")
				|| str.equals("final") || str.equals("abstract") || str.equals("native") || str.equals("synchronized")
				|| str.equals("transient") || str.equals("volatile") || str.equals("strictfp") || str.equals("const")) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isWhiteSpace(String str) {
		if (str.equals(" ") || str.equals("\n") || str.equals("\r") || str.equals("\r\n") || str.equals("\t")) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isSymbol(String s) {
		if (s.equals(" ") || s.equals("!") || s.endsWith("#") || s.equals("$") || s.equals("%") || s.equals("&") || s.equals("(") || s.equals(")") || s.equals("-")
				|| s.equals("=") || s.equals("^") || s.equals("~") || s.equals("|") || s.equals("[") || s.equals("]") || s.equals("{") || s.equals("}") || s.equals("+")
				|| s.equals(";") || s.equals("*") || s.equals(":") || s.equals(",") || s.equals("<") || s.equals(".") || s.equals(">") || s.equals("/") || s.equals("?")) {
			return true;
		} else {
			return false;
		}
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

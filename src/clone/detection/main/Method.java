package clone.detection.main;

import java.util.ArrayList;

public class Method {
	String fileName;
	String methodName;
	int startLine;
	int endLine;
	ArrayList<String> tokenList = new ArrayList<String>();
	int hash;

	public Method(String fileName, String methodName, int startLine, int endLine, ArrayList<String> tokenList, int hash) {
		this.fileName = fileName;
		this.methodName = methodName;
		this.startLine = startLine;
		this.endLine = endLine;
		this.tokenList = tokenList;
		this.hash = hash;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public int getStartLine() {
		return startLine;
	}

	public void setStartLine(int startLine) {
		this.startLine = startLine;
	}

	public int getEndLine() {
		return endLine;
	}

	public void setEndLine(int endLine) {
		this.endLine = endLine;
	}

	public ArrayList<String> getTokenList() {
		return tokenList;
	}

	public void setTokenList(ArrayList<String> tokenList) {
		this.tokenList = tokenList;
	}

	public int getHash(){
		return hash;
	}

	public void setHash(int hash){
		this.hash = hash;
	}
}

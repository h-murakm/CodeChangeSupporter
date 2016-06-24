package clone.detection.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

public class Tokenizer implements Runnable {

	String fileName;
	String targetFileName;
	int targetLine;
	public static final char QUOTE = '\'';
	public static final char DOUBLE_QUOTE = '\"';
	final ArrayList<String> reservedWord = new ArrayList<String>();

	Tokenizer(String file, String targetFileName, int targetLine) {
		this.fileName = file;
		this.targetFileName = targetFileName;
		this.targetLine = targetLine;
	}

	@Override
	public void run() {
		char source[] = getFileContents(new File(fileName));
		ASTParser parser = ASTParser.newParser(AST.JLS4);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(source);
		CompilationUnit unit = (CompilationUnit) parser.createAST(new NullProgressMonitor());
		MethodParser mp = new MethodParser(unit, fileName, targetFileName, targetLine);
		//MethodParser2 mp = new MethodParser2(unit, source);
		unit.accept(mp);
	}

	public char[] getFileContents(File file) {
		char contents[] = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			StringBuffer sb = new StringBuffer();
			for (String line = ""; (line = br.readLine()) != null;)
				sb.append((new StringBuilder(String.valueOf(line))).append("\n").toString());

			br.close();
			contents = new char[sb.length()];
			sb.getChars(0, sb.length() - 1, contents, 0);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return contents;
	}

}

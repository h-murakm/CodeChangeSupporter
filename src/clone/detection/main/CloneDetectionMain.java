package clone.detection.main;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CloneDetectionMain {

	static HashMap<Integer, ArrayList<Method>> map;
	static ArrayList<String> fileNameList;

	public static void detectClones(String targetProject, int minimalTokenLength, String fileName, int selectedLine, String resultFile, String workingDirectory) {
		System.out.println(targetProject);
		System.out.println(minimalTokenLength);
		System.out.println(fileName);
		System.out.println(selectedLine);
		System.out.println(resultFile);
		System.out.println(workingDirectory);

		map = new HashMap<Integer, ArrayList<Method>>();
		fileNameList = new ArrayList<String>();

		FileCollector.listPath(new File(targetProject));
		int threadsNum = Runtime.getRuntime().availableProcessors();
		ExecutorService service = Executors.newFixedThreadPool(1);
		for (String file : fileNameList) {
			service.execute(new Tokenizer(file));
		}
	}

}

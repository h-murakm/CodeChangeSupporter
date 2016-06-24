package clone.detection.main;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CloneDetectionMain {

	static HashMap<Integer, ArrayList<Method>> map;
	static ArrayList<String> fileNameList;
	static Method targetMethod;

	public static void detectClones(String targetProject, int minimalTokenLength, String targetFileName, int targetLine, String resultFile, String workingDirectory) {
		System.out.println(targetProject);
		System.out.println(minimalTokenLength);
		System.out.println(targetFileName);
		System.out.println(targetLine);
		System.out.println(resultFile);
		System.out.println(workingDirectory);

		map = new HashMap<Integer, ArrayList<Method>>();
		fileNameList = new ArrayList<String>();

		FileCollector.listPath(new File(targetProject));
		int threadsNum = Runtime.getRuntime().availableProcessors();
		ExecutorService tokenizationService = Executors.newFixedThreadPool(threadsNum);
		for (String file : fileNameList) {
			tokenizationService.execute(new Tokenizer(file, targetFileName, targetLine));
		}
		tokenizationService.shutdown();
        while(!tokenizationService.isTerminated()){};

        int hash = targetMethod.getHash();
        ArrayList<Method> cloneMethods = map.get(hash);
        for(Method method : cloneMethods){
        	System.out.println(method.getTokenList().toString());
        }

	}

}

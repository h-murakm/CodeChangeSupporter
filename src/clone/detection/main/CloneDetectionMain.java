package clone.detection.main;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

public class CloneDetectionMain {

	static HashMap<Integer, ArrayList<Method>> map;
	static ArrayList<String> fileNameList;
	static Method targetMethod;
	public static ArrayList<Method> cloneMethods;

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
		while (!tokenizationService.isTerminated()) {
		}
		;

		int hash = targetMethod.getHash();
		cloneMethods = map.get(hash);

		createCloneView();

	}

	private static void createCloneView() {
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		IWorkbenchPage page = window.getActivePage();
		//IViewPart view = page.findView("cloneView");
		System.out.println("a");
		//page.hideView(view);
		System.out.println("b");
		try {
			IViewPart view = page.showView("cloneView");
		} catch (PartInitException e) {
			e.printStackTrace();
		}
		System.out.println("c");
	}

}

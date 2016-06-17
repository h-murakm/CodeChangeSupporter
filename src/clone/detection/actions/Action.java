package clone.detection.actions;

import java.util.Calendar;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

import clone.detection.main.CloneDetectionMain;

/**
 * Our sample action implements workbench action delegate.
 * The action proxy will be created by the workbench and
 * shown in the UI. When the user tries to use the action,
 * this delegate will be created and execution will be
 * delegated to it.
 * @see IWorkbenchWindowActionDelegate
 */
public class Action implements IWorkbenchWindowActionDelegate {
	/**
	 * The constructor.
	 */
	public Action() {
	}

	/**
	 * The action has been activated. The argument of the
	 * method represents the 'real' action sitting
	 * in the workbench UI.
	 * @see IWorkbenchWindowActionDelegate#run
	 */
	public void run(IAction action) {

		IWorkbench wb = PlatformUI.getWorkbench();
		IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
		IWorkbenchPage page = win.getActivePage();
		IEditorPart editor = page.getActiveEditor();
		ISelectionProvider selectionProvider = editor.getEditorSite().getSelectionProvider();
		ISelection selection = selectionProvider.getSelection();
		String targetProject = getSrcRoot(editor);
		//projectPath = project;
		String fileName = getFileName(editor);
		int selectedLine = getSelectedLine(selection);
		int minimalTokenLength = 30;
		String resultFile = createResultFile(targetProject);
		String workingDirectory = "C:\\CodeChangeSupporter\\tmp";
		CloneDetectionMain.detectClones(targetProject, minimalTokenLength, fileName, selectedLine, resultFile, workingDirectory);
	}

	private String createResultFile(String targetProject) {
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DAY_OF_MONTH);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		int second = c.get(Calendar.SECOND);
		StringBuilder sb = new StringBuilder("C:\\CodeChangeSupporter\\log_");
		sb.append(year);
		sb.append(normalize(month));
		sb.append(normalize(day)).append("_");
		sb.append(normalize(hour));
		sb.append(normalize(minute));
		sb.append(normalize(second)).append(".txt");
		return sb.toString();
	}

	private String normalize(int i) {
		StringBuilder sb = new StringBuilder();
		if (i < 10) {
			sb.append("0");
		}
		sb.append(i);
		return sb.toString();
	}

	private String getSrcRoot(IEditorPart editor) {
		org.eclipse.ui.IEditorInput input = editor.getEditorInput();
		String path = ((FileEditorInput) input).getPath().toOSString();
		int index = path.indexOf("src") + "src".length();
		return path.substring(0, index);
	}

	private int getSelectedLine(ISelection selection) {
		if (selection instanceof ITextSelection) {
			ITextSelection textSelection = (ITextSelection) selection;
			return textSelection.getStartLine() + 1;
		} else {
			return 0;
		}
	}

	private String getFileName(IEditorPart editor) {
		org.eclipse.ui.IEditorInput input = editor.getEditorInput();
		IPath path = ((FileEditorInput) input).getPath();
		return path.toOSString();
	}

	public void selectionChanged(IAction action, ISelection selection) {
	}

	public void dispose() {
	}

	public void init(IWorkbenchWindow window) {
	}
}
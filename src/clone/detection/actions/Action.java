package clone.detection.actions;

import java.io.IOException;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;

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
		String project = getSrcRoot(editor);
		projectPath = project;
		String fileName = getFileName(editor);
		int selectedLine = getSelectedLine(selection);
		String args[] = new String[8];
		args[0] = project;
		args[1] = "C:\\a\\text.txt";
		args[2] = "java";
		args[3] = "30";
		args[4] = "2";
		args[5] = "C:\\a";
		args[6] = fileName;
		args[7] = (new StringBuilder()).append(selectedLine).toString();
		try
		{
			Main.detect(args);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private String getSrcRoot(IEditorPart editor)
    {
        org.eclipse.ui.IEditorInput input = editor.getEditorInput();
        String path = ((FileEditorInput)input).getPath().toOSString();
        int index = path.indexOf("src") + "src".length();
        return path.substring(0, index);
    }

    private int getSelectedLine(ISelection selection)
    {
        if(selection instanceof ITextSelection)
        {
            ITextSelection textSelection = (ITextSelection)selection;
            return textSelection.getStartLine() + 1;
        } else
        {
            return 0;
        }
    }

    private String getFileName(IEditorPart editor)
    {
        org.eclipse.ui.IEditorInput input = editor.getEditorInput();
        IPath path = ((FileEditorInput)input).getPath();
        return path.toOSString();
    }

	/**
	 * Selection in the workbench has been changed. We
	 * can change the state of the 'real' action here
	 * if we want, but this can only happen after
	 * the delegate has been created.
	 * @see IWorkbenchWindowActionDelegate#selectionChanged
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}

	/**
	 * We can use this method to dispose of any system
	 * resources we previously allocated.
	 * @see IWorkbenchWindowActionDelegate#dispose
	 */
	public void dispose() {
	}

	/**
	 * We will cache window object in order to
	 * be able to provide parent shell for the message dialog.
	 * @see IWorkbenchWindowActionDelegate#init
	 */
	public void init(IWorkbenchWindow window) {
	}
}
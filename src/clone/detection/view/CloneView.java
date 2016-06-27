package clone.detection.view;

import java.util.ArrayList;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.part.ViewPart;

import clone.detection.main.CloneDetectionMain;
import clone.detection.main.Method;

public class CloneView extends ViewPart {

	//TableViewer viewer;
	Table table;

	@Override
	public void createPartControl(Composite parent) {
		System.out.println("aaa");
//		System.out.println("parent:"+parent.toString());
//		TableViewer viewer = new TableViewer(parent, SWT.MULTI | SWT.V_SCROLL | SWT.FULL_SELECTION);
//		System.out.println(viewer.toString());
//		viewer.setContentProvider(new ViewContentProvider());
//		//viewer.setLabelProvider(new ViewLabelProvider(null, null));
//		//viewer.setSorter(new NameSorter());
//		createTable(parent, viewer);
	}

	private void createTable(Composite parent, TableViewer viewer) {
		String[] titles = { "a", "b", "c", "d", "e", "f" };
		int[] bounds = { 100, 100, 100, 100, 100, 100 };
		table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		ArrayList<Method> cloneMethods = CloneDetectionMain.cloneMethods;
		for (Method method : cloneMethods) {
			TableViewerColumn column = new TableViewerColumn(viewer, SWT.NONE);
			column.getColumn().setText(method.getFileName());
			column.getColumn().setText(""+method.getStartLine());
			column.getColumn().setText(""+method.getEndLine());
			column.getColumn().setText(method.getMethodName());
			column.getColumn().setText(""+method.getHash());
			column.getColumn().setText(method.getUserDefinedNameList().toString());

		}
	}

	@Override
	public void setFocus() {

	}

}

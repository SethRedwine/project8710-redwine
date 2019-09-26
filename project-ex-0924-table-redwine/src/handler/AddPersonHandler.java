
package handler;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.swt.widgets.Shell;

import model.Person;
import model.PersonModelProvider;
import util.UtilFile;
import view.MyTableViewer;

public class AddPersonHandler {
	@Inject
	private EPartService epartService;
	@Inject
	@Named(IServiceConstants.ACTIVE_SHELL)
	Shell shell;

	@Execute
	public void execute() {
//		MsgUtil.openWarning("Hint", "Class Exercise!!");
//		AddPersonDialog dialog = new AddPersonDialog(shell);
//		dialog.open();
		PersonModelProvider personInstance = PersonModelProvider.INSTANCE;

		List<String> contents = UtilFile.readFile(
				"/Users/SCR/Documents/College/Modern SW Development Methods/workspaceCSCI8710-Redwine/project-ex-0924-table-redwine/input_add.csv");
		List<List<String>> tableContents = UtilFile.convertTableContents(contents);
		List<Person> newPersons = new ArrayList<Person>();
		for (List<String> iList : tableContents) {
			newPersons.add(new Person(iList.get(0), iList.get(1), iList.get(2), iList.get(3)));
		}
		if (!newPersons.isEmpty()) {
			personInstance.addPersons(newPersons);
			MPart findPart = epartService.findPart(MyTableViewer.ID);
			Object findPartObj = findPart.getObject();

			if (findPartObj instanceof MyTableViewer) {
				MyTableViewer v = (MyTableViewer) findPartObj;
				v.refresh();
			}
		}
	}

}

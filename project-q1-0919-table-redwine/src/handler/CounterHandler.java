package handler;

import java.util.HashSet;
import java.util.List;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import model.ModelProvider;
import model.ProgramElement;

public class CounterHandler {

	@Execute
	public void execute(Shell shell) {
		int packs = getPackages();
		int classes = getClasses();
		int meths = getMeths();
		MessageDialog.openInformation(shell, "Package/Class/Method Counter",
				String.format("Info: Found %d packages, %d classes, and %d methods in total", packs, classes, meths));
	}

	private int getPackages() {
		List<ProgramElement> elements = ModelProvider.INSTANCE.getProgramElements();
		HashSet<String> classSet = new HashSet<>();
		for (ProgramElement elem : elements) {
			classSet.add(elem.getPkgName());
		}
		return classSet.size();
	}

	private int getClasses() {
		List<ProgramElement> elements = ModelProvider.INSTANCE.getProgramElements();
		HashSet<String> classSet = new HashSet<>();
		for (ProgramElement elem : elements) {
			classSet.add(elem.getClassName());
		}
		return classSet.size();
	}

	private int getMeths() {
		List<ProgramElement> elements = ModelProvider.INSTANCE.getProgramElements();
		return elements.size();
	}
}

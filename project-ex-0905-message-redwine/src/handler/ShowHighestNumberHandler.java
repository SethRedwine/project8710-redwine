
package handler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

public class ShowHighestNumberHandler {
	@Execute
	public void execute(Shell shell) {
		List<String> lines = readFile(
				"/Users/SCR/Documents/College/Modern SW Development Methods/workspaceCSCI8710-Redwine/project-ex-0905-message-redwine/numbers.csv");
		Integer first = 0, second = 0, third = 0, fourth = 0, fifth = 0;
		for (String line : lines) {
			Integer num = Integer.parseInt(line);
			if (num > first) {
				first = num;
			} else if (num > second) {
				second = num;
			} else if (num > third) {
				third = num;
			} else if (num > fourth) {
				fourth = num;
			} else if (num > fifth) {
				fifth = num;
			}
		}
		MessageDialog.openInformation(shell, "Title",
				String.format("Top five numbers: %d, %d, %d, %d, %d", first, second, third, fourth, fifth));
	}

	private List<String> readFile(String filePath) {
		List<String> contents = new ArrayList<>();
		File file = new File(filePath);
		Scanner scanner = null;
		try {
			scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				contents.add(line);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (scanner != null)
				scanner.close();
		}
		return contents;
	}

}
package handler;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import model.ModelProvider;
import model.ProgramElement;

public class ExportHandler {
	@Execute
	public void execute(Shell shell) {
		int lines = export();
		MessageDialog.openInformation(shell, "Export", String.format("Info: Exported %d lines", lines));
	}

	private int export() {
		List<ProgramElement> elements = ModelProvider.INSTANCE.getProgramElements();
		int lines = elements.size();
		StringBuffer inputBuffer = new StringBuffer();
		inputBuffer.append("Package Name,Class Name,Method Name,Is Return Void, Is Public Modifier\n");
		for (ProgramElement elem : elements) {
			inputBuffer.append(String.format("%s,%s,%s,%s,%s%n", elem.getPkgName(), elem.getClassName(),
					elem.getMethodName(), elem.isReturnVoid(), elem.isPublic()));
		}
		try {
//							System.out.println("[DBG]");
//							System.out.println(inputBuffer.toString());
			File file = new File("output.csv");
			System.out.println(file.getAbsolutePath());
			file.createNewFile();
			FileOutputStream fileOut = new FileOutputStream(file);
			fileOut.write(inputBuffer.toString().getBytes());
			fileOut.close();
//							List<String> contents = new ArrayList<String>();
//							Scanner scanner = null;
//							try {
//								scanner = new Scanner(file);
//								while (scanner.hasNextLine()) {
//									String line = scanner.nextLine();
//									contents.add(line);
//								}
//							} catch (FileNotFoundException err) {
//								err.printStackTrace();
//							} finally {
//								if (scanner != null)
//									scanner.close();
//							}
//							System.out.println(contents);
//							System.out.println("Exported...allegedly");

		} catch (Exception ex) {
			System.out.println("Problem saving file.");
			ex.printStackTrace();
		}

		return lines;
	}
}

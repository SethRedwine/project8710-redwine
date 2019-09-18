package view;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import analysis.ProjectAnalyzer;
import model.ModelProvider;
import model.ProgramElement;

public class Viewer {
	private TableViewer viewer;
	private static final Image CHECKED = getImage("checked.gif");
	private static final Image UNCHECKED = getImage("unchecked.gif");

	/**
	 * Create contents of the view part.
	 */
	@PostConstruct
	public void createControls(Composite parent) {
		GridLayout layout = new GridLayout(2, false);
		parent.setLayout(layout);
		Label searchLabel = new Label(parent, SWT.NONE);
		searchLabel.setText("Search: ");
		final Text searchText = new Text(parent, SWT.BORDER | SWT.SEARCH);
		searchText.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
		createViewer(parent);
		createContextMenu(parent);
	}

	private void createContextMenu(Composite parent) {
		Menu contextMenu = new Menu(viewer.getTable());
		viewer.getTable().setMenu(contextMenu);
		createMenuItems(contextMenu);
	}

	private void createMenuItems(Menu contextMenu) {
		final MenuItem analyzeItem = new MenuItem(contextMenu, SWT.PUSH);
		analyzeItem.setText("Analyze Program");
		analyzeItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ModelProvider.INSTANCE.clearProgramElements();
				try {
					ProjectAnalyzer analyzer = new ProjectAnalyzer();
					analyzer.analyze();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				viewer.setInput(ModelProvider.INSTANCE.getProgramElements());
			}
		});
		final MenuItem exportItem = new MenuItem(contextMenu, SWT.PUSH);
		exportItem.setText("Export");
		exportItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				List<ProgramElement> elements = ModelProvider.INSTANCE.getProgramElements();

				StringBuffer inputBuffer = new StringBuffer();
				inputBuffer.append(
						"Package Name,Class Name,Method Name,Is Return Void,Parameter Size,Starting Position of Method\n");
				for (ProgramElement elem : elements) {
					inputBuffer.append(String.format("%s,%s,%s,%s,%s,%s%n", elem.getPkgName(), elem.getClassName(),
							elem.getMethodName(), elem.isReturnVoid(), elem.getParameterSize(),
							elem.getStartingPositionOfMethod()));
				}
				try {
//					System.out.println("[DBG]");
//					System.out.println(inputBuffer.toString());
					File file = new File("output.csv");
					System.out.println(file.getAbsolutePath());
					file.createNewFile();
					FileOutputStream fileOut = new FileOutputStream(file);
					fileOut.write(inputBuffer.toString().getBytes());
					fileOut.close();
//					List<String> contents = new ArrayList<String>();
//					Scanner scanner = null;
//					try {
//						scanner = new Scanner(file);
//						while (scanner.hasNextLine()) {
//							String line = scanner.nextLine();
//							contents.add(line);
//						}
//					} catch (FileNotFoundException err) {
//						err.printStackTrace();
//					} finally {
//						if (scanner != null)
//							scanner.close();
//					}
//					System.out.println(contents);
//					System.out.println("Exported...allegedly");

				} catch (Exception ex) {
					System.out.println("Problem saving file.");
					ex.printStackTrace();
				}
			}
		});
	}

	private void createViewer(Composite parent) {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		createProgElemColumns(parent, viewer);
		final Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		viewer.setContentProvider(ArrayContentProvider.getInstance());
		// get the content for the viewer, setInput will call getElements in the
		// contentProvider
		viewer.setInput(ModelProvider.INSTANCE.getProgramElements());
		// make the selection available to other views
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
				Object firstElement = selection.getFirstElement();
				System.out.println("Do something with it: " + firstElement);
			}
		});

		// define layout for the viewer
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = 2;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		viewer.getControl().setLayoutData(gridData);
	}

	private void createProgElemColumns(final Composite parent, final TableViewer viewer) {
		String[] titles = { "Package name", "Class name", "Method Name", "isReturnVoid", "Parameter size",
				"Starting Position of Method" };
		int[] bounds = { 100, 100, 100, 100, 100, 100 };

		TableViewerColumn col = createTableViewerColumn(titles[0], bounds[0], 0);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				ProgramElement p = (ProgramElement) element;
				return p.getPkgName();
			}
		});

		col = createTableViewerColumn(titles[1], bounds[1], 1);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				ProgramElement p = (ProgramElement) element;
				return p.getClassName();
			}
		});

		col = createTableViewerColumn(titles[2], bounds[2], 2);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				ProgramElement p = (ProgramElement) element;
				return p.getMethodName();
			}
		});

		col = createTableViewerColumn(titles[3], bounds[3], 3);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return null;
			}

			@Override
			public Image getImage(Object element) {
				if (((ProgramElement) element).isReturnVoid()) {
					return CHECKED;
				} else {
					return UNCHECKED;
				}
			}
		});

		col = createTableViewerColumn(titles[4], bounds[4], 4);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				ProgramElement p = (ProgramElement) element;
				return String.valueOf(p.getParameterSize());
			}
		});

		col = createTableViewerColumn(titles[5], bounds[5], 5);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				ProgramElement p = (ProgramElement) element;
				return String.valueOf(p.getStartingPositionOfMethod());
			}
		});
	}

	private TableViewerColumn createTableViewerColumn(String title, int bound, final int colNumber) {
		final TableViewerColumn viewerColumn = new TableViewerColumn(viewer, SWT.NONE);
		final TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(bound);
		column.setResizable(true);
		column.setMoveable(true);
		return viewerColumn;
	}

	public TableViewer getViewer() {
		return viewer;
	}

	private static Image getImage(String file) {
		Bundle bundle = FrameworkUtil.getBundle(Viewer.class);
		URL url = FileLocator.find(bundle, new Path("icons/" + file), null);
		ImageDescriptor image = ImageDescriptor.createFromURL(url);
		return image.createImage();
	}

	@PreDestroy
	public void dispose() {
	}

	@Focus
	public void setFocus() {
	}
}

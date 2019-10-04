/**
 * @(#) MethodLabelProvider.java
 */
package view.provider;

import org.eclipse.jface.viewers.StyledString;

import model.MethodElement;

/**
 * @since J2SE-1.8
 */
public class LocationLabelProvider extends ProgElemLabelProvider {

	@Override
	public StyledString getStyledText(Object element) {
		if (element instanceof MethodElement) {
			return new StyledString(((MethodElement) element).getLocation());
		}
		return new StyledString(""); // super.getStyledText(element);
	}
}

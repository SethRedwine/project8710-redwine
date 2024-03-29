package model.provider;

import org.eclipse.jface.viewers.StyledString;

import model.progelement.MethodElement;

public class LabelProviderMethodLocation extends LabelProviderProgElem {

	@Override
	public StyledString getStyledText(Object element) {
		if (element instanceof MethodElement) {
			return new StyledString(((MethodElement) element).getLocation().toString());
		}
		return new StyledString(""); // super.getStyledText(element);
	}
}

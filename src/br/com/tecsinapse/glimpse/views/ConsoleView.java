package br.com.tecsinapse.glimpse.views;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.TextConsoleViewer;
import org.eclipse.ui.part.ViewPart;

public class ConsoleView extends ViewPart {

	public static MessageConsole console = new MessageConsole("Glimpse Console", null);
	
	@Override
	public void createPartControl(Composite parent) {
		TextConsoleViewer viewer = new TextConsoleViewer(parent, console);
		viewer.setEditable(false);
	}

	@Override
	public void setFocus() {
	}

}

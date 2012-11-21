package br.com.tecsinapse.glimpse.util;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

public class DisplayUtil {

	public static void asyncExec(Runnable r) {
		Display display = PlatformUI.getWorkbench().getDisplay();
		if (!display.isDisposed()) {
			display.asyncExec(r);
		}
	}
	
}

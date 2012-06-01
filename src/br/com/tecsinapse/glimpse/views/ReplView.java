package br.com.tecsinapse.glimpse.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import br.com.tecsinapse.glimpse.client.DefaultReplManager;
import br.com.tecsinapse.glimpse.client.Repl;
import br.com.tecsinapse.glimpse.client.ReplManager;
import br.com.tecsinapse.glimpse.client.http.HttpConnector;
import br.com.tecsinapse.glimpse.preferences.PreferenceUtils;

public class ReplView extends ViewPart {

	private ReplManager replManager;

	private Repl repl;

	@Override
	public void createPartControl(Composite parent) {
		HttpConnector connector = new HttpConnector(PreferenceUtils.getUrl(),
				PreferenceUtils.getUserName(), PreferenceUtils.getPassword());
		replManager = new DefaultReplManager(connector);

		SashForm split = new SashForm(parent, SWT.VERTICAL);

		final StyledText console = new StyledText(split, SWT.V_SCROLL
				| SWT.WRAP);
		console.setEditable(false);

		final StyledText command = new StyledText(split, SWT.V_SCROLL
				| SWT.WRAP);
		command.addVerifyKeyListener(new VerifyKeyListener() {

			@Override
			public void verifyKey(VerifyEvent e) {
				if (e.stateMask == SWT.CTRL
						&& (e.keyCode == SWT.LF || e.keyCode == SWT.CR)) {
					e.doit = false;

					Display display = PlatformUI.getWorkbench().getDisplay();
					if (!display.isDisposed()) {
						display.asyncExec(new Runnable() {

							@Override
							public void run() {
								if (repl == null) {
									repl = replManager.createRepl();
								}
								String exp = command.getText();
								String result = repl.eval(exp);
								StringBuilder builder = new StringBuilder();
								builder.append("----\n");
								builder.append(exp);
								builder.append("\n");
								builder.append("=> ");
								builder.append(result);
								builder.append("\n");
								console.append(builder.toString());
								command.setText("");
							}
						});
					}
				}
			}
		});
	}

	@Override
	public void setFocus() {
	}

}

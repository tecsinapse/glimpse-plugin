/*
 * Copyright 2012 Tecsinapse
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.com.tecsinapse.glimpse.views;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.graphics.Font;
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

	private StyledText console;

	private StyledText command;

	public void reconnectRepl() {
		repl.close();
		repl = replManager.createRepl();
		resetCommand();
		resetConsole();
	}

	private void resetConsole() {
		console.setText(";; Glimpse Repl\n");
	}

	private void resetCommand() {
		command.setText("");
	}
	
	@Override
	public void createPartControl(Composite parent) {
		HttpConnector connector = new HttpConnector(PreferenceUtils.getUrl(),
				PreferenceUtils.getUserName(), PreferenceUtils.getPassword());
		replManager = new DefaultReplManager(connector);

		SashForm split = new SashForm(parent, SWT.VERTICAL);
		split.setBackground(Display.getDefault().getSystemColor(
				SWT.COLOR_GRAY));

		Font font = JFaceResources.getTextFont();

		console = new StyledText(split, SWT.V_SCROLL
				| SWT.WRAP);
		console.setFont(font);
		console.setEditable(false);
		resetConsole();

		command = new StyledText(split, SWT.V_SCROLL
				| SWT.WRAP);
		command.setFont(font);
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
								console.append(exp);
								String result = repl.eval(exp);
								StringBuilder builder = new StringBuilder();
								builder.append("\n");
								builder.append("=> ");
								builder.append(result);
								builder.append("\n");
								String resultText = builder.toString();
								StyleRange resultStyle = new StyleRange(console
										.getCharCount(), resultText.length(),
										Display.getDefault().getSystemColor(
												SWT.COLOR_DARK_GREEN), null);
								console.append(resultText);
								console.setStyleRange(resultStyle);
								console.setCaretOffset(console.getCharCount());
								console.showSelection();
								resetCommand();
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

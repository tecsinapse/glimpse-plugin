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

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
import org.eclipse.ui.part.ViewPart;

import br.com.tecsinapse.glimpse.client.DefaultReplManager;
import br.com.tecsinapse.glimpse.client.Repl;
import br.com.tecsinapse.glimpse.client.ReplManager;
import br.com.tecsinapse.glimpse.client.http.HttpConnector;
import br.com.tecsinapse.glimpse.preferences.PreferenceUtils;
import br.com.tecsinapse.glimpse.util.DisplayUtil;

public class ReplView extends ViewPart {

	private String url;

	private String username;

	private String password;

	private ReplManager replManager;

	private Repl repl;

	private StyledText console;

	private StyledText command;

	ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, Long.MAX_VALUE,
			TimeUnit.NANOSECONDS, new LinkedBlockingQueue<Runnable>());

	private class ExpressionEvaluator implements Runnable {

		private String expression;

		public ExpressionEvaluator(String expression) {
			this.expression = expression;
		}

		@Override
		public void run() {
			final String result = repl.eval(expression);
			DisplayUtil.asyncExec(new Runnable() {

				@Override
				public void run() {
					console.append(expression);
					StringBuilder builder = new StringBuilder();
					builder.append("\n");
					builder.append("=> ");
					builder.append(result);
					builder.append("\n");
					String resultText = builder.toString();
					StyleRange resultStyle = new StyleRange(console
							.getCharCount(), resultText.length(), Display
							.getDefault().getSystemColor(SWT.COLOR_DARK_GREEN),
							null);
					console.append(resultText);
					console.setStyleRange(resultStyle);
					console.setCaretOffset(console.getCharCount());
					console.showSelection();
					resetCommand();
				}
			});
		}
	}

	public void reconnectRepl() {
		repl.close();
		repl = replManager.createRepl();
		resetCommand();
		resetConsole();
	}

	private void checkPreferencesStillValid() {
		String newUrl = PreferenceUtils.getUrl();
		String newUserName = PreferenceUtils.getUserName();
		String newPassword = PreferenceUtils.getPassword();

		if (!equals(newUrl, url) || !equals(newUserName, username) || !equals(newPassword, password)) {
			url = newUrl;
			username = newUserName;
			password = newPassword;
			HttpConnector connector = new HttpConnector(newUrl, newUserName,
					newPassword);
			replManager = new DefaultReplManager(connector);
			if (repl != null) {
				repl.close();
			}
		}
	}
	
	private boolean equals(Object o1, Object o2) {
		if (o1 == null && o2 == null) return true;
		if (o1 == null) return false;
		return o1.equals(o2);
	}

	private void resetConsole() {
		StringBuilder text = new StringBuilder();
		text.append(";; Glimpse Repl");
		if (url != null) {
			text.append(" at ");
			text.append(url);
		}
		text.append("\n");
		console.setText(text.toString());
	}

	private void resetCommand() {
		command.setText("");
		command.setEnabled(true);
		command.setFocus();
	}

	@Override
	public void createPartControl(Composite parent) {
		SashForm split = new SashForm(parent, SWT.VERTICAL);
		split.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_GRAY));

		Font font = JFaceResources.getTextFont();

		console = new StyledText(split, SWT.V_SCROLL | SWT.WRAP);
		console.setFont(font);
		console.setEditable(false);
		resetConsole();

		command = new StyledText(split, SWT.V_SCROLL | SWT.WRAP);
		command.setFont(font);
		command.addVerifyKeyListener(new VerifyKeyListener() {

			@Override
			public void verifyKey(VerifyEvent e) {
				if (e.stateMask == SWT.CTRL
						&& (e.keyCode == SWT.LF || e.keyCode == SWT.CR)) {
					e.doit = false;

					DisplayUtil.asyncExec(new Runnable() {

						@Override
						public void run() {
							checkPreferencesStillValid();
							if (repl == null) {
								repl = replManager.createRepl();
								resetConsole();
							}
							String exp = command.getText();
							command.setEnabled(false);
							ExpressionEvaluator evaluator = new ExpressionEvaluator(
									exp);
							executor.execute(evaluator);
						}
					});
				}
			}
		});
	}

	@Override
	public void setFocus() {
	}

}

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

import org.eclipse.jface.text.ITextListener;
import org.eclipse.jface.text.TextEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.TextConsoleViewer;
import org.eclipse.ui.part.ViewPart;

public class ConsoleView extends ViewPart {

	public static final String ID = "br.com.tecsinapse.glimpse.views.console";

	private Composite viewComposite;

	private Label titleLabel;

	private TextConsoleViewer textConsoleViewer;

	public void display(MessageConsole console) {
		if (textConsoleViewer != null) {
			textConsoleViewer.getControl().dispose();
		}
		titleLabel.setText(console.getName());
		textConsoleViewer = new TextConsoleViewer(viewComposite, console);
		textConsoleViewer.setEditable(false);
		
		textConsoleViewer.addTextListener(new ITextListener() {
			
			@Override
			public void textChanged(TextEvent event) {
				StyledText textWidget = textConsoleViewer.getTextWidget();
				textWidget.setCaretOffset(textWidget.getCharCount());
				textWidget.showSelection();
			}
		});
		
		GridData viewerData = new GridData();
		viewerData.horizontalAlignment = SWT.FILL;
		viewerData.verticalAlignment = SWT.FILL;
		viewerData.grabExcessHorizontalSpace = true;
		viewerData.grabExcessVerticalSpace = true;
		textConsoleViewer.getControl().setLayoutData(viewerData);
		viewComposite.layout();
	}

	@Override
	public void createPartControl(Composite parent) {
		viewComposite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(1, true);
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		layout.marginTop = 0;
		layout.marginLeft = 0;
		layout.marginHeight = 0;
		layout.marginBottom = 0;
		viewComposite.setLayout(layout);
		titleLabel = new Label(viewComposite, SWT.NONE);
		titleLabel.setText("No script output to show at this time");
		GridData labelData = new GridData();
		labelData.horizontalAlignment = SWT.FILL;
		labelData.grabExcessHorizontalSpace = true;
		titleLabel.setLayoutData(labelData);
		Label separator = new Label(viewComposite, SWT.SEPARATOR
				| SWT.SHADOW_OUT | SWT.HORIZONTAL);
		GridData separatorData = new GridData();
		separatorData.horizontalAlignment = SWT.FILL;
		separatorData.grabExcessHorizontalSpace = true;
		separator.setLayoutData(separatorData);
	}

	@Override
	public void setFocus() {
	}

}

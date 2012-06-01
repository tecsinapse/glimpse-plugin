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

package br.com.tecsinapse.glimpse.launch;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

import br.com.tecsinapse.glimpse.preferences.PreferenceUtils;

public class RunGlimpseLaunchShortcut implements ILaunchShortcut {

	@Override
	public void launch(ISelection selection, String mode) {
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection strSel = (IStructuredSelection) selection;
			List<?> files = strSel.toList();
			if (files.size() == 1) {
				IFile file = (IFile) Platform.getAdapterManager().getAdapter(
						files.get(0), IFile.class);
				try {
					String name = file.getName();
					String script = IOUtils.toString(file.getContents());
					launchJob(name, script);
				} catch (IOException e) {
					throw new IllegalStateException(e);
				} catch (CoreException e) {
					throw new IllegalStateException(e);
				}
			}
		}
	}

	@Override
	public void launch(IEditorPart activeEditor, String mode) {
		if (activeEditor instanceof ITextEditor) {
			ITextEditor textEditor = (ITextEditor) activeEditor;
			IDocumentProvider dp = textEditor.getDocumentProvider();
			IDocument doc = dp.getDocument(textEditor.getEditorInput());
			launchJob(activeEditor.getTitle(), doc.get());
		}
	}

	private void launchJob(String fileName, String script) {
		String consoleTypeStr = PreferenceUtils.getConsoleType();
		ConsoleType consoleType = ConsoleType.valueOf(consoleTypeStr);
		MessageConsole console = consoleType
				.createMessageConsole(generateConsoleName(fileName));
		MessageConsoleStream out = console.newMessageStream();

		String url = PreferenceUtils.getUrl();
		String username = PreferenceUtils.getUserName();
		String password = PreferenceUtils.getPassword();

		if (url != null && username != null && password != null) {
			ScriptJob job = new ScriptJob(generateJobName(fileName), script,
					url, username, password, out);
			job.schedule();
		}
	}

	private String generateJobName(String fileName) {
		return "Glimpse - " + fileName;
	}

	private String generateConsoleName(String fileName) {
		return fileName + " (" + new Date() + ")";
	}

}

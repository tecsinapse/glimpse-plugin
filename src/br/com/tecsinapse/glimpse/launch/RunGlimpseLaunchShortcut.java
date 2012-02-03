package br.com.tecsinapse.glimpse.launch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.IConsoleView;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

import br.com.tecsinapse.glimpse.Activator;
import br.com.tecsinapse.glimpse.preferences.GlimpsePreferenceConstants;

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
		IWorkbenchPage activePage = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();
		ConsolePlugin plugin = ConsolePlugin.getDefault();
		IConsoleManager conMan = plugin.getConsoleManager();
		MessageConsole console = findOrCreateMessageConsole(fileName, conMan);
		MessageConsoleStream out = console.newMessageStream();
		String id = IConsoleConstants.ID_CONSOLE_VIEW;
		try {
			IConsoleView view = (IConsoleView) activePage.showView(id);
			view.display(console);
		} catch (PartInitException e) {
			throw new IllegalStateException("Error showing console view", e);
		}

		IPreferenceStore preferenceStore = Activator.getDefault()
				.getPreferenceStore();
		String url = preferenceStore.getString(GlimpsePreferenceConstants.URL);
		String username = preferenceStore
				.getString(GlimpsePreferenceConstants.USERNAME);
		String password = preferenceStore
				.getString(GlimpsePreferenceConstants.PASSWORD);

		if (url != null && username != null && password != null) {
			ScriptJob job = new ScriptJob(generateConsoleName(fileName),
					script, url, username, password, out);
			job.schedule();
		}
	}

	private MessageConsole findOrCreateMessageConsole(String title,
			IConsoleManager conMan) {
		String name = generateConsoleName(title);
		IConsole[] consoles = conMan.getConsoles();
		for (IConsole iConsole : consoles) {
			if (iConsole.getName().equals(name)
					&& (iConsole instanceof MessageConsole)) {
				MessageConsole console = (MessageConsole) iConsole;
				console.clearConsole();
				return console;
			}
		}
		MessageConsole console = new MessageConsole(name, null);
		conMan.addConsoles(new IConsole[] { console });
		return console;
	}

	private String generateConsoleName(String title) {
		return "Glimpse - " + title;
	}

}

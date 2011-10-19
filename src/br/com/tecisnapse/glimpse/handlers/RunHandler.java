package br.com.tecisnapse.glimpse.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.text.IDocument;
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

import br.com.tecsinapse.dealerprime.web.monitor.script.RemoteScriptRunner;
import br.com.tecsinapse.dealerprime.web.monitor.script.RemoteScriptRunnerFactory;

public class RunHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchPage activePage = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();
		final IEditorPart activeEditor = activePage.getActiveEditor();
		if (activeEditor instanceof ITextEditor) {
			ITextEditor textEditor = (ITextEditor) activeEditor;
			IDocumentProvider dp = textEditor.getDocumentProvider();
			IDocument doc = dp.getDocument(textEditor.getEditorInput());

			RemoteScriptRunner runner = RemoteScriptRunnerFactory
					.create("http://xxxcnn7534.hospedagemdesites.ws:9004", "manutencao",
							"t3c51n4p53");
			String result = runner.run(doc.get());

			ConsolePlugin plugin = ConsolePlugin.getDefault();
			IConsoleManager conMan = plugin.getConsoleManager();
			MessageConsole console = new MessageConsole("Glimpse - " + activeEditor.getTitle(), null);
			conMan.addConsoles(new IConsole[] { console });
			MessageConsoleStream out = console.newMessageStream();
			out.println(result);
			String id = IConsoleConstants.ID_CONSOLE_VIEW;
			try {
				IConsoleView view = (IConsoleView) activePage.showView(id);
				view.display(console);
			} catch (PartInitException e) {
				throw new ExecutionException("Error showing console view", e);
			}

		}
		return null;
	}

}

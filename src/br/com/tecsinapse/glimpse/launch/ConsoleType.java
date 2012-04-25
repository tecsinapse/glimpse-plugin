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

import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.IConsoleView;
import org.eclipse.ui.console.MessageConsole;

import br.com.tecsinapse.glimpse.views.ConsoleView;

public enum ConsoleType {

	ECLIPSE {
		@Override
		public MessageConsole createMessageConsole(String name) {
			MessageConsole console = null;
			ConsolePlugin plugin = ConsolePlugin.getDefault();
			IConsoleManager conMan = plugin.getConsoleManager();
			IConsole[] consoles = conMan.getConsoles();
			for (IConsole iConsole : consoles) {
				if (iConsole.getName().equals(name)
						&& (iConsole instanceof MessageConsole)) {
					console = (MessageConsole) iConsole;
					console.clearConsole();
					break;
				}
			}
			if (console == null) {
				console = new MessageConsole(name, null);
				conMan.addConsoles(new IConsole[] { console });
			}

			IWorkbenchPage activePage = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getActivePage();
			String id = IConsoleConstants.ID_CONSOLE_VIEW;
			try {
				IConsoleView view = (IConsoleView) activePage.showView(id);
				view.display(console);
			} catch (PartInitException e) {
				throw new IllegalStateException("Error showing console view", e);
			}
			return console;
		}
	},
	GLIMPSE {
		@Override
		public MessageConsole createMessageConsole(String name) {
			try {
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
						.getActivePage();
				ConsoleView consoleView = (ConsoleView) page.showView(ConsoleView.ID);
				MessageConsole console = new MessageConsole(name, null);
				consoleView.display(console);
				return console;
			} catch (PartInitException e) {
				throw new IllegalStateException(e);
			}
		}
	};

	public abstract MessageConsole createMessageConsole(String name);

}

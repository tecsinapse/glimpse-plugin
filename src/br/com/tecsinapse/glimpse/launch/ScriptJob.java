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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.console.MessageConsoleStream;

import br.com.tecsinapse.glimpse.client.Monitor;
import br.com.tecsinapse.glimpse.client.ScriptRunner;
import br.com.tecsinapse.glimpse.client.ScriptRunnerFactory;

public class ScriptJob extends Job {

	private MessageConsoleStream out;
	private String script;
	private String url;
	private String username;
	private String password;

	public ScriptJob(String name, String script, String url, String username,
			String password, MessageConsoleStream out) {
		super(name);

		this.script = script;
		this.url = url;
		this.username = username;
		this.password = password;
		this.out = out;
	}

	@Override
	protected IStatus run(final IProgressMonitor monitor) {
		ScriptRunner scriptRunner = ScriptRunnerFactory.create(url, username,
				password);

		scriptRunner.run(script, new Monitor() {

			@Override
			public void worked(int workedSteps) {
				monitor.worked(workedSteps);
			}

			@Override
			public boolean isCanceled() {
				return monitor.isCanceled();
			}

			@Override
			public void println(Object o) {
				out.println(o == null ? "null" : o.toString());
			}

			@Override
			public void close() {
				monitor.done();
			}

			@Override
			public void begin(int steps) {
				monitor.beginTask("Groovy", steps);
			}
		});
		return Status.OK_STATUS;
	}

}

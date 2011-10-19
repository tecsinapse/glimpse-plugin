package br.com.tecsinapse.glimpse.handlers;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.console.MessageConsoleStream;

import br.com.tecsinapse.dealerprime.web.monitor.script.RemoteScriptRunner;
import br.com.tecsinapse.dealerprime.web.monitor.script.RemoteScriptRunnerFactory;
import br.com.tecsinapse.glimpse.PluginConstants;

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
	protected IStatus run(IProgressMonitor monitor) {
		RemoteScriptRunner runner = RemoteScriptRunnerFactory.create(url,
				username, password);
		try {
			String result = runner.run(script);
			out.println(result);
			out.println("---------");
			out.println("COMPLETED");
			return Status.OK_STATUS;
		} catch (RuntimeException e) {
			out.println("-----");
			out.println("ERROR");
			return new Status(Status.ERROR, PluginConstants.ID,
					"Error executing script", e);
		}
	}

}

package br.com.tecsinapse.dealerprime.web.monitor.script;

import java.io.IOException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.methods.PostMethod;
import org.springframework.remoting.httpinvoker.CommonsHttpInvokerRequestExecutor;
import org.springframework.remoting.httpinvoker.HttpInvokerClientConfiguration;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

public class RemoteScriptRunnerFactory {

	public static RemoteScriptRunner create(final String url,
			final String username, final String password) {
		HttpInvokerProxyFactoryBean factoryBean = new HttpInvokerProxyFactoryBean();
		factoryBean.setServiceInterface(RemoteScriptRunner.class);
		factoryBean.setServiceUrl(url);
		factoryBean
				.setHttpInvokerRequestExecutor(new CommonsHttpInvokerRequestExecutor() {

					protected PostMethod createPostMethod(
							HttpInvokerClientConfiguration config)
							throws IOException {
						PostMethod postMethod = super.createPostMethod(config);
						String base64 = username + ":" + password;
						postMethod.setRequestHeader(
								"Authorization",
								"Basic "
										+ new String(Base64.encodeBase64(base64
												.getBytes())));
						return postMethod;
					};

				});
		factoryBean.afterPropertiesSet();
		return (RemoteScriptRunner) factoryBean.getObject();
	}

}

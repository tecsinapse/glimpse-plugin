package br.com.tecsinapse.glimpse.preferences;

import org.eclipse.jface.preference.IPreferenceStore;

import br.com.tecsinapse.glimpse.Activator;

public class PreferenceUtils {

	private static IPreferenceStore getPreferenceStore() {
		return Activator.getDefault().getPreferenceStore();
	}

	public static String getConsoleType() {
		return getPreferenceStore().getString(
				GlimpsePreferenceConstants.CONSOLE_TYPE);
	}

	public static String getUrl() {
		return getPreferenceStore().getString(GlimpsePreferenceConstants.URL);
	}

	public static String getUserName() {
		return getPreferenceStore().getString(
				GlimpsePreferenceConstants.USERNAME);
	}

	public static String getPassword() {
		return getPreferenceStore().getString(
				GlimpsePreferenceConstants.PASSWORD);
	}

}

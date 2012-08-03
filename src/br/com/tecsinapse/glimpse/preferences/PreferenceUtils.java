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

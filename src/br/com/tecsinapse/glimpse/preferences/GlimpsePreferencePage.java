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

import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import br.com.tecsinapse.glimpse.Activator;

public class GlimpsePreferencePage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {

	public GlimpsePreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("Glimpse Preferences");
	}

	@Override
	public void init(IWorkbench workbench) {
	}

	@Override
	protected void createFieldEditors() {
		addField(new StringFieldEditor(GlimpsePreferenceConstants.URL, "Url",
				getFieldEditorParent()));
		addField(new StringFieldEditor(GlimpsePreferenceConstants.USERNAME,
				"User name", getFieldEditorParent()));
		addField(new StringFieldEditor(GlimpsePreferenceConstants.PASSWORD,
				"Password", getFieldEditorParent()));
		addField(new ComboFieldEditor(GlimpsePreferenceConstants.CONSOLE_TYPE,
				"Console type", new String[][] { { "Glimpse", "GLIMPSE" },
						{ "Eclipse", "ECLIPSE" } }, getFieldEditorParent()));
	}
}

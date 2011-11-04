package br.com.tecsinapse.glimpse.preferences;

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
	}

}

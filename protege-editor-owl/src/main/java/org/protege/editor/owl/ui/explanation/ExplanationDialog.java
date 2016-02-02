package org.protege.editor.owl.ui.explanation;

import org.protege.editor.core.prefs.Preferences;
import org.protege.editor.core.prefs.PreferencesManager;
import org.protege.editor.owl.model.inference.ReasonerPreferences;
import org.semanticweb.owlapi.model.OWLAxiom;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

public class ExplanationDialog extends JPanel {
	private static final long serialVersionUID = -4476549896762790748L;
	public static final String PREFERENCES_SET_KEY = "EXPLANATION_PREFS_SET";
    public static final String DEFAULT_EXPLANATION_ID = "PREFERRED_PLUGIN_ID";
    
	private JPanel explanationContainer;
	private ExplanationResult explanation;
	private OWLAxiom axiom;
	
	public ExplanationDialog(ExplanationManager explanationManager, OWLAxiom axiom) {
		this.axiom = axiom;
		
		setLayout(new BorderLayout());
		Collection<ExplanationService> teachers = explanationManager.getTeachers(axiom);
		if (teachers.size() == 1) {
			explanation = teachers.iterator().next().explain(axiom);
		}
		else {
			JComboBox selector = createComboBox(teachers);
			add(selector, BorderLayout.NORTH);
		}
		explanationContainer = new JPanel();
		explanationContainer.setLayout(new BoxLayout(explanationContainer, BoxLayout.Y_AXIS));
		explanationContainer.add(explanation);
		add(explanationContainer, BorderLayout.CENTER);
	}
	
	private JComboBox createComboBox(Collection<ExplanationService> teachers) {
		ExplanationService[] teacherArray = teachers.toArray(new ExplanationService[teachers.size()]);
		final JComboBox selector = new JComboBox(teacherArray);
		ExplanationService selected = teacherArray[0];
		String id = getDefaultPluginId();
		if (id != null) {
			for (ExplanationService t : teachers) {
				if (id.equals(t.getPluginId())) {
					selected = t;
				}
			}
		}
		selector.setSelectedItem(selected);
		explanation = selected.explain(axiom);
		selector.addActionListener(e -> {
            ExplanationService t = (ExplanationService) selector.getSelectedItem();
            setDefaultPluginId(t.getPluginId());
            explanationContainer.removeAll();
            if (explanation != null) {
                explanation.dispose();
            }
            explanation = t.explain(axiom);
            explanationContainer.add(explanation);
revalidate();
        });
		return selector;
	}

	
	public String getDefaultPluginId() {
        PreferencesManager prefMan = PreferencesManager.getInstance();
        Preferences prefs = prefMan.getPreferencesForSet(PREFERENCES_SET_KEY, ReasonerPreferences.class);
        return prefs.getString(DEFAULT_EXPLANATION_ID, null);
	}
	
	public void setDefaultPluginId(String id) {
        PreferencesManager prefMan = PreferencesManager.getInstance();
        Preferences prefs = prefMan.getPreferencesForSet(PREFERENCES_SET_KEY, ReasonerPreferences.class);
        prefs.putString(DEFAULT_EXPLANATION_ID, id);
	}
	
	public void dispose() {
		if (explanation != null) {
			explanation.dispose();
		}
	}
}

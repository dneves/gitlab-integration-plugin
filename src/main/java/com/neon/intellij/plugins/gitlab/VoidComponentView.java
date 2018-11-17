package com.neon.intellij.plugins.gitlab;

import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.Project;
import com.neon.intellij.plugins.gitlab.view.configurable.GitLabConfigurable;
import info.clearthought.layout.TableLayout;
import info.clearthought.layout.TableLayoutConstraints;

import javax.swing.*;

public class VoidComponentView extends JPanel {

    public VoidComponentView( final Project project ) {
        JButton buttonPreferences = new JButton( "Open Plugin Configuration" );
        buttonPreferences.addActionListener(e -> ShowSettingsUtil.getInstance().showSettingsDialog( project, GitLabConfigurable.class ));

        TableLayout layout = new TableLayout( new double[][] {
                { TableLayout.FILL, TableLayout.MINIMUM, TableLayout.FILL },
                { TableLayout.FILL, TableLayout.PREFERRED, TableLayout.FILL }
        } );
        this.setLayout( layout );
        this.add( buttonPreferences, new TableLayoutConstraints( 1, 1, 1, 1 ) );
    }

}

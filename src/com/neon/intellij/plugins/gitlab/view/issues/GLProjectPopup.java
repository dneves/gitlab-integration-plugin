package com.neon.intellij.plugins.gitlab.view.issues;

import com.intellij.icons.AllIcons;
import com.neon.intellij.plugins.gitlab.controller.GLIController;
import org.gitlab.api.models.GitlabIssue;
import org.gitlab.api.models.GitlabProject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GLProjectPopup extends JPopupMenu {

    public GLProjectPopup( final GLIController controller, final GitlabProject project ) {

//        JMenuItem refresh = new JMenuItem( "Refresh" );
//        refresh.addActionListener( new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                // TODO : refresh issues for selected project
//            }
//        });
//        this.add( refresh );

        JMenuItem create = new JMenuItem( "New Issue", AllIcons.Actions.NewFolder );
        create.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GitlabIssue issue = new GitlabIssue();
                issue.setProjectId( project.getId() );
                controller.openEditor( issue );
            }
        });
        this.add( create );
    }

}

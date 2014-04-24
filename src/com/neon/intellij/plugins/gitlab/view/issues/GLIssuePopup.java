package com.neon.intellij.plugins.gitlab.view.issues;

import com.intellij.openapi.diagnostic.Logger;
import com.neon.intellij.plugins.gitlab.controller.GLIController;
import com.neon.intellij.plugins.gitlab.model.gitlab.GLIssueState;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import org.gitlab.api.models.GitlabIssue;

public class GLIssuePopup extends JPopupMenu {

    private static final Logger LOG = Logger.getInstance("gitlab");

    public GLIssuePopup( final GLIController controller, final GitlabIssue issue ) {
        JMenuItem editItem = new JMenuItem( "Edit" );
        editItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.openEditor(issue);
            }
        });
        this.add(editItem);

        JMenuItem delete = new JMenuItem( "Delete" );
        delete.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    GitlabIssue deleteIssue = controller.deleteIssue(issue);
                    issue.setState( deleteIssue.getState() );
                } catch (IOException e1) {
                    LOG.error( e1 );
                }
            }
        });
        this.add( delete );

        JMenu statesMenu = new JMenu( "State" );

        GLIssueState issueState = GLIssueState.fromValue(issue.getState());

        if ( GLIssueState.CLOSED.equals( issueState ) ) {
            JRadioButtonMenuItem reopen = new JRadioButtonMenuItem(GLIssueState.REOPEN.getLabel());
            reopen.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        GitlabIssue savedIssue = controller.changeState(issue, GLIssueState.REOPEN);
                        issue.setState( savedIssue.getState() );
                    } catch (IOException e1) {
                        LOG.error(e1);
                    }
                }
            });
            statesMenu.add(reopen);
        } else {
            JRadioButtonMenuItem closed = new JRadioButtonMenuItem(GLIssueState.CLOSED.getLabel());
            closed.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        GitlabIssue savedIssue = controller.changeState(issue, GLIssueState.CLOSED);
                        issue.setState( savedIssue.getState() );
                    } catch (IOException e1) {
                        LOG.error(e1);
                    }
                }
            });
            statesMenu.add(closed);
        }

        this.addSeparator();
        this.add( statesMenu );
    }

}

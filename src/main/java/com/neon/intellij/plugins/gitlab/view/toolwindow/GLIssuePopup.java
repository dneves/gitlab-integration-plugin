package com.neon.intellij.plugins.gitlab.view.toolwindow;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.diagnostic.Logger;
import com.neon.intellij.plugins.gitlab.controller.GLIController;
import com.neon.intellij.plugins.gitlab.model.gitlab.GIPIssue;
import com.neon.intellij.plugins.gitlab.model.intellij.GLIssueNode;

import javax.swing.*;
import java.io.IOException;

public class GLIssuePopup extends JPopupMenu {

    private static final Logger LOG = Logger.getInstance("gitlab");

    public GLIssuePopup( final GLIController controller, final GLIssueNode node ) {
        final GIPIssue issue = node.getUserObject();

        JMenuItem editItem = new JMenuItem( "Edit", AllIcons.Actions.Edit );
        editItem.addActionListener(e -> controller.openEditor(issue));
        this.add(editItem);

        JMenuItem delete = new JMenuItem( "Delete", AllIcons.Actions.Delete );
        delete.addActionListener(e -> {
            try {
                GIPIssue deleteIssue = controller.deleteIssue(issue);
                issue.state = deleteIssue.state;
            } catch (IOException e1) {
                LOG.error( e1 );
            }
        });
        this.add( delete );

        JMenu statesMenu = new JMenu( "Change State To ..." );

        if ( "closed".equalsIgnoreCase( issue.state ) ) {
            JRadioButtonMenuItem reopen = new JRadioButtonMenuItem( "Re-Open", AllIcons.Actions.Resume );
            reopen.addActionListener(e -> {
                try {
                    GIPIssue savedIssue = controller.changeState(issue, "reopen");
                    issue.state = savedIssue.state;
                } catch (IOException e1) {
                    LOG.error(e1);
                }
            });
            statesMenu.add(reopen);
        } else {
            JRadioButtonMenuItem closed = new JRadioButtonMenuItem( "Closed", AllIcons.Actions.Close );
            closed.addActionListener(e -> {
                try {
                    GIPIssue savedIssue = controller.changeState(issue, "closed");
                    issue.state = savedIssue.state;
                } catch (IOException e1) {
                    LOG.error(e1);
                }
            });
            statesMenu.add(closed);
        }

        this.addSeparator();
        this.add( statesMenu );

//        TODO: action to open issue in browser (web_url)
    }

}

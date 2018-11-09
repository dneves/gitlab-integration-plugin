package com.neon.intellij.plugins.gitlab.view.toolwindow;

import com.intellij.icons.AllIcons;
import com.neon.intellij.plugins.gitlab.controller.GLIController;
import com.neon.intellij.plugins.gitlab.model.gitlab.GIPIssue;
import com.neon.intellij.plugins.gitlab.model.gitlab.GIPProject;
import com.neon.intellij.plugins.gitlab.model.intellij.GLProjectNode;

import javax.swing.*;

public class GLProjectPopup extends JPopupMenu {

    public GLProjectPopup( final GLIController controller, final GLIssueListMouseAdapter mouseAdapter, final GLProjectNode node ) {
        final GIPProject project = node.getUserObject();

        JMenuItem refresh = new JMenuItem( "Refresh Issues", AllIcons.Actions.Refresh );
        refresh.addActionListener(e -> {
            node.removeAllChildren();
            mouseAdapter.doubleClick( node );
        });
        this.add( refresh );

        JMenuItem create = new JMenuItem( "New Issue", AllIcons.General.Add );
        create.addActionListener(e -> {
            GIPIssue issue = new GIPIssue();
            issue.project_id = project.id;
            controller.openEditor( issue );
        });
        this.add( create );
    }

}

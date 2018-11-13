package com.neon.intellij.plugins.gitlab.view.toolwindow;

import com.intellij.icons.AllIcons;
import com.neon.intellij.plugins.gitlab.OpenIssueEditorAction;
import com.neon.intellij.plugins.gitlab.RefreshProjectIssuesAction;
import com.neon.intellij.plugins.gitlab.model.gitlab.GIPIssue;
import com.neon.intellij.plugins.gitlab.model.gitlab.GIPProject;
import com.neon.intellij.plugins.gitlab.model.intellij.GLProjectNode;

import javax.swing.*;

public class GLProjectPopup extends JPopupMenu {

    public GLProjectPopup(final GLProjectNode node,
                          final OpenIssueEditorAction openIssueEditorAction,
                          final RefreshProjectIssuesAction refreshProjectIssuesAction ) {
        final GIPProject project = node.getUserObject();

        JMenuItem refresh = new JMenuItem( "Refresh Issues", AllIcons.Actions.Refresh );
        refresh.addActionListener(e -> {
            refreshProjectIssuesAction.accept( node );
        });
        this.add( refresh );

        JMenuItem create = new JMenuItem( "New Issue", AllIcons.General.Add );
        create.addActionListener(e -> {
            GIPIssue issue = new GIPIssue();
            issue.project_id = project.id;
            openIssueEditorAction.accept( issue );
        });
        this.add( create );
    }

}

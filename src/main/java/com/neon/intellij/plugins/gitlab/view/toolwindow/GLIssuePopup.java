package com.neon.intellij.plugins.gitlab.view.toolwindow;

import com.intellij.icons.AllIcons;
import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.diagnostic.Logger;
import com.neon.intellij.plugins.gitlab.ChangeIssueStateAction;
import com.neon.intellij.plugins.gitlab.DeleteIssueAction;
import com.neon.intellij.plugins.gitlab.OpenIssueEditorAction;
import com.neon.intellij.plugins.gitlab.model.gitlab.GIPIssue;
import com.neon.intellij.plugins.gitlab.model.intellij.GLIssueNode;

import javax.swing.*;

public class GLIssuePopup extends JPopupMenu {

    private static final Logger LOG = Logger.getInstance("gitlab");

    public GLIssuePopup(final GLIssueNode node,
                        final OpenIssueEditorAction openIssueEditorAction,
                        final DeleteIssueAction deleteIssueAction,
                        final ChangeIssueStateAction changeIssueStateAction ) {
        final GIPIssue issue = node.getUserObject();

        JMenuItem editItem = new JMenuItem( "Edit", AllIcons.Actions.Edit );
        editItem.addActionListener(e -> openIssueEditorAction.accept( issue ));
        this.add(editItem);

        JMenuItem delete = new JMenuItem( "Delete", AllIcons.Actions.Delete );
        delete.addActionListener(e -> {
            GIPIssue deletedIssue = deleteIssueAction.apply(issue);
            issue.state = deletedIssue.state;
        });
        this.add( delete );

        JMenu statesMenu = new JMenu( "Change State To ..." );

        if ( "closed".equalsIgnoreCase( issue.state ) ) {
            JRadioButtonMenuItem reopen = new JRadioButtonMenuItem( "Re-Open", AllIcons.Actions.Resume );
            reopen.addActionListener(e -> {
                GIPIssue savedIssue = changeIssueStateAction.apply(issue, "reopen");
                issue.state = savedIssue.state;
            });
            statesMenu.add(reopen);
        } else {
            JRadioButtonMenuItem closed = new JRadioButtonMenuItem( "Closed", AllIcons.Actions.Close );
            closed.addActionListener(e -> {
                GIPIssue savedIssue = changeIssueStateAction.apply(issue, "closed");
                issue.state = savedIssue.state;
            });
            statesMenu.add(closed);
        }

        this.addSeparator();
        this.add( statesMenu );

        this.addSeparator();
        JMenuItem openBrowser = new JMenuItem( "Open in Browser", AllIcons.General.Web);
        openBrowser.addActionListener(e -> {
            if ( issue.web_url == null || issue.web_url.trim().isEmpty() ) {
                return ;
            }

            BrowserUtil.browse( issue.web_url );
        });
        openBrowser.setEnabled( issue.web_url != null && ! issue.web_url.trim().isEmpty() );
        this.add( openBrowser );
    }

}

package com.neon.intellij.plugins.gitlab.view;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.*;
import com.neon.intellij.plugins.gitlab.*;
import com.neon.intellij.plugins.gitlab.model.gitlab.GIPGroup;
import com.neon.intellij.plugins.gitlab.model.gitlab.GIPIssue;
import com.neon.intellij.plugins.gitlab.model.gitlab.GIPProject;
import com.neon.intellij.plugins.gitlab.model.gitlab.GIPUser;
import com.neon.intellij.plugins.gitlab.model.intellij.GLProjectNode;
import com.neon.intellij.plugins.gitlab.view.toolwindow.GLIssueListView;
import com.neon.intellij.plugins.gitlab.view.toolwindow.GLIssuesFilterView;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GitLabView extends JPanel implements GIPGroupObserver, GIPProjectObserver, GIPIssueObserver, GIPUserObserver {

    private final GLIssueListView glIssueListView ;

    private final GLIssuesFilterView glIssuesFilterView;

    public GitLabView(final OpenIssueEditorAction openIssueEditorAction,
                      final RefreshProjectIssuesAction refreshProjectIssuesAction,
                      final DeleteIssueAction deleteIssueAction,
                      final ChangeIssueStateAction changeIssueStateAction) {
        this.glIssueListView = new GLIssueListView( openIssueEditorAction, refreshProjectIssuesAction,
                deleteIssueAction, changeIssueStateAction);
        this.glIssuesFilterView = new GLIssuesFilterView( glIssueListView );

        this.setLayout( new BorderLayout( 5, 5 ) );
        this.add( glIssuesFilterView, BorderLayout.NORTH );
        this.add( buildActionsPanel( this, openIssueEditorAction, refreshProjectIssuesAction ), BorderLayout.WEST );
        this.add( glIssueListView, BorderLayout.CENTER );
    }


    private Component buildActionsPanel( final JComponent target,
                                         final OpenIssueEditorAction openIssueEditorAction,
                                         final RefreshProjectIssuesAction refreshProjectIssuesAction ) {
        final ActionManager actionManager = ActionManager.getInstance();

        final DefaultActionGroup actionGroup = new DefaultActionGroup();
        actionGroup.add( new AnAction( "Refresh All", "Refresh connection settings and projects list", AllIcons.Actions.Refresh ) {
            @Override
            public void actionPerformed(AnActionEvent anActionEvent) {
                refreshProjectIssuesAction.accept( null );
            }
        });
        actionGroup.addSeparator();
        actionGroup.add(new AnAction( "New Issue", "", AllIcons.General.Add ) {
            @Override
            public void actionPerformed(AnActionEvent anActionEvent) {
                GLProjectNode[] selected = glIssueListView.getSelectedNodes( GLProjectNode.class, null );
                if ( selected != null && selected.length > 0 ) {
                    GLProjectNode node = selected[ 0 ];
                    GIPProject project = node.getUserObject();

                    GIPIssue issue = new GIPIssue();
                    issue.project_id = project.id;
                    openIssueEditorAction.accept( issue );
                }
            }
        });
//        actionGroup.addSeparator();
//        actionGroup.add( new AnAction( "Expand All", "", AllIcons.Actions.Expandall ) {
//            @Override
//            public void actionPerformed(AnActionEvent anActionEvent) {
////                TODO : to implement
//            }
//        });
//        actionGroup.add(new AnAction("Collapse All", "", AllIcons.Actions.Collapseall) {
//            @Override
//            public void actionPerformed(AnActionEvent anActionEvent) {
////                TODO : to implement
//            }
//        });
//        actionGroup.add( new AnAction( "Group By Module", "", AllIcons.Actions.GroupByModule ) {
//            @Override
//            public void actionPerformed(AnActionEvent anActionEvent) {
////                TODO : to implement
//            }
//        });

        ActionToolbar actionToolbar = actionManager.createActionToolbar("Gitlab Integration Toolbar", actionGroup, false);
        actionToolbar.setTargetComponent( target );
        return actionToolbar.getComponent();
    }


    @Override
    public void accept(GIPGroup group) {
        glIssueListView.accept( group );
    }

    @Override
    public void accept(GIPProject project) {
        glIssueListView.accept( project );
    }

    @Override
    public void accept(GIPIssue issue) {
        glIssueListView.accept( issue );
    }

    @Override
    public void onStart() {
        glIssuesFilterView.onStart();
    }

    @Override
    public void accept(List<GIPUser> users) {
        glIssuesFilterView.accept( users );
    }
}

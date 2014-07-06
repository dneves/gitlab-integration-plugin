package com.neon.intellij.plugins.gitlab.view;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.progress.ProgressManager;
import com.neon.intellij.plugins.gitlab.controller.GLIController;
import com.neon.intellij.plugins.gitlab.view.toolwindow.GLIssueListView;
import com.neon.intellij.plugins.gitlab.model.intellij.GLProjectNode;
import com.neon.intellij.plugins.gitlab.view.toolwindow.GLIssuesFilterView;
import com.neon.intellij.plugins.gitlab.view.toolwindow.GetProjectsTask;
import java.awt.BorderLayout;
import java.awt.Component;
import org.gitlab.api.models.GitlabIssue;
import org.gitlab.api.models.GitlabProject;

import javax.swing.*;

public class GitLabView extends JPanel {

    private final GLIController controller;

    private final GLIssuesFilterView filterView;

    private final GLIssueListView glIssueListView;

    public GitLabView( final GLIController controller ) {
        this.controller = controller;

        this.glIssueListView = new GLIssueListView( controller );
        this.filterView = new GLIssuesFilterView( controller, glIssueListView );

        setupComponents();
        setupLayout();
    }

    private void setupComponents() {
        refreshProjects();
    }

    private void setupLayout() {
        this.setLayout( new BorderLayout() );
        this.add( filterView, BorderLayout.NORTH );
        this.add( buildActionsPanel( this ), BorderLayout.WEST );
        this.add( glIssueListView, BorderLayout.CENTER );
    }

    private Component buildActionsPanel( final JComponent target ) {
        final ActionManager actionManager = ActionManager.getInstance();

        final DefaultActionGroup actionGroup = new DefaultActionGroup();
        actionGroup.add( new AnAction( "Refresh All", "Refresh connection settings and projects list", AllIcons.Actions.Refresh ) {
            @Override
            public void actionPerformed(AnActionEvent anActionEvent) {
                controller.refresh();
                refreshProjects();
            }
        });
        actionGroup.addSeparator();
        actionGroup.add(new AnAction( "New Issue", "", AllIcons.General.Add ) {
            @Override
            public void actionPerformed(AnActionEvent anActionEvent) {
                GLProjectNode[] selected = glIssueListView.getSelectedNodes( GLProjectNode.class, null );
                if ( selected != null && selected.length > 0 ) {
                    GLProjectNode node = selected[ 0 ];
                    GitlabProject project = node.getUserObject();

                    GitlabIssue issue = new GitlabIssue();
                    issue.setProjectId( project.getId() );
                    controller.openEditor( issue );
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

    private void refreshProjects() {
        ProgressManager.getInstance().run( new GetProjectsTask( controller, glIssueListView ) );
    }

}

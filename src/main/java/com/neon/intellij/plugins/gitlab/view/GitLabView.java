package com.neon.intellij.plugins.gitlab.view;

import com.neon.intellij.plugins.gitlab.GIPGroupObserver;
import com.neon.intellij.plugins.gitlab.GIPIssueObserver;
import com.neon.intellij.plugins.gitlab.GIPProjectObserver;
import com.neon.intellij.plugins.gitlab.GIPUserObserver;
import com.neon.intellij.plugins.gitlab.controller.GLIController;
import com.neon.intellij.plugins.gitlab.model.gitlab.GIPGroup;
import com.neon.intellij.plugins.gitlab.model.gitlab.GIPIssue;
import com.neon.intellij.plugins.gitlab.model.gitlab.GIPProject;
import com.neon.intellij.plugins.gitlab.model.gitlab.GIPUser;
import com.neon.intellij.plugins.gitlab.view.toolwindow.GLIssueListView;
import com.neon.intellij.plugins.gitlab.view.toolwindow.GLIssuesFilterView;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GitLabView extends JPanel implements GIPGroupObserver, GIPProjectObserver, GIPIssueObserver, GIPUserObserver {

    private final GLIssueListView glIssueListView ;

    private final GLIssuesFilterView glIssuesFilterView;

    public GitLabView( final GLIController controller ) {
        this.glIssueListView = new GLIssueListView( controller );
        this.glIssuesFilterView = new GLIssuesFilterView( glIssueListView );

        this.setLayout( new BorderLayout( 10, 10 ) );
        this.add( glIssuesFilterView, BorderLayout.NORTH );
//        this.add( buildActionsPanel( this ), BorderLayout.WEST );
        this.add( glIssueListView, BorderLayout.CENTER );
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

//    private final GLIController controller;
//
//    private final GLIssuesFilterView filterView;
//
//
//    public GitLabView( final GLIController controller ) {
//        this.controller = controller;
//
//        this.glIssueListView = new GLIssueListView( controller );
//        this.filterView = new GLIssuesFilterView( controller, glIssueListView );
//
//        setupComponents();
//        setupLayout();
//    }
//
//    private void setupComponents() {
//        refreshProjects();
//    }
//
//    private void setupLayout() {
//        this.setLayout( new BorderLayout() );
//        this.add( filterView, BorderLayout.NORTH );
//        this.add( buildActionsPanel( this ), BorderLayout.WEST );
//        this.add( glIssueListView, BorderLayout.CENTER );
//    }
//
//    private Component buildActionsPanel( final JComponent target ) {
//        final ActionManager actionManager = ActionManager.getInstance();
//
//        final DefaultActionGroup actionGroup = new DefaultActionGroup();
//        actionGroup.add( new AnAction( "Refresh All", "Refresh connection settings and projects list", AllIcons.Actions.Refresh ) {
//            @Override
//            public void actionPerformed(AnActionEvent anActionEvent) {
//                controller.refresh();
//                refreshProjects();
//            }
//        });
//        actionGroup.addSeparator();
//        actionGroup.add(new AnAction( "New Issue", "", AllIcons.General.Add ) {
//            @Override
//            public void actionPerformed(AnActionEvent anActionEvent) {
//                GLProjectNode[] selected = glIssueListView.getSelectedNodes( GLProjectNode.class, null );
//                if ( selected != null && selected.length > 0 ) {
//                    GLProjectNode node = selected[ 0 ];
//                    GitlabProject project = node.getUserObject();
//
//                    GitlabIssue issue = new GitlabIssue();
//                    issue.setProjectId( project.getId() );
//                    controller.openEditor( issue );
//                }
//            }
//        });
////        actionGroup.addSeparator();
////        actionGroup.add( new AnAction( "Expand All", "", AllIcons.Actions.Expandall ) {
////            @Override
////            public void actionPerformed(AnActionEvent anActionEvent) {
//////                TODO : to implement
////            }
////        });
////        actionGroup.add(new AnAction("Collapse All", "", AllIcons.Actions.Collapseall) {
////            @Override
////            public void actionPerformed(AnActionEvent anActionEvent) {
//////                TODO : to implement
////            }
////        });
////        actionGroup.add( new AnAction( "Group By Module", "", AllIcons.Actions.GroupByModule ) {
////            @Override
////            public void actionPerformed(AnActionEvent anActionEvent) {
//////                TODO : to implement
////            }
////        });
//
//        ActionToolbar actionToolbar = actionManager.createActionToolbar("Gitlab Integration Toolbar", actionGroup, false);
//        actionToolbar.setTargetComponent( target );
//        return actionToolbar.getComponent();
//    }
//
//    private void refreshProjects() {
//        ProgressManager.getInstance().run( new GetGroupsTask( controller, glIssueListView ) );
//    }
//
//}

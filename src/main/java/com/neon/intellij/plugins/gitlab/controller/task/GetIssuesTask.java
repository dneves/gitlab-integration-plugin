//package com.neon.intellij.plugins.gitlab.controller.task;
//
//import com.intellij.openapi.diagnostic.Logger;
//import com.intellij.openapi.progress.ProgressIndicator;
//import com.intellij.openapi.progress.Task;
//import com.neon.intellij.plugins.gitlab.controller.GLIController;
//import com.neon.intellij.plugins.gitlab.model.intellij.GLProjectNode;
//import com.neon.intellij.plugins.gitlab.view.toolwindow.ProjectIssuesHolder;
//import org.jetbrains.annotations.NotNull;
//
//import javax.swing.*;
//import java.util.LinkedList;
//import java.util.List;
//
//public class GetIssuesTask extends Task.Backgroundable {
//
//    private static final Logger LOG = Logger.getInstance("gitlab");
//
//    private final GLIController controller;
//
//    private final List< GLProjectNode > projectNodes;
//
//    private final ProjectIssuesHolder issuesHolder;
//
//    private final int totalProjects;
//
//    private int currentProject = 0;
//
//    public GetIssuesTask(final GLIController controller, final List< GLProjectNode > projectNodes, final ProjectIssuesHolder issuesHolder) {
//        super( controller.getProject(), "Get " + projectNodes.size() + " Issues", true );
//        this.controller = controller;
//        this.projectNodes = projectNodes;
//        this.issuesHolder = issuesHolder;
//        this.totalProjects = projectNodes.size();
//    }
//
//    @Override
//    public void run( @NotNull ProgressIndicator progressIndicator ) {
//        progressIndicator.setFraction( 0.0 );
//        progressIndicator.setText( "fetching remote issues for " + projectNodes.size() + " projects ..." );
//
//        List<UpdatableComponents> update = fetch(progressIndicator, projectNodes);
//        updateView( update );
//
//        progressIndicator.setFraction( 1.0 );
//        progressIndicator.setText( "get issues done" );
//    }
//
//    private List< UpdatableComponents > fetch( final ProgressIndicator progressIndicator, final List< GLProjectNode > projectNodes ) {
//        final List< UpdatableComponents > update = new LinkedList<>();
////        for ( final GLProjectNode projectNode : projectNodes ) {
////            try {
////                final Collection<GitlabIssue> issues = controller.getIssues( projectNode.getUserObject() );
////                update.add( new UpdatableComponents( projectNode, issues ) );
////
////                progressIndicator.setFraction( ( ++currentProject ) / totalProjects );
////                progressIndicator.setText("got " + (issues == null ? "0" : issues.size()) + " issues for "
////                        + projectNode.getUserObject().getName() + " ( " + currentProject + " / " + totalProjects + " )" );
////
////                LOG.info( "got " + (issues == null ? "0" : issues.size()) + " issues for "
////                        + projectNode.getUserObject().getName() + " ( " + currentProject + " / " + totalProjects + " )" );
////            } catch ( Exception e1 ) {
////                LOG.error( e1 );
////            }
////        }
//        return update;
//    }
//
//    private class UpdatableComponents {
//        public GLProjectNode projectNode;
////        public Collection< GitlabIssue > issues;
//
////        private UpdatableComponents(GLProjectNode projectNode, Collection<GitlabIssue> issues) {
////            this.projectNode = projectNode;
////            this.issues = issues;
////        }
//    }
//
//    private void updateView( final List< UpdatableComponents > update ) {
//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                for ( UpdatableComponents entry : update ) {
//                    GLProjectNode projectNode = entry.projectNode;
////                    Collection< GitlabIssue > issues = entry.issues;
////                    issuesHolder.addIssues( issues, projectNode );
//                }
//            }
//        });
//    }
//
//}

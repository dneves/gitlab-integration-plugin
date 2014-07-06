package com.neon.intellij.plugins.gitlab.view.toolwindow;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.neon.intellij.plugins.gitlab.controller.GLIController;
import com.neon.intellij.plugins.gitlab.model.intellij.GLProjectNode;
import java.io.IOException;
import java.util.List;
import javax.swing.SwingUtilities;
import org.gitlab.api.models.GitlabIssue;
import org.jetbrains.annotations.NotNull;

public class GetProjectIssuesTask extends Task.Backgroundable {

    private static final Logger LOG = Logger.getInstance("gitlab");

    private final GLIController controller;

    private final GLProjectNode projectNode;

    private final ProjectIssuesHolder issuesHolder;

    public GetProjectIssuesTask(final GLIController controller, final GLProjectNode projectNode, final ProjectIssuesHolder issuesHolder ) {
        super( controller.getProject(), "Get " + projectNode.getUserObject().getName() + " Issues", true );
        this.controller = controller;
        this.projectNode = projectNode;
        this.issuesHolder = issuesHolder;
    }

    @Override
    public void run( @NotNull ProgressIndicator progressIndicator ) {
        progressIndicator.setFraction( 0.0 );
        progressIndicator.setText( "Fetching remote issues for " + projectNode.getUserObject().getName() );

        try {
            final List<GitlabIssue> issues = controller.getIssues( projectNode.getUserObject() );

            progressIndicator.setFraction( 0.5 );
            progressIndicator.setText( "Got " + ( issues == null ? "0" : issues.size()) + " remote issues" );

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    issuesHolder.addIssues(issues, projectNode);
                }
            });

            progressIndicator.setFraction( 1.0 );
            progressIndicator.setText( "Get issues done" );
        } catch ( IOException e1 ) {
            LOG.error( e1 );
        }
    }

}

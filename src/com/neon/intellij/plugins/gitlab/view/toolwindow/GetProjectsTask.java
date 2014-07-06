package com.neon.intellij.plugins.gitlab.view.toolwindow;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.neon.intellij.plugins.gitlab.controller.GLIController;
import java.io.IOException;
import java.util.List;
import javax.swing.SwingUtilities;
import org.gitlab.api.models.GitlabProject;
import org.jetbrains.annotations.NotNull;

public class GetProjectsTask extends Task.Backgroundable {

    private static final Logger LOG = Logger.getInstance("gitlab");

    private final GLIController controller;

    private final ProjectsHolder projectsHolder;

    public GetProjectsTask( final GLIController controller, final ProjectsHolder projectsHolder ) {
        super( controller.getProject(), "Get Projects Task", true );
        this.controller = controller;
        this.projectsHolder = projectsHolder;
    }

    @Override
    public void run(@NotNull final ProgressIndicator progressIndicator) {
        progressIndicator.setFraction( 0.0 );
        progressIndicator.setText( "Fetching remote projects" );

        try {
            final List<GitlabProject> projects = controller.getProjects();

            progressIndicator.setFraction( 0.5 );
            progressIndicator.setText( "Got " + ( projects == null ? "0" : projects.size() ) + " remote projects" );

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    projectsHolder.addProjects( projects );
                }
            });

            progressIndicator.setFraction( 1.0 );
            progressIndicator.setText( "Get projects done" );
        } catch ( IOException e1 ) {
            LOG.error( e1 );
        }
    }

}

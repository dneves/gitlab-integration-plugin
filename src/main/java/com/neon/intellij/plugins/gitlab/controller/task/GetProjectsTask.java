package com.neon.intellij.plugins.gitlab.controller.task;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.neon.intellij.plugins.gitlab.controller.GLIController;
import com.neon.intellij.plugins.gitlab.view.toolwindow.ProjectsHolder;
import java.io.IOException;
import java.util.Collection;
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
        progressIndicator.setText( "fetching remote projects" );

        try {
            final Collection<GitlabProject> projects = controller.getProjects();

            progressIndicator.setFraction( 0.5 );
            progressIndicator.setText( "got " + ( projects == null ? "0" : projects.size() ) + " projects" );

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    projectsHolder.addProjects( projects );
                }
            });

            progressIndicator.setFraction( 1.0 );
            progressIndicator.setText( "get projects done" );
        } catch ( IOException e1 ) {
            LOG.error( e1 );
        }
    }

}

package com.neon.intellij.plugins.gitlab.view;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.neon.intellij.plugins.gitlab.controller.GLIController;
import com.neon.intellij.plugins.gitlab.view.issues.GLIssueListView;
import info.clearthought.layout.TableLayout;
import info.clearthought.layout.TableLayoutConstraints;
import java.io.IOException;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.gitlab.api.models.GitlabProject;
import org.jetbrains.annotations.NotNull;

public class GitLabView extends JPanel {

    private static final Logger LOG = Logger.getInstance("gitlab");

    private final GLIController controller;

    private final GLIssueListView glIssueListView;

    public GitLabView( final GLIController controller ) {
        this.controller = controller;

        this.glIssueListView = new GLIssueListView( controller );

        setupComponents();
        setupLayout();
    }

    private void setupComponents() {
        refreshProjects();
    }

    private void refreshProjects() {
        ProgressManager.getInstance().run( new Task.Backgroundable( controller.getProject(), "Get Projects" ) {
            @Override
            public void run(@NotNull ProgressIndicator progressIndicator) {
                progressIndicator.setFraction( 0.0 );
                progressIndicator.setText( "Getting remote projects" );

                try {
                    final List<GitlabProject> projects = controller.getProjects();

                    progressIndicator.setFraction( 0.5 );
                    progressIndicator.setText( "Got remote projects" );

                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            glIssueListView.addProjects(projects);
                        }
                    });

                    progressIndicator.setFraction( 1.0 );
                    progressIndicator.setText( "Get projects done" );
                } catch ( IOException e1 ) {
                    LOG.error( e1 );
                }
            }
        } );
    }

    private void setupLayout() {
        this.setLayout( new TableLayout(
                new double[] { TableLayout.FILL },
                new double[] { TableLayout.MINIMUM, TableLayout.FILL }
        ) );

        this.add( new JLabel( "options" ), new TableLayoutConstraints( 0, 0, 0, 0, TableLayout.RIGHT, TableLayout.CENTER ) );
        this.add( glIssueListView, new TableLayoutConstraints( 0, 1, 0, 1, TableLayout.FULL, TableLayout.FULL ) );
    }

}

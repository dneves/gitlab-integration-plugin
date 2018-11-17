package com.neon.intellij.plugins.gitlab.controller;


import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.neon.intellij.plugins.gitlab.*;
import com.neon.intellij.plugins.gitlab.controller.editor.GLIssueVirtualFile;
import com.neon.intellij.plugins.gitlab.model.gitlab.GIPIssue;
import com.neon.intellij.plugins.gitlab.model.gitlab.GIPProject;
import com.neon.intellij.plugins.gitlab.model.intellij.GLProjectNode;
import com.neon.intellij.plugins.gitlab.view.GitLabView;

import javax.swing.*;
import java.util.logging.Logger;

public class GLIController {

    private static final Logger LOGGER = Logger.getLogger( GLIController.class.getName() );

    private final Project project;

    private final JBFacade jbFacade;

    private final GitLabView view;

    private final GitLabServiceSupplier gitLabServiceSupplier;

    public GLIController(final Project project, final GitLabServiceSupplier gitLabServiceSupplier) {
        this.project = project;
        this.gitLabServiceSupplier = gitLabServiceSupplier;

        this.jbFacade = new JBFacade( project );

        this.view = new GitLabView( project,
                gipIssue -> openEditor( gipIssue ),
                projectNode -> refresh( projectNode ),
                gipIssue -> deleteIssue( gipIssue),
                (gipIssue, newState) -> changeState( gipIssue, newState ));
    }

    public GitLabView getView() {
        return view;
    }

    public void run() {
        refresh( view, view, view, view );
    }

    private void openEditor( GIPIssue issue ) {
        jbFacade.openEditor( new GLIssueVirtualFile( issue,
                gipIssue -> saveIssue( gipIssue ),
                vf -> closeEditor( vf ) ) );
    }

    private void closeEditor( final GLIssueVirtualFile vf ) {
        jbFacade.closeEditor(vf);
    }

    private void refreshSelectedProjectNodes() {
        GLProjectNode[] glProjectNodes = view.getSelectedNodes(GLProjectNode.class, null);
        if ( glProjectNodes != null && glProjectNodes.length > 0 ) {
            for (GLProjectNode glProjectNode : glProjectNodes) {
                refresh( glProjectNode );
            }
        }
    }

    private GIPIssue saveIssue( GIPIssue issue ) {
        GIPIssue response;
        if ( issue.id == null || issue.id <= 0 ) {
            response = gitLabServiceSupplier
                    .get()
                    .createIssue(issue.project_id, issue.title, issue.description)
                    .blockingSingle();
        } else {
            response = gitLabServiceSupplier
                    .get()
                    .updateIssue( issue.project_id, issue.iid, issue.title, issue.description, issue.state )
                    .blockingSingle();
        }

        refreshSelectedProjectNodes();

        return response;
    }

    private void deleteIssue(final GIPIssue issue) {
        gitLabServiceSupplier
                .get()
                .deleteIssue(issue.project_id, issue.iid)
                .blockingAwait();

        refreshSelectedProjectNodes();
    }

    private GIPIssue changeState(final GIPIssue issue, final String newState) {
        GIPIssue response = gitLabServiceSupplier
                .get()
                .changeIssueState(issue.project_id, issue.iid, newState)
                .blockingSingle();

        refreshSelectedProjectNodes();

        return response;
    }
//
    private void refresh(GIPGroupObserver viewGroupObserver,
                         GIPProjectObserver viewProjectObserver,
                         GIPIssueObserver viewIssueObserver,
                         GIPUserObserver viewUserObserver ) {
        GitLabService gitLabService = gitLabServiceSupplier.get();

        GIPGroupObserver groupObserver = group -> {
            SwingUtilities.invokeLater(() -> viewGroupObserver.accept( group ));

            GIPProjectObserver projectObserver = project -> {
                SwingUtilities.invokeLater(() -> viewProjectObserver.accept( project ));

                updateProjectIssues( gitLabService, project, viewProjectObserver, viewIssueObserver );
            };

            GetProjectsTask getProjectsTask = new GetProjectsTask( project, gitLabService, projectObserver, group.id);
            ProgressManager.getInstance().run(getProjectsTask);
        };

        groupObserver.onStartGroupsUpdate();
        ProgressManager.getInstance().run( new GetGroupsTask( project, gitLabService, groupObserver ) );

        ProgressManager.getInstance().run( new GetUsersTask( project, gitLabService, viewUserObserver ) );
    }

    private void updateProjectIssues( GitLabService gitLabService, GIPProject project,
                                      GIPProjectObserver viewProjectObserver,
                                      GIPIssueObserver viewIssueObserver ) {
        SwingUtilities.invokeLater(() -> {
            viewProjectObserver.onStartProjectUpdate( project );

            GetIssuesTask getIssuesTask = new GetIssuesTask(GLIController.this.project, gitLabService,
                    issue -> SwingUtilities.invokeLater(() -> viewIssueObserver.accept(issue)), project.id);
            ProgressManager.getInstance().run(getIssuesTask);
        });
    }

    private void refresh( GLProjectNode projectNode ) {
        if ( projectNode == null ) {
            refresh( view, view, view, view );
            return ;
        }

        updateProjectIssues( gitLabServiceSupplier.get(), projectNode.getUserObject(), view, view );
    }

}

package com.neon.intellij.plugins.gitlab.controller;


import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.neon.intellij.plugins.gitlab.*;
import com.neon.intellij.plugins.gitlab.controller.editor.GLIssueVirtualFile;
import com.neon.intellij.plugins.gitlab.model.gitlab.GIPIssue;
import com.neon.intellij.plugins.gitlab.model.intellij.GLProjectNode;
import com.neon.intellij.plugins.gitlab.view.GitLabView;

import javax.swing.*;
import java.util.logging.Logger;

public class GLIController {

    private static final Logger LOGGER = Logger.getLogger( GLIController.class.getName() );

    private final Project project;

    private final ToolWindow toolWindow;

    private final JBFacade jbFacade;

    private final GitLabView view;

    private final GitLabServiceSupplier gitLabServiceSupplier;

    public GLIController(final Project project, final ToolWindow toolWindow, final GitLabServiceSupplier gitLabServiceSupplier) {
        this.project = project;
        this.toolWindow = toolWindow;
        this.gitLabServiceSupplier = gitLabServiceSupplier;

        this.jbFacade = new JBFacade( project );

        this.view = new GitLabView(
                gipIssue -> openEditor( gipIssue ),
                projectNode -> refresh( projectNode ),
                gipIssue -> deleteIssue( gipIssue),
                (gipIssue, newState) -> changeState( gipIssue, newState ));
    }

    public void run() {
        refresh( view, view, view, view );

        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent( view, "", false );
        toolWindow.getContentManager().addContent( content );
    }

    private void openEditor( GIPIssue issue ) {
        jbFacade.openEditor( new GLIssueVirtualFile( issue,
                gipIssue -> saveIssue( gipIssue ),
                vf -> closeEditor( vf ) ) );
    }

    private void closeEditor( final GLIssueVirtualFile vf ) {
        jbFacade.closeEditor(vf);
    }

    private GIPIssue saveIssue( GIPIssue issue ) {
        if ( issue.id <= 0 ) {
            return gitLabServiceSupplier
                    .get()
                    .createIssue( issue.project_id, issue.title )
                    .blockingSingle();
        } else {
            return gitLabServiceSupplier
                    .get()
                    .updateIssue( issue.project_id, issue.iid, issue.title, issue.description, issue.state )
                    .blockingSingle();
        }
    }

    private GIPIssue deleteIssue(final GIPIssue issue) {
        return gitLabServiceSupplier
                .get()
                .deleteIssue( issue.project_id, issue.iid )
                .blockingSingle();
    }

    private GIPIssue changeState(final GIPIssue issue, final String newState) {
        return gitLabServiceSupplier
                .get()
                .changeIssueState( issue.project_id, issue.iid, newState )
                .blockingSingle();
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

                GetIssuesTask getIssuesTask = new GetIssuesTask(GLIController.this.project, gitLabService,
                        issue -> SwingUtilities.invokeLater(() -> viewIssueObserver.accept(issue)), project.id);
                ProgressManager.getInstance().run(getIssuesTask);
            };

            GetProjectsTask getProjectsTask = new GetProjectsTask( project, gitLabService, projectObserver, group.id);
            ProgressManager.getInstance().run(getProjectsTask);
        };
        ProgressManager.getInstance().run( new GetGroupsTask( project, gitLabService, groupObserver ) );

        ProgressManager.getInstance().run( new GetUsersTask( project, gitLabService, viewUserObserver ) );
    }

    private void refresh( GLProjectNode projectNode ) {
        if ( projectNode == null ) {
            refresh( view, view, view, view );
            return ;
        }

//        TODO: refresh only this project's issues
    }

}

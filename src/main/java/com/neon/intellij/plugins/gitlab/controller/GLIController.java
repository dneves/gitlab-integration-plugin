package com.neon.intellij.plugins.gitlab.controller;


import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.neon.intellij.plugins.gitlab.*;
import com.neon.intellij.plugins.gitlab.controller.editor.GLIssueVirtualFile;
import com.neon.intellij.plugins.gitlab.GetUsersTask;
import com.neon.intellij.plugins.gitlab.model.gitlab.GIPIssue;
import com.neon.intellij.plugins.gitlab.view.GitLabView;

import javax.swing.*;
import java.io.IOException;
import java.util.function.Supplier;
import java.util.logging.Logger;

public class GLIController {

    private static final Logger LOGGER = Logger.getLogger( GLIController.class.getName() );

    private final Project project;

    private final ToolWindow toolWindow;

    private final JBFacade jbFacade;


    public GLIController(final Project project, final ToolWindow toolWindow) {
        this.project = project;
        this.toolWindow = toolWindow;

        this.jbFacade = new JBFacade( project );
    }

    public void run() {
        final GitLabView view = new GitLabView( this );

        refresh( view );

        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent( view, "", false );
        toolWindow.getContentManager().addContent( content );
    }

    public void openEditor( final GIPIssue issue ) {
        jbFacade.openEditor( new GLIssueVirtualFile( this, issue ) );
    }

    public void closeEditor( final GLIssueVirtualFile vf ) {
        jbFacade.closeEditor(vf);
    }

    public GIPIssue saveIssue(final GIPIssue issue) throws IOException {
//        TODO: persist issue
        if ( issue.id <= 0 ) {
//            return glFacade.createIssue(issue);
        } else {
//            return glFacade.editIssue(issue);
        }
        return issue;
    }

    public GIPIssue deleteIssue(final GIPIssue issue) throws IOException {
//        TODO: delete gitlab issue
        return issue;
//        return glFacade.closeIssue( issue );
    }
//
    public GIPIssue changeState(final GIPIssue issue, final String newState) throws IOException {
//        TODO: switch issue state
//        if ( GLIssueState.REOPEN.equals( newState ) ) {
//            return glFacade.openIssue(issue);
//        } else if ( GLIssueState.CLOSED.equals( newState ) ) {
//            return glFacade.closeIssue(issue);
//        }
        return issue;
    }
//
    public void refresh(GitLabView view) {
        final GitLabService gitLabService = new GitLabServiceSupplier(new ConnectionPropertiesSupplier()).get();

        GIPGroupObserver groupObserver = group -> {
            SwingUtilities.invokeLater(() -> view.accept( group ));

            GIPProjectObserver projectObserver = project -> {
                SwingUtilities.invokeLater(() -> view.accept( project ));

                GetIssuesTask getIssuesTask = new GetIssuesTask(GLIController.this.project, gitLabService,
                        issue -> SwingUtilities.invokeLater(() -> view.accept(issue)), project.id);
                ProgressManager.getInstance().run(getIssuesTask);
            };

            GetProjectsTask getProjectsTask = new GetProjectsTask( project, gitLabService, projectObserver, group.id);
            ProgressManager.getInstance().run(getProjectsTask);
        };
        ProgressManager.getInstance().run( new GetGroupsTask( project, gitLabService, groupObserver ) );

        ProgressManager.getInstance().run( new GetUsersTask( project, gitLabService, view ) );
    }


}

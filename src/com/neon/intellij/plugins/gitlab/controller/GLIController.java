package com.neon.intellij.plugins.gitlab.controller;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.neon.intellij.plugins.gitlab.controller.editor.GLIssueVirtualFile;
import com.neon.intellij.plugins.gitlab.model.gitlab.GLIssueState;
import com.neon.intellij.plugins.gitlab.model.intellij.ConfigurableState;
import com.neon.intellij.plugins.gitlab.view.GitLabView;
import java.io.IOException;
import java.util.List;
import org.gitlab.api.models.GitlabIssue;
import org.gitlab.api.models.GitlabProject;
import org.gitlab.api.models.GitlabUser;

public class GLIController {

    private static final Logger LOG = Logger.getInstance( "gitlab" );

    private final Project project;

    private final ToolWindow toolWindow;

    private final GLFacade glFacade;

    private final JBFacade jbFacade;


    public GLIController(final Project project, final ToolWindow toolWindow) {
        this.project = project;
        this.toolWindow = toolWindow;

        this.jbFacade = new JBFacade( project );

        Object[] connectionProperties = getConnectionProperties();
        this.glFacade = new GLFacade(
                (String)connectionProperties[ 0 ], (String)connectionProperties[ 1 ], (Boolean)connectionProperties[ 2] );
    }

    public Project getProject() {
        return project;
    }

    public void run() {
        final GitLabView view = new GitLabView( this );

        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent( view, "", false );
        toolWindow.getContentManager().addContent( content );
    }

    public List< GitlabProject > getProjects() throws IOException {
        return glFacade.getProjects();
    }

    public List<GitlabIssue > getIssues( final GitlabProject project ) throws IOException {
        return glFacade.getIssues( project );
    }

    public List<GitlabUser> getUsers() throws IOException {
        return glFacade.getUsers();
    }

    public static String getLabel( final GitlabUser user ) {
        String result = null;
        if ( user != null ) {
            result = user.getName();
            if (result == null || result.trim().isEmpty()) {
                result = user.getEmail();
            }
            if (result == null || result.trim().isEmpty()) {
                result = user.getUsername();
            }
        }
        return result != null ? result : "";
    }

    public void openEditor( final GitlabIssue issue ) {
        jbFacade.openEditor( new GLIssueVirtualFile( this, issue ) );
    }

    public void closeEditor( final GLIssueVirtualFile vf ) {
        jbFacade.closeEditor(vf);
    }

    public GitlabIssue saveIssue(final GitlabIssue issue) throws IOException {
        if ( issue.getId() <= 0 ) {
            return glFacade.createIssue(issue);
        } else {
            return glFacade.editIssue(issue);
        }
    }

    public GitlabIssue deleteIssue(final GitlabIssue issue) throws IOException {
        return glFacade.closeIssue( issue );
    }

    public GitlabIssue changeState(final GitlabIssue issue, final GLIssueState newState) throws IOException {
        if ( GLIssueState.REOPEN.equals( newState ) ) {
            return glFacade.openIssue(issue);
        } else if ( GLIssueState.CLOSED.equals( newState ) ) {
            return glFacade.closeIssue(issue);
        }
        return issue;
    }

    public void refresh() {
        Object[] properties = getConnectionProperties();
        glFacade.reload( (String) properties[0], (String) properties[1], (Boolean) properties[2] );
    }

    private Object[] getConnectionProperties() {
        ConfigurableState state = ConfigurableState.getInstance();
        LOG.info( "getConnectionProperties() : host=" + state.getHost() + ", token=" + state.getToken() );
        return new Object[] { state.host, state.token, state.ignoreCertificateErrors };
    }

}

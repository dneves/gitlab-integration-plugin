package com.neon.intellij.plugins.gitlab.controller;

import org.gitlab.api.GitlabAPI;
import org.gitlab.api.models.GitlabIssue;
import org.gitlab.api.models.GitlabNamespace;
import org.gitlab.api.models.GitlabProject;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.gitlab.api.models.GitlabUser;

public class GLFacade {

    private GitlabAPI api;

    public GLFacade( final String gitlabHost, final String gitlabApiToken ) {
        reload( gitlabHost, gitlabApiToken );
    }

    public boolean reload( final String host, final String token ) {
        if ( host != null && token != null && ! host.isEmpty() && ! token.isEmpty() ) {
            api = GitlabAPI.connect(host, token);
            api.ignoreCertificateErrors(true);
            return true;
        }
        return false;
    }

    private void checkApi() throws IOException {
        if ( api == null ) {
            throw new IOException( "please, configure plugin settings" );
        }
    }

    public List< GitlabProject > getProjects() throws IOException {
        checkApi();

        List<GitlabProject> projects = api.getProjects();
        Collections.sort( projects, new Comparator< GitlabProject >() {
            @Override
            public int compare( GitlabProject o1, GitlabProject o2 ) {
                GitlabNamespace namespace1 = o1.getNamespace();
                String n1 = namespace1 != null ? namespace1.getName().toLowerCase() : "Default";
                GitlabNamespace namespace2 = o2.getNamespace();
                String n2 = namespace2 != null ? namespace2.getName().toLowerCase() : "Default";

                int compareNamespace = n1.compareTo(n2);
                return compareNamespace != 0 ? compareNamespace : o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
            }
        } );
        return projects;
    }


    public List< GitlabIssue > getIssues( final GitlabProject project ) throws IOException {
        checkApi();

        List<GitlabIssue> issues = api.getIssues( project );
        Collections.sort( issues, new Comparator< GitlabIssue >() {
            @Override
            public int compare( GitlabIssue o1, GitlabIssue o2 ) {
                return o1.getTitle().compareTo( o2.getTitle() );
            }
        } );
        return issues;
    }

    public List< GitlabUser > getUsers() throws IOException {
        checkApi();

        List<GitlabUser> users = api.getUsers();
        Collections.sort( users, new Comparator<GitlabUser>() {
            @Override
            public int compare(GitlabUser user1, GitlabUser user2) {
                String label1 = GLIController.getLabel( user1 );
                String label2 = GLIController.getLabel( user2 );
                return label1.compareTo( label2 );
            }
        });
        return users;
    }

    public GitlabIssue createIssue(final GitlabIssue issue) throws IOException {
        checkApi();
        return api.createIssue( issue.getProjectId(), 0, 0, null, issue.getDescription(), issue.getTitle() );
    }

    public GitlabIssue editIssue(final GitlabIssue issue) throws IOException {
        return editIssue( issue, GitlabIssue.Action.LEAVE );
    }

    public GitlabIssue closeIssue(final GitlabIssue issue) throws IOException {
        return editIssue( issue, GitlabIssue.Action.CLOSE );
    }

    public GitlabIssue openIssue(final GitlabIssue issue) throws IOException {
        return editIssue( issue, GitlabIssue.Action.REOPEN );
    }

    private GitlabIssue editIssue(final GitlabIssue issue, final GitlabIssue.Action action) throws IOException {
        checkApi();

        String[] labelsList = issue.getLabels();
        StringBuilder labels = new StringBuilder();
        for (String label : labelsList) {
            labels.append( label );
        }

        return api.editIssue( issue.getProjectId(), issue.getId(),
                issue.getAssignee() == null ? 0 : issue.getAssignee().getId(),
                issue.getMilestone() == null ? 0 : issue.getMilestone().getId(),
                labels.toString(), issue.getDescription(), issue.getTitle(), action );
    }

}

package com.neon.intellij.plugins.gitlab.controller;

import java.io.IOException;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import org.gitlab.api.GitlabAPI;
import org.gitlab.api.models.GitlabIssue;
import org.gitlab.api.models.GitlabNamespace;
import org.gitlab.api.models.GitlabProject;
import org.gitlab.api.models.GitlabUser;

public class GLFacade {

    private GitlabAPI api;

    public GLFacade( final String gitlabHost, final String gitlabApiToken, final Boolean ignoreCertificateErrors ) {
        reload( gitlabHost, gitlabApiToken, ignoreCertificateErrors );
    }

    public boolean reload( final String host, final String token, final Boolean ignoreCertificateErrors ) {
        if ( host != null && token != null && ! host.isEmpty() && ! token.isEmpty() ) {
            api = GitlabAPI.connect(host, token);
            api.ignoreCertificateErrors( ignoreCertificateErrors );
            return true;
        }
        return false;
    }

    private void checkApi() throws IOException {
        if ( api == null ) {
            // todo : launch plugin configuration window (how?)
            throw new IOException( "please, configure plugin settings" );
        }
    }

    public Collection< GitlabProject > getProjects() throws IOException {
        checkApi();

        SortedSet< GitlabProject > result = new TreeSet<>( new Comparator< GitlabProject >() {
            @Override
            public int compare(GitlabProject o1, GitlabProject o2) {
                GitlabNamespace namespace1 = o1.getNamespace();
                String n1 = namespace1 != null ? namespace1.getName().toLowerCase() : "Default";
                GitlabNamespace namespace2 = o2.getNamespace();
                String n2 = namespace2 != null ? namespace2.getName().toLowerCase() : "Default";

                int compareNamespace = n1.compareTo(n2);
                return compareNamespace != 0 ? compareNamespace : o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
            }
        } );

        List<GitlabProject> projects = api.getProjects();
        result.addAll( projects );

        return result;
    }


    public Collection< GitlabIssue > getIssues( final GitlabProject project ) throws IOException {
        checkApi();

        SortedSet< GitlabIssue > result = new TreeSet<>( new Comparator<GitlabIssue>() {
            @Override
            public int compare(GitlabIssue o1, GitlabIssue o2) {
                return new Integer( o1.getId() ).compareTo( o2.getId() );
            }
        } );

        List<GitlabIssue> issues = api.getIssues( project );
        result.addAll( issues );

        return result;
    }

    public Collection< GitlabUser > getUsers() throws IOException {
        checkApi();

        SortedSet< GitlabUser > result = new TreeSet<>( new Comparator<GitlabUser>() {
            @Override
            public int compare(GitlabUser o1, GitlabUser o2) {
                String label1 = GLIController.getLabel( o1 );
                String label2 = GLIController.getLabel( o2 );
                return label1.compareTo( label2 );
            }
        });

        List<GitlabUser> users = api.getUsers();
        result.addAll( users );

        return result;
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

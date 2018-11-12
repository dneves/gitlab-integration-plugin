package com.neon.intellij.plugins.gitlab;

import com.neon.intellij.plugins.gitlab.model.gitlab.GIPGroup;
import com.neon.intellij.plugins.gitlab.model.gitlab.GIPIssue;
import com.neon.intellij.plugins.gitlab.model.gitlab.GIPProject;
import com.neon.intellij.plugins.gitlab.model.gitlab.GIPUser;
import io.reactivex.Observable;
import retrofit2.http.*;

import java.util.List;

public interface GitLabService {

    @GET( "groups" )
    Observable< List< GIPGroup > > listGroups( @Query("per_page") Integer limit,
                                               @Query("page") Integer page );

    @GET( "groups/{id}/projects" )
    Observable< List< GIPProject > > listGroupProjects( @Path("id") Integer groupId,
                                                   @Query("per_page") Integer limit,
                                                   @Query("page") Integer page );

    @GET( "projects/{id}/issues" )
    Observable< List<GIPIssue> > listProjectIssues( @Path("id") Integer projectId,
                                             @Query( "per_page" ) Integer limit,
                                             @Query( "page" ) Integer page );

    @GET( "users" )
    Observable< List<GIPUser > > listUsers( @Query( "per_page" ) Integer limit,
                                            @Query( "page" ) Integer page,
                                            @Query( "active" ) Boolean active,
                                            @Query("order_by") String orderBy,
                                            @Query("sort") String sort );

    @POST( "projects/{projectId}/issues")
    Observable< GIPIssue > createIssue( @Path( "projectId" ) Integer projectId,
                                        @Query( "title" ) String title );

    @PUT( "projects/{projectId}/issues/{issueIid}" )
    Observable< GIPIssue > updateIssue( @Path( "projectId" ) Integer projectId,
                                        @Path( "issueIid" ) Integer projectIssueId,
                                        @Query( "title" ) String title,
                                        @Query( "description" ) String description,
                                        @Query( "state_event" ) String state );

    @PUT( "projects/{projectId}/issues/{issueIid}" )
    Observable< GIPIssue > changeIssueState( @Path( "projectId" ) Integer projectId,
                                        @Path( "issueIid" ) Integer projectIssueId,
                                        @Query( "state_event" ) String state );

    @DELETE( "projects/{projectId}/issues/{issueIid}" )
    Observable< GIPIssue > deleteIssue( @Path( "projectId" ) Integer projectId,
                                        @Path( "issueIid" ) Integer projectIssueId );
}

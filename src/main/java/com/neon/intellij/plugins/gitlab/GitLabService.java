package com.neon.intellij.plugins.gitlab;

import com.neon.intellij.plugins.gitlab.model.gitlab.GIPGroup;
import com.neon.intellij.plugins.gitlab.model.gitlab.GIPProject;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;

public interface GitLabService {

    @GET( "groups" )
    Observable< List< GIPGroup > > listGroups( @Query("per_page") Integer limit,
                                               @Query("page") Integer page );

    @GET( "groups/{id}/projects" )
    Observable< List< GIPProject > > listProjects( @Path("id") Integer groupId,
                                                   @Query("per_page") Integer limit,
                                                   @Query("page") Integer page );

}

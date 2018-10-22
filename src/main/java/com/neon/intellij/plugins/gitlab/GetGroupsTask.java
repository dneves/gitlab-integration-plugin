package com.neon.intellij.plugins.gitlab;

import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.neon.intellij.plugins.gitlab.model.gitlab.GIPGroup;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class GetGroupsTask extends Task.Backgroundable {

    private static final Logger LOGGER = Logger.getLogger( GetGroupsTask.class.getName() );

    private final GitLabService gitLabService;

    private final GIPGroupObserver groupObserver;

    public GetGroupsTask(@Nullable Project project, GitLabService gitLabService, GIPGroupObserver groupObserver ) {
        super(project, "Getting Groups From Gitlab", true);
        this.gitLabService = gitLabService;
        this.groupObserver = groupObserver;
    }

    @Override
    public void run( @NotNull ProgressIndicator indicator ) {
        long start = System.currentTimeMillis();

        indicator.setIndeterminate(true);
        indicator.setText("fetching gitlab groups");

//        TODO: request more while hasNext (response.body.size >= limit)
        gitLabService.listGroups( 10 )
                .subscribe(new Observer<List<GIPGroup>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<GIPGroup> groups) {
                        groups.forEach(groupObserver::accept);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e );
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        indicator.setFraction(1.0);
        indicator.setText("fetched gitlab groups (" + (System.currentTimeMillis() - start) + "ms)");
    }

}

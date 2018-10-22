package com.neon.intellij.plugins.gitlab;

import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.neon.intellij.plugins.gitlab.model.gitlab.GIPProject;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GetProjectsTask extends Task.Backgroundable {

    private static final Logger LOGGER = Logger.getLogger( GetProjectsTask.class.getName() );

    private final GitLabService gitLabService;

    private final GIPProjectObserver projectObserver;

    private final Integer groupId;

    public GetProjectsTask(@Nullable Project project, GitLabService gitLabService, GIPProjectObserver projectObserver, Integer groupId ) {
        super(project, "Getting Projects From Gitlab", true);
        this.gitLabService = gitLabService;
        this.projectObserver = projectObserver;
        this.groupId = groupId;
    }

    @Override
    public void run(@NotNull ProgressIndicator indicator) {
        indicator.setIndeterminate(true);
        indicator.setText("fetching gitlab projects for group: ");

//        TODO: request more while hasNext (response.body.size >= limit)
        gitLabService.listProjects(groupId, 10 ).subscribe(new Observer<List<GIPProject>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<GIPProject> projects) {
                projects.forEach(projectObserver::accept);
            }

            @Override
            public void onError(Throwable e) {
                LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e );
            }

            @Override
            public void onComplete() {

            }
        });
    }

}

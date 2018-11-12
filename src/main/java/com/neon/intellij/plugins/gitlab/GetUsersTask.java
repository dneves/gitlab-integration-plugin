package com.neon.intellij.plugins.gitlab;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.neon.intellij.plugins.gitlab.model.gitlab.GIPUser;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GetUsersTask extends Task.Backgroundable {

    private static final Logger LOG = Logger.getInstance("gitlab");

    private final GitLabService service;

    private final GIPUserObserver usersObserver;

    public GetUsersTask(final Project project, final GitLabService service, final GIPUserObserver usersObserver ) {
        super( project, "Get Users Task", true );
        this.service = service;
        this.usersObserver = usersObserver;
    }

    @Override
    public void run(@NotNull ProgressIndicator progressIndicator) {
        progressIndicator.setIndeterminate( true );

        usersObserver.onStart();

        requestUsers( 1, usersObserver );
    }

    private void requestUsers( int page, GIPUserObserver usersObserver ) {
        service.listUsers( 10, page, true, "name", "asc" )
                .subscribe(new Observer<List<GIPUser>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<GIPUser> gipUsers) {
                        if ( gipUsers == null || gipUsers.isEmpty() ) {
                            return ;
                        }

                        usersObserver.accept( gipUsers );

                        if ( gipUsers.size() >= 10 ) {
                            requestUsers( page + 1, usersObserver );
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}

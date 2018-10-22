//package com.neon.intellij.plugins.gitlab.controller.task;
//
//import com.intellij.openapi.diagnostic.Logger;
//import com.intellij.openapi.progress.ProgressIndicator;
//import com.intellij.openapi.progress.Task;
//import com.neon.intellij.plugins.gitlab.controller.GLIController;
//import com.neon.intellij.plugins.gitlab.view.toolwindow.UsersHolder;
//import org.jetbrains.annotations.NotNull;
//
//public class GetUsersTask extends Task.Backgroundable {
//
//    private static final Logger LOG = Logger.getInstance("gitlab");
//
//    private final GLIController controller;
//
//    private final UsersHolder usersHolder;
//
//    public GetUsersTask( final GLIController controller, final UsersHolder usersHolder ) {
//        super( controller.getProject(), "Get Users Task", true );
//        this.controller = controller;
//        this.usersHolder = usersHolder;
//    }
//
//    @Override
//    public void run(@NotNull ProgressIndicator progressIndicator) {
//        progressIndicator.setFraction( 0.0 );
//        progressIndicator.setText( "Fetching remote users" );
//
////        try {
////            final Collection<GitlabUser> users = controller.getUsers();
//
////            progressIndicator.setFraction( 0.5 );
////            progressIndicator.setText( "Got " + ( users == null ? "0" : users.size() ) + " remote users" );
//
////            SwingUtilities.invokeLater(new Runnable() {
////                @Override
////                public void run() {
////                    usersHolder.setUsers( users );
////                }
////            });
//
//            progressIndicator.setFraction( 1.0 );
//            progressIndicator.setText( "Get users done" );
////        } catch ( IOException e1 ) {
////            LOG.error( e1 );
////        }
//    }
//
//}

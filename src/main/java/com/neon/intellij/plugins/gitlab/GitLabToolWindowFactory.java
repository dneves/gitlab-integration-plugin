package com.neon.intellij.plugins.gitlab;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.neon.intellij.plugins.gitlab.controller.GLIController;
import com.neon.intellij.plugins.gitlab.view.configurable.GitLabConfigurable;
import org.jetbrains.annotations.NotNull;

public class GitLabToolWindowFactory implements ToolWindowFactory {

    public GitLabToolWindowFactory() {

    }

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();

        final ConnectionPropertiesSupplier connectionPropertiesSupplier = new ConnectionPropertiesSupplier();

        final GLIController controller = createPlugin(project, connectionPropertiesSupplier);

        final Content mainContent = contentFactory.createContent( controller.getView(), "", false );

        final Content initContent = contentFactory.createContent( new VoidComponentView( project ), "", false );

        GitLabConfigurable.getInstance().onApply( () -> {
//                switch views (if configurable got data
            if ( isConfigurableConfigured( connectionPropertiesSupplier ) ) {

                toolWindow.getContentManager().removeContent( initContent, true );
                toolWindow.getContentManager().addContent( mainContent );

                controller.run();
            }
        } );

        if ( ! isConfigurableConfigured( connectionPropertiesSupplier ) ) {
            toolWindow.getContentManager().addContent( initContent );
        } else {
            toolWindow.getContentManager().addContent( mainContent );
            controller.run();
        }
    }

    private boolean isConfigurableConfigured( ConnectionPropertiesSupplier connectionPropertiesSupplier ) {
        ConnectionPropertiesSupplier.ConnectionProperties connectionProperties = connectionPropertiesSupplier.get();
        return !(connectionProperties.host == null || connectionProperties.host.trim().isEmpty() ||
                connectionProperties.privateToken == null || connectionProperties.privateToken.trim().isEmpty());
    }

    private GLIController createPlugin( Project project, ConnectionPropertiesSupplier connectionPropertiesSupplier ) {
        final GitLabServiceSupplier gitLabServiceSupplier = new GitLabServiceSupplier(connectionPropertiesSupplier);

        return new GLIController( project, gitLabServiceSupplier );
    }
}

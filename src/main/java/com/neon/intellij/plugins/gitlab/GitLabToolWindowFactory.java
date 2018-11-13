package com.neon.intellij.plugins.gitlab;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.neon.intellij.plugins.gitlab.controller.GLIController;
import org.jetbrains.annotations.NotNull;

public class GitLabToolWindowFactory implements ToolWindowFactory {

    public GitLabToolWindowFactory() {

    }

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        final GitLabServiceSupplier gitLabServiceSupplier = new GitLabServiceSupplier(new ConnectionPropertiesSupplier());

        GLIController controller = new GLIController( project, toolWindow, gitLabServiceSupplier );
        controller.run();
    }

}

package com.neon.intellij.plugins.gitlab.view.toolwindow;

import java.util.List;
import org.gitlab.api.models.GitlabProject;

public interface ProjectsHolder {

    void addProjects( List<GitlabProject> projects );

}

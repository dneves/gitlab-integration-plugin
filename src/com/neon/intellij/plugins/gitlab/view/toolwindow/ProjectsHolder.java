package com.neon.intellij.plugins.gitlab.view.toolwindow;

import java.util.Collection;
import org.gitlab.api.models.GitlabProject;

public interface ProjectsHolder {

    void addProjects( Collection<GitlabProject> projects );

}

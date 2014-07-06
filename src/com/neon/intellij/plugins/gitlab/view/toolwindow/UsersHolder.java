package com.neon.intellij.plugins.gitlab.view.toolwindow;

import java.util.List;
import org.gitlab.api.models.GitlabUser;

public interface UsersHolder {

    void setUsers( List<GitlabUser> users );

}

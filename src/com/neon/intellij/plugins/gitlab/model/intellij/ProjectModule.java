package com.neon.intellij.plugins.gitlab.model.intellij;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ModuleRootManager;
import org.jetbrains.annotations.NotNull;

public class ProjectModule {

    private final ModuleRootManager moduleRootManager;

    private final Module module;

    public ProjectModule( @NotNull Module module ) {
        this.module = module;
        this.moduleRootManager = ModuleRootManager.getInstance( module );
    }

}

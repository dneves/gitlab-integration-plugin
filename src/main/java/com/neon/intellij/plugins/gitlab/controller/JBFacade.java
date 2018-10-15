package com.neon.intellij.plugins.gitlab.controller;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.neon.intellij.plugins.gitlab.model.intellij.ProjectModule;
import java.util.LinkedList;
import java.util.List;

public class JBFacade {

    private final Logger LOG = Logger.getInstance( "gitlab" );

    private final ProjectRootManager projectRootManager;

    private final ModuleManager moduleManager;

    private final FileEditorManager fileEditorManager;

    private final PropertiesComponent properties;


    public JBFacade(final Project project) {
        this.projectRootManager = ProjectRootManager.getInstance( project );
        this.moduleManager = ModuleManager.getInstance( project );
        this.fileEditorManager = FileEditorManager.getInstance(project);
        this.properties = PropertiesComponent.getInstance( project );
    }

    public List<ProjectModule> getModules() {
        List< ProjectModule > result = new LinkedList< ProjectModule >();
        Module[] modules = moduleManager.getModules();
        for (Module module : modules) {
            result.add( new ProjectModule( module ) );
        }
        return result;
    }

    public void openEditor( VirtualFile vf ) {
        fileEditorManager.openFile( vf, true );
    }

    public void closeEditor( VirtualFile vf ) {
        fileEditorManager.closeFile( vf );
    }

    public void setProperty( String key, String value ) {
        properties.setValue( key, value );
    }
    public String getProperty( String key ) {
        return properties.getValue( key );
    }
}

package com.neon.intellij.plugins.gitlab.controller.editor;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorPolicy;
import com.intellij.openapi.fileEditor.FileEditorProvider;
import com.intellij.openapi.fileEditor.FileEditorState;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

public class GLEditorProvider implements ApplicationComponent, FileEditorProvider {

    public static final FileType FILE_TYPE = new GLFileType();


    public GLEditorProvider() {

    }


    @Override
    public boolean accept(@NotNull Project project, @NotNull VirtualFile virtualFile) {
        return ( virtualFile.getFileType() == FILE_TYPE );
    }

    @NotNull
    @Override
    public FileEditor createEditor(@NotNull Project project, @NotNull VirtualFile virtualFile) {
        FileEditor result = null;
        if ( virtualFile instanceof GLIssueVirtualFile) {
            result = new GLIssueEditor( (GLIssueVirtualFile) virtualFile );
        }
        return result;
    }

    @Override
    public void disposeEditor(@NotNull FileEditor fileEditor) {
        ( (GLIssueEditor) fileEditor ).dispose();
    }

    @NotNull
    @Override
    public FileEditorState readState(@NotNull Element element, @NotNull Project project, @NotNull VirtualFile virtualFile) {
        return GLFileEditorState.DUMMY;
    }

    @Override
    public void writeState(@NotNull FileEditorState fileEditorState, @NotNull Project project, @NotNull Element element) {

    }

    @NotNull
    @Override
    public String getEditorTypeId() {
        return getComponentName();
    }

    @NotNull
    @Override
    public FileEditorPolicy getPolicy() {
        return FileEditorPolicy.HIDE_DEFAULT_EDITOR;
    }

    @NotNull
    public String getComponentName() {
        return "GLEditorProvider";
    }

    @Override
    public void initComponent() {

    }

    @Override
    public void disposeComponent() {

    }

}

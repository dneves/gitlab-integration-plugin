package com.neon.intellij.plugins.gitlab.controller.editor;

import com.intellij.openapi.fileEditor.FileEditorState;
import com.intellij.openapi.fileEditor.FileEditorStateLevel;

public class GLFileEditorState implements FileEditorState {

    public static final FileEditorState DUMMY = new GLFileEditorState();

    @Override
    public boolean canBeMergedWith( FileEditorState otherState, FileEditorStateLevel level ) {
        return false;
    }
}
package com.neon.intellij.plugins.gitlab.controller.editor;

import com.intellij.ide.structureView.StructureViewBuilder;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import javax.swing.Icon;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GLFileType implements FileType {

    @NotNull
    @NonNls
    public String getName() {
        return "GitLab Integration file";
    }

    @NotNull
    public String getDescription() {
        return "GitLab Integration File";
    }

    @NotNull
    @NonNls
    public String getDefaultExtension() {
        return "gl";
    }

    @Nullable
    public Icon getIcon() {
        return null;
    }

    public boolean isBinary() {
        return false;
    }

    public boolean isReadOnly() {
        return false;
    }

    @Nullable
    @NonNls
    public String getCharset( @NotNull VirtualFile file ) {
        return null;
    }

    public String getCharset(@NotNull VirtualFile virtualFile, byte[] bytes) {
        return null;
    }

    @Nullable
    public SyntaxHighlighter getHighlighter( @Nullable Project project, final VirtualFile virtualFile ) {
        return null;
    }

    @Nullable
    public StructureViewBuilder getStructureViewBuilder( @NotNull VirtualFile file, @NotNull Project project ) {
        return null;
    }
}

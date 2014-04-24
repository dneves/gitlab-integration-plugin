package com.neon.intellij.plugins.gitlab.model;

public interface EditableView< INPUT, OUTPUT > {

    void fill( INPUT input );

    OUTPUT save();

}

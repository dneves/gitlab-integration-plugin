package com.neon.intellij.plugins.gitlab.model.gitlab;

@Deprecated
public enum GLIssueState {

    OPENED( "Opened", "opened" )
    , CLOSED( "Closed", "closed" )
    , REOPEN( "Re-Open", "reopen" );


    private final String label;

    private final String value;

    GLIssueState( String label, String value ) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public String getValue() {
        return value;
    }

    public static GLIssueState fromValue( String value ) {
        for ( GLIssueState state : GLIssueState.values() ) {
            if ( state.value.equalsIgnoreCase( value ) ) {
                return state;
            }
        }
        return null;
    }
}

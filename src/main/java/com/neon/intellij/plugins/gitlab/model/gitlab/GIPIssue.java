package com.neon.intellij.plugins.gitlab.model.gitlab;

import java.util.List;

public class GIPIssue {

    public String id;

    public Integer iid;

    public String title;

    public String description;

    public String state;

    public Integer project_id;

    public List< GIPIssueAssignee > assignees;

    public GIPIssueAuthor author;

}

package com.ambition.spotfix;

public class Issue {
    private String uid;
    private String issue;
    private String description;

    public Issue() {
        // Default constructor required for calls to DataSnapshot.getValue(Issue.class)
    }

    public Issue(String uid, String issue, String description) {
        this.uid = uid;
        this.issue = issue;
        this.description = description;
    }

    public String getUid() {
        return uid;
    }

    public String getIssue() {
        return issue;
    }

    public String getDescription() {
        return description;
    }
}


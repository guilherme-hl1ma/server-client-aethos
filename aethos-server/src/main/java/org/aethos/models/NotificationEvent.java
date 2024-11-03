package org.aethos.models;

import org.aethos.Message;

public class NotificationEvent extends Message {
    private String uuidUserFrom;
    private String uuidUserTo; // ou vai ser o uuidPostTo?
    private boolean like;
    private boolean follow;
    private boolean comment;
    private boolean read;

    public NotificationEvent(String uuidUserFrom,
                             String uuidUserTo,
                             boolean like,
                             boolean follow,
                             boolean comment,
                             boolean read)
    {
        this.uuidUserFrom = uuidUserFrom;
        this.uuidUserTo = uuidUserTo;
        this.like = like;
        this.follow = follow;
        this.comment = comment;
        this.read = read;
    }

    public String getUuidUserFrom() {
        return uuidUserFrom;
    }

    public String getUuidUserTo() {
        return uuidUserTo;
    }

    public boolean isLike() {
        return like;
    }

    public boolean isFollow() {
        return follow;
    }

    public boolean isComment() {
        return comment;
    }

    public boolean isRead() {
        return read;
    }
}

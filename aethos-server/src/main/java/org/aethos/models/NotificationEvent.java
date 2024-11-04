package org.aethos.models;

/**
 * Classe que representa um Evento de Notificação.
 */
public class NotificationEvent extends Message {
    private String uuidUserFrom;
    private String uuidUserTo;
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

    @Override
    public String toString()
    {
        String operationType = "";
        String isRead = "";

        if (isLike())   operationType = "Like";
        else if (isComment())    operationType = "Follow";
        else operationType = "Comment";

        if (isRead())   isRead = "Read";
        else    isRead = "Not Read";

        return String.format(
                "Source User UUID: %s%n" +
                "Destination User UUID: %s%n" +
                "Operation Type: %s%n" +
                "Notification was read: %s%n",
                getUuidUserFrom(), getUuidUserTo(), operationType, isRead
        );
    }

    @Override
    public int hashCode()
    {
        int aux = 11;

        aux = aux * 11 + String.valueOf(uuidUserFrom).hashCode();
        aux = aux * 11 + String.valueOf(uuidUserTo).hashCode();
        aux = aux * 11 + Boolean.valueOf(like).hashCode();
        aux = aux * 11 + Boolean.valueOf(follow).hashCode();
        aux = aux * 11 + Boolean.valueOf(comment).hashCode();
        aux = aux * 11 + Boolean.valueOf(read).hashCode();

        if (aux < 0)    aux = -aux;

        return aux;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)    return true;
        if (obj == null)    return false;
        if (this.getClass() != obj.getClass())  return false;

        NotificationEvent notificationEvent = (NotificationEvent) obj;
        if (!notificationEvent.getUuidUserFrom().equals(this.getUuidUserFrom()))  return false;
        if (!notificationEvent.getUuidUserTo().equals(this.getUuidUserTo()))  return false;
        if (notificationEvent.isLike() != this.isLike())   return false;
        if (notificationEvent.isFollow() != this.isFollow())    return false;
        if (notificationEvent.isComment() != this.isComment())  return false;
        if (notificationEvent.isRead() != this.isRead())    return false;

        return true;
    }
}

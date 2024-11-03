export type NotificationEvent = {
    uuidUserFrom?: string,
    uuidUserTo  ?: string,
    like        ?: boolean,
    follow      ?: boolean,
    comment     ?: boolean,
    read        ?: boolean,
}
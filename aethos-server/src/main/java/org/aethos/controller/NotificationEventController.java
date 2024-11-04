package org.aethos.controller;

import com.google.gson.Gson;
import org.aethos.models.NotificationEvent;
import org.aethos.service.NotificationEventService;

import java.util.HashMap;
import java.util.Map;

/**
 * Classe responsável por receber e processar com os eventos de notificação.
 */
public class NotificationEventController {
    private final NotificationEventService notificationEventService;
    private final Gson gson;

    public NotificationEventController() {
        this.notificationEventService = new NotificationEventService();
        this.gson = new Gson();
    }

    public String handleNotification(String postNotification) {
        NotificationEvent notificationEvent = gson.fromJson(postNotification, NotificationEvent.class);
        boolean result = notificationEventService.postNotification(notificationEvent);
        Map<String, String> responseMap = new HashMap<>();
        if (result) {
            responseMap.put("status", "success");
            responseMap.put("message", "notification recorded successfully");
        } else {
            responseMap.put("status", "error");
            responseMap.put("message", "failed to record notification");
        }
        return gson.toJson(responseMap);
    }
}

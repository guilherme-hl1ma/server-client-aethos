package org.aethos.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import org.aethos.models.NotificationEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Classe responsável por salvar os dados da notificação no Firebase Firestore.
 */
public class NotificationEventService {
    private final Firestore db;

    public NotificationEventService() {
        db = FirestoreClient.getFirestore();
    }

    public boolean postNotification(NotificationEvent notificationEvent) {
        Map<String, Object> docData = new HashMap<>();
        docData.put("uuidFromUser", notificationEvent.getUuidUserFrom());
        docData.put("uuidToUser", notificationEvent.getUuidUserTo());
        docData.put("like", notificationEvent.isLike());
        docData.put("follow", notificationEvent.isFollow());
        docData.put("comment", notificationEvent.isComment());
        docData.put("read", notificationEvent.isRead());

        ApiFuture<WriteResult> result = db.collection("notifications").document().set(docData);

        try {
            result.get();  // Aguarda o resultado
            return true;
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error saving data to Firestore: " + e.getMessage());
            return false;
        }
    }
}
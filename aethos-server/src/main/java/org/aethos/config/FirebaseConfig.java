package org.aethos.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import java.io.FileInputStream;
import java.io.IOException;

public class FirebaseConfig {
    FileInputStream serviceAccount =
            new FileInputStream("src/main/resources/serviceAccountKey.json");

    public FirebaseConfig() throws IOException {
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://aethos-espi-default-rtdb.asia-southeast1.firebasedatabase.app")
                .build();

        FirebaseApp defaultApp = FirebaseApp.initializeApp(options);
    }

}

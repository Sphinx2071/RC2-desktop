package edu.uw.cse.ifrcdemo.setup.ui.common.services;

import org.opendatakit.aggregate.odktables.rest.entity.PrivilegesInfo;
import org.opendatakit.sync.client.SyncClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SyncClientService {
    private final SyncClient syncClient;

    public SyncClientService() {
        this.syncClient = new SyncClient();
    }

    public void initializeServer(String serverUrl, String username, String password) {
        try {
            syncClient.init(serverUrl, username, password);

            // Verify server access
            PrivilegesInfo privInfo = syncClient.getPrivilegesInfo(
                    serverUrl + "/odktables",
                    "default"
            );

            if (privInfo == null) {
                throw new RuntimeException("Failed to verify server access");
            }
        } catch (Exception e) {
            throw new RuntimeException("Server initialization failed", e);
        }
    }
}


/*@Service
public class SyncClientService {
    private final SyncClient syncClient;

    public SyncClientService() {
        this.syncClient = new SyncClient();
        this.syncClient.init();
    }

    public void initializeServer(String hostname, String username, String password) {
        try {
            syncClient.init(hostname, username, password);

            // Verify server access
            PrivilegesInfo privInfo = syncClient.getPrivilegesInfo(
                    hostname + "/odktables",  // Your server URL format
                    "default"                 // Your appId
            );

            if (privInfo == null) {
                throw new ServerInitializationException("Failed to verify server access");
            }
        } catch (Exception e) {
            throw new ServerInitializationException("Server initialization failed", e);
        }
    }

    public class ServerInitializationException extends RuntimeException {
        public ServerInitializationException(String message) {
            super(message);
        }

        public ServerInitializationException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}*/

package mathapp.socket.server.concurrent;

import mathapp.common.Logger;
import mathapp.socket.server.ServerThread;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

class ThreadManager {
    private HashMap<String, ServerThread> threads;

    ThreadManager() {
        this.threads = new HashMap<>();
    }

    void addThread(ServerThread thread) {
        Logger.server(Logger.formatId(thread.getConnection().getId()) + "Starting worker thread");
        thread.start();
        this.threads.put(UUID.randomUUID().toString().toUpperCase(), thread);
    }

    void closeCompleted() {
        for (Entry<String, ServerThread> threadItem : this.threads.entrySet()) {
            try {
                if (!threadItem.getValue().isInterrupted()) {
                    Logger.server(Logger.formatId(threadItem.getValue().getConnection().getId()) + "Ending worker thread");
                    this.threads.remove(threadItem.getKey());
                }
            } catch (Exception ex) {
                Logger.error(ex);
            }
        }
    }
}

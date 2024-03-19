package webSocketServer;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.concurrent.ConcurrentHashMap;
import java.util.ArrayList;
public class GameConnectionManager {
    public final ConcurrentHashMap<Integer, ArrayList<GameConnection>> connections = new ConcurrentHashMap<>();

    public void addConnection(int gid, Session session){
        assert session != null;
        GameConnection connection = new GameConnection(gid, session);
        ArrayList<GameConnection> gameConnections = connections.get(gid); //may return null if gid not in hashmap
        if(gameConnections == null){ //initialize to empty list
            gameConnections = new ArrayList<GameConnection>();
        }
        gameConnections.add(connection); //add connection
        //connections.put(gid, gameConnections); //update gid to updated gameConnections array
    }

    public void removeConnection(int gid, Session session){
        ArrayList<GameConnection> gameConnections = connections.get(gid); //may return null if gid not in hashmap
        gameConnections.remove(session); //reference
    }

    public void broadcast(int gid, Session senderSession, Notification notification) throws IOException {
        var removeList = new ArrayList<GameConnection>(); //person closed their computer
        ArrayList<GameConnection> gameConnections = connections.get(gid);
        //get game, go through its connections, remove dead ones else broadcast if not sender...
        for (GameConnection c: gameConnections) {
            if (c.session.isOpen()) {
                if (!c.session.equals(senderSession)) {
                    c.send(notification.toString());
                }
            } else {
                removeList.add(c);
            }
        }

        // Clean up any connections that were left open.
        for (GameConnection c : removeList) {
            gameConnections.remove(c); //reference to arraylist in hashmap
        }
    }



}

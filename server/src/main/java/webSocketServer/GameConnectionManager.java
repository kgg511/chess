package webSocketServer;
import com.google.gson.Gson;
import exception.ResponseException;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.ServerMessage;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class GameConnectionManager{
    public final ConcurrentHashMap<Integer, Map<String, Session>> connections = new ConcurrentHashMap<>();
    //each game has a dictionary of authToken: Session connection
    public void addConnection(int gid, String authToken, Session session) throws ResponseException {
        assert session != null;
        Map<String, Session> map = null;

        if(!connections.containsKey(gid)){
            map = new HashMap<>();
            map.put(authToken, session);
        }
        else{
            map = connections.get(gid);
            if(map.containsKey(authToken)){
                throw new ResponseException(400, "You cannot join the game twice on the same device.");
            }
            map.put(authToken, session);
        }
        connections.put(gid, map);
    }
    public void removeConnection(int gid, String authToken){ //game completes
        Map<String, Session> map = connections.get(gid);
        if(map != null){
            map.remove(authToken);
        }
    }

    public void removeGameConnections(int gid){
        Map<String, Session> map = connections.get(gid);
        map = new HashMap<>();
    }
    public void broadcast(int gid, Session senderSession, ServerMessage notification) throws IOException {
        var removeList = new ArrayList<String>(); //person closed their computer
        Map<String, Session> gameConnections = connections.get(gid);
        //get game, go through its connections, remove dead ones else broadcast if not sender...

        for (String token: gameConnections.keySet()) {
            Session c = gameConnections.get(token);
            if (c.isOpen()) {
                if (!c.equals(senderSession)) {
                    c.getRemote().sendString(new Gson().toJson(notification));
                }
            } else {
                removeList.add(token);
            }
        }
        // Clean up any connections that were left open.
        for (String c : removeList) {
            gameConnections.remove(c); //remove key for authToken c
        }
    }

    public void sendToSession(Session session, ServerMessage notification) throws IOException{
        //send notification to only the specified session
        try{session.getRemote().sendString(new Gson().toJson(notification));}
        catch(Exception e){
            e.printStackTrace();
        }
    }

}

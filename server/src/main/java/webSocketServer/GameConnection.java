package webSocketServer;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import model.GameData;
public class GameConnection { //store the players in the game, the observers
    public int gid; //for each player in each game there is an additional websocket connection
    public Session session;

    public GameConnection(int gid, Session session) {
        this.gid = gid;
        this.session = session;
    }
    public void send(String msg) throws IOException {
        session.getRemote().sendString(msg);
    }
}

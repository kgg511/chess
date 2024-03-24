package clientStuff;

import clientStuff.webSocketClient.WebSocketCommunicator;

public interface ChessClientInterface {

    public WebSocketCommunicator getWS();
    public int getGameID();
    public State getState();
    public String eval(String input);

    public String help();

    public void setColor();
    public ServerFacade getFacade();

}

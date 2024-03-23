package clientStuff;

public interface ChessClientInterface {
    public int getGameID();
    public State getState();
    public String eval(String input);

    public String help();

    public void setColor();
    public ServerFacade getFacade();

}

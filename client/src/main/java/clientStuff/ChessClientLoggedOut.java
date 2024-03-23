package clientStuff;

import chess.ChessBoard;
import exception.ResponseException;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.ArrayList;
import model.*;
import ui.DrawChessBoard;

import static ui.EscapeSequences.*;

public class ChessClientLoggedOut implements ChessClientInterface{
    public final ServerFacade server;
    private final String serverUrl;
    private State state = State.SIGNEDOUT;
    public ChessClientLoggedOut(int port, String host, ServerFacade f) {
        this.serverUrl = host + ":" + port;
        if(f != null){server = f;}
        else{server = new ServerFacade(port, host);}
    }
    public State getState(){ return this.state;}
    public ServerFacade getFacade(){return this.server;}
    @Override
    public int getGameID() {return -1;} //GameID is irrelevant

    public String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "help"-> help();
                case "quit" -> "quit";
                case "register" -> register(params);
                case "login" -> login(params);
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }
    public String help() {
        System.out.println(SET_TEXT_COLOR_BLUE);
        return """
                register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                login <USERNAME> <PASSWORD> - to play chess
                quit - exit
                help - with possible commands
                """;
    }
    //register <USERNAME> <PASSWORD> <EMAIL> - to create an account
    public String register(String... params) throws ResponseException{
        if(params.length >= 3){ //cmd, username, password, email
            Response.RegisterResponse response = server.register(params[0], params[1], params[2]);
            state = State.SIGNEDIN;
            return String.format("You registered as %s.", response.username());
        }
        throw new ResponseException(400, "Expected: <USERNAME> <PASSWORD> <EMAIL>");
    }
    public String login(String... params) throws ResponseException{
        if(params.length >= 2){ //cmd, username, password
            Response.LoginResponse response = server.login(params[0], params[1]);
            state = State.SIGNEDIN;
            return String.format("You signed in as %s.", response.username());
        }
        throw new ResponseException(400, "Expected: <USERNAME> <PASSWORD>");
    }

    public void setColor(){
        System.out.print(SET_TEXT_COLOR_LIGHT_GREY);
    }

}

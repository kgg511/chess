package clientStuff;

import java.util.Scanner;
import static ui.EscapeSequences.*;
public class Repl {
    private ChessClientInterface client;
    private final int port;
    private final String host;
    public Repl(int port, String host) {
        this.port = port;
        this.host = host;
        client = new ChessClientLoggedOut(port, host, null);
    }

    public void run(){
        System.out.println(SET_TEXT_COLOR_LIGHT_GREY + "Welcome to 240 chess. Type Help to get started.");
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine(); //get line
            try {
                result = client.eval(line); //evalute
                System.out.print(result);
                client.setColor();

                if(client.getState() == State.SIGNEDOUT && client.getClass() != ChessClientLoggedOut.class){
                    client = new ChessClientLoggedOut(port, host, client.getFacade());
                    client.setColor();
                }
                else if(client.getState() == State.SIGNEDIN && client.getClass() != ChessClientLoggedIn.class){
                    client = new ChessClientLoggedIn(port, host, client.getFacade());
                    client.setColor();
                }
                else if(client.getState() == State.GAME && client.getClass() != ChessClientGame.class){
                    client = new ChessClientGame(port, host, client.getFacade());
                    client.setColor();
                }

            } catch (Throwable e) {
                System.out.print(e.getMessage());
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        System.out.print("\n" + "[" + client.getState() + "]" + ">>> ");
    }
}

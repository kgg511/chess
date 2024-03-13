package clientStuff;

import clientStuff.ChessClient;

import java.util.Scanner;
import static ui.EscapeSequences.*;
public class Repl {

    //actual console stuff
    private final ChessClient client;

    public Repl(int port, String host) {
        client = new ChessClient(port, host);
    }
    //serverUrl, this

    public void run(){
        System.out.println("Welcome to 240 chess. Type Help to get started.");

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine(); //get line
            try {
                result = client.eval(line); //evalute
                System.out.print(SET_TEXT_COLOR_BLUE + result);
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

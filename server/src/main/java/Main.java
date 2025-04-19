import chess.*;
import server.Server;

public class Main {
    public static void main(String[] args) {
        new Server().run(8080);
        System.out.println("server running");
    }
}
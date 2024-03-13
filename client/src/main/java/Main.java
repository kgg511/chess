import clientStuff.Repl;

public class Main {
    public static void main(String[] args) {
        int port = 8080;
        String hostname = "http://localhost";
        if (args.length == 2) {
            hostname = "http://" + args[0];
            port = Integer.parseInt(args[1]);
        }

        new Repl(port, hostname).run();
    }

}
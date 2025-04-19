import clientStuff.Repl;

public class Main {
    public static void main(String[] args) {
        System.out.println("client running");
        int port = 8080; //default stuff
        String hostPort;
        String hostname = "http://localhost";
        if (args.length > 0) {
            hostPort = args[0];
            String[] parts = hostPort.split(":");
            hostname = parts[0];
            port = Integer.parseInt(parts[1]);
        }

        new Repl(port, hostname).run();
    }

}
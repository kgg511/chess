package clientStuff;

import Request.JoinGameRequest;
import Response.*;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import model.*;
import exception.ResponseException;
public class ServerFacade {
    private final String serverURL;
    private String authToken = "";

    public ServerFacade(int port, String url) {
        serverURL = url + ":" + port;
    }
    public String getAuthToken(){return authToken;}
    //register: returns username and authToken as responseObject
    public RegisterResponse register(String username, String password, String email) throws ResponseException{
        String path = "/user";
        UserData user = new UserData(username, password, email);
        RegisterResponse r = this.makeRequest("POST", path, "", user, RegisterResponse.class);
        authToken = r.authToken();
        return r;
    }

    public LoginResponse login(String username, String password) throws ResponseException{
        String path = "/session";
        UserData user = new UserData(username, password, "");
        LoginResponse r = this.makeRequest("POST", path, "", user, LoginResponse.class);
        authToken = r.authToken();
        return r;
    }
    public LogoutResponse logout() throws ResponseException{
        String path = "/session";
        LogoutResponse r = this.makeRequest("DELETE", path, authToken, null, LogoutResponse.class);
        authToken = "";
        return r;
    }
    public JoinGameResponse joinGame(String playerColor, int gameID) throws ResponseException{
        String path = "/game";
        JoinGameRequest request = new JoinGameRequest(playerColor, gameID);
        JoinGameResponse r = this.makeRequest("PUT", path, authToken, request, JoinGameResponse.class);
        return r;
    }
    public CreateGameResponse createGame(String gameName) throws ResponseException{
        String path = "/game";
        //chessgame is actually created on server side
        GameData game = new GameData(-1, "", "", gameName, null);
        CreateGameResponse r = this.makeRequest("POST", path, authToken, game, CreateGameResponse.class);
        return r;
    }
    public ArrayList<GameData> listGames() throws ResponseException {
        String path = "/game";
        var response = this.makeRequest("GET", path, authToken, null, ListGamesResponse.class);
        return response.games();
    }
    private <T> T makeRequest(String method, String path, String header, Object request, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverURL + path)).toURL(); // + path
            HttpURLConnection http = (HttpURLConnection) url.openConnection();

            if(header != ""){
                http.setRequestProperty("authorization", header);
            }
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeBody(request, http);
            http.connect(); //this send the request?
            throwIfNotSuccessful(http);
            return readBody(http, responseClass); //read the response
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }
    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }
    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }
    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (status != 200) {
            throw new ResponseException(status, "failure: " + status);
        }
    }

}

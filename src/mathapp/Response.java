package mathapp;

import mathapp.Logger;

public class Response {
    private String type;
    private String message;

    public Response(String type, String message) {
        this.type = type;
        this.message = message;

        switch (type) {
            case "ERROR":
                Logger.error(message);
                break;
            case "RESULT":
                Logger.client("Result: " + message);
                break;
            case "SERVER":
            default:
                Logger.server(message);
                break;
        }
    }

    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public static Response fromString(String data) throws Exception {
        try {
            String[] responseElements = data.split("-");
            return new Response(responseElements[0], responseElements[1]);
        } catch (Exception ex) {
            throw new Exception("Invalid response from server");
        }
    }
}

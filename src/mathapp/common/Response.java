package mathapp.common;

// this class is concerned with logging, fromString determines which type of log should
// be printed depending on the response from the server

public class Response {
    private ResponseType type;

    private Response(String type, String message) {
        switch (type) {
            case "ERROR":
                Logger.error(message);
                this.type = ResponseType.ERROR;
                break;
            case "RESULT":
                Logger.server("Result: " + message);
                this.type = ResponseType.RESULT;
                break;
            case "MESSAGE":
            default:
                Logger.server(message);
                this.type = ResponseType.MESSAGE;
                break;
        }
    }

    public ResponseType getType() {
        return type;
    }

    public static Response fromString(String data) throws Exception {
        try {
            String[] responseElements = data.split("#");
            return new Response(responseElements[0], responseElements[1]);
        } catch (Exception ex) {
            throw new Exception("Invalid response from server");
        }
    }
}

package mathapp.socket;

import mathapp.common.Colors;
import mathapp.common.Logger;
import mathapp.common.ResponseType;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

// used by both client and server, wraps a java.net.Socket object and adds send()
// and receive() methods for communication

public class IOSocket {
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;

    public IOSocket(Socket socket) throws IOException {
        this.socket = socket;
        this.initialise();
    }

    public void close() {
        try {
            this.socket.close();
        } catch (Exception ex) {
            Logger.error(ex);
        }
    }

    private void initialise() throws IOException {
        // get an input stream for reading character-mode input (BufferedReader)
        this.input = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));

        // get an output stream  for writing character-mode output (PrintWriter)
        this.output = new PrintWriter(new OutputStreamWriter(this.socket.getOutputStream()));
    }

    public String getIpAddress() {
        return Colors.ANSI_GREEN + this.socket.getInetAddress().toString().replace('/', ' ').trim() + ":" + this.socket.getPort() + Colors.ANSI_RESET;
    }

    public void send(String message) throws IOException {
        output.println(message);
        //The ensuing flush method call is necessary for the data to
        // be written to the socket data stream before the socket is closed.
        output.flush();
    }

    // sends a message across the socket
    public void send(ResponseType type, String message) throws IOException {
        this.send(String.join("-", type.name(), message));
    }

    // receives a message across the socket
    public String receive() throws IOException {
        // read a line from the data stream
        return input.readLine();
    }
}

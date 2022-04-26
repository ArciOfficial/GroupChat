package Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	
	public static final String TERM = "\r\n";
	public static final String JOIN = "JOINX ";
    public static final String LEAVE = "LEAVE ";
    public static final String MSG = "MESSG ";
    public static final String GROUP = "GROUP ";
    public static final String IMAGE = "IMAGE ";
    public static final int CMDLEN = 6;
    public static final int BUFSIZE = 1024;
	
	private ServerSocket serverSocket;
	
	public Server(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}
	
	public void startServer() {
        try {
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                System.out.println("A new client has connected!");
                ClientHandler clientHandler = new ClientHandler(socket);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException e) {
            closeServerSocket();
        }
    }
	
    public void closeServerSocket() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1111);
        Server server = new Server(serverSocket);
        server.startServer();
    }
	
}

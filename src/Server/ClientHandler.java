package Server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();

    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;
    private String clientUsername;

    public ClientHandler(Socket socket) {
    	String joinMessage;
    	String usersList = "GROUP";
    	
        try {
            this.socket = socket;
            this.dis = new DataInputStream(socket.getInputStream());
            this.dos = new DataOutputStream(socket.getOutputStream());
            joinMessage = dis.readUTF();
            clientUsername = joinMessage.replace(Server.JOIN, "");
            clientUsername = clientUsername.replace(Server.TERM, "");
            clientHandlers.add(this);
            
            for (ClientHandler clientHandler : clientHandlers) {
            	if(!clientHandler.clientUsername.equals(clientUsername)) {
            		usersList += " " + clientHandler.clientUsername;
            	}
            }
            usersList += Server.TERM;
            dos.writeUTF(usersList);
            broadcastMessage(joinMessage);
        } catch (IOException e) {
            closeEverything(socket, dis, dos);
        }
    }

    @Override
    public void run() {
        String msgFromClient;

        while (socket.isConnected()) {
        	try {
                msgFromClient = dis.readUTF();
                
                String cmd = msgFromClient.substring(0, Server.CMDLEN);
                String msg = msgFromClient.replace(cmd, "");
                
                switch(cmd) {
                	case Server.MSG:
                		msgFromClient = msgFromClient.replace(msg, clientUsername + ": " + msg);
                		broadcastMessage(msgFromClient);
                		break;
                	case Server.LEAVE:
                		removeClientHandler();
                }
            } catch (IOException e) {
                closeEverything(socket, dis, dos);
            }
        }
    }

    public void broadcastMessage(String messageToSend) {
        for (ClientHandler clientHandler : clientHandlers) {
            try {
                if (!clientHandler.clientUsername.equals(clientUsername)) {
                    clientHandler.dos.writeUTF(messageToSend);
                }
            } catch (IOException e) {
                closeEverything(socket, dis, dos);
            }
        }
    }

    public void removeClientHandler() {
        clientHandlers.remove(this);
        broadcastMessage(Server.LEAVE + clientUsername + Server.TERM);
    }

    public void closeEverything(Socket socket, DataInputStream dis, DataOutputStream dos) {

        removeClientHandler();
        try {
            if (dis != null) {
                dis.close();
            }
            if (dos != null) {
                dos.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

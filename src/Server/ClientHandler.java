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
            System.out.println("Username:" + clientUsername);
            clientUsername = clientUsername.replace(Server.TERM, "");
            if(checkName(clientUsername)) {
            	return;
            }
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
                
                String imgCmd = msgFromClient.substring(0, Server.CMDLEN - 1);
                
                if(imgCmd.equals(Server.IMAGE)) {
                	int length = dis.readInt();
            		byte[] imageBytes = dis.readNBytes(length);
            		broadcastMessage(Server.IMGNM + clientUsername + Server.TERM);
            		broadcastMessage(Server.IMAGE);
            		broadcastLength(length);
            		broadcastBytes(imageBytes);
                }
                else
                {
                	String cmd = msgFromClient.substring(0, Server.CMDLEN);
                    String msg = msgFromClient.replace(cmd, "");
                	
                	switch(cmd) {
	                	case Server.MSG:
	                		msgFromClient = Server.MSG + clientUsername + msg + Server.TERM;
	                		broadcastMessage(msgFromClient);
	                		break;
	                	case Server.LEAVE:
	                		removeClientHandler();
                	}
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
    
    public void broadcastLength(int length) {
    	for (ClientHandler clientHandler : clientHandlers) {
            try {
                if (!clientHandler.clientUsername.equals(clientUsername)) {
                    clientHandler.dos.writeInt(length);
                }
            } catch (IOException e) {
                closeEverything(socket, dis, dos);
            }
        }
    }
    
    public void broadcastBytes(byte[] arr) {
    	for (ClientHandler clientHandler : clientHandlers) {
            try {
                if (!clientHandler.clientUsername.equals(clientUsername)) {
                	for(int i = 0; i < arr.length; i++) {
                		dos.writeByte(arr[i]);
                	}
                }
            } catch (IOException e) {
                closeEverything(socket, dis, dos);
            }
        }
    }
    
    public boolean checkName(String username) throws IOException {
    	for (ClientHandler clientHandler : clientHandlers) {
        	if(username.equals(clientHandler.clientUsername)) {
        		dos.writeUTF(Server.BDNME);
        		closeEverything(socket, dis, dos);
        		return true;
        	}
        }
		return false;
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

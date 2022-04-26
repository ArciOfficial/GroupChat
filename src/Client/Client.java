package Client;

import java.io.*;
import java.net.Socket;

public class Client {
	
	public static final String TERM = "\r\n";
	public static final String JOIN = "JOINX ";
    public static final String LEAVE = "LEAVE ";
    public static final String MSG = "MESSG ";
    public static final String GROUP = "GROUP ";
    public static final String IMAGE = "IMAGE ";
    public static final int CMDLEN = 6;
    public static final int BUFSIZE = 1024;

    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;
    private String username;
    private static ChatGUI gui;

    public Client(Socket socket) {
        try {
            this.socket = socket;
            this.dis = new DataInputStream(socket.getInputStream());
            this.dos = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            closeEverything();
        }
    }

    public void sendMessage(String message) {
        try {
            if (socket.isConnected()) {
                dos.writeUTF(message);
            }
        } catch (IOException e) {
            closeEverything();
        }
    }

    public void listenForMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msgFromGroupChat;

                while (socket.isConnected()) {
                    try {
                        msgFromGroupChat = dis.readUTF();
                        
                        String cmd = msgFromGroupChat.substring(0, CMDLEN);
                        String msg = msgFromGroupChat.replace(cmd, "");
                        
                        switch(cmd) {
                        	case JOIN:
                        		String name = msg.replace(TERM, "");
                        		gui.users.addUser(name);
                        		gui.chatPanel.showMessage(name + " has joined the chat." + TERM);
                        		break;
                        	case MSG:
                        		gui.chatPanel.showMessage(msg);
                        		break;
                        	case GROUP:
                        		String users = msg.replace(TERM, "");
                        		gui.users.addGroupUsers(users);
                        		break;
                        	case LEAVE:
                        		String user = msg.replace(TERM, "");
                        		gui.chatPanel.showMessage(user + " has left the chat." + TERM);
                        		gui.users.removeUser(user);
                        		closeEverything();
                        }
                    } catch (IOException e) {
                        closeEverything();
                    }
                }
            }
        }).start();
    }

    public void closeEverything() {

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
    
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

    public static void main(String[] args) throws IOException {

        Socket socket = new Socket("localhost", 1111);

        Client client = new Client(socket);
        gui = new ChatGUI(client);

        client.listenForMessage();
    }
    
}

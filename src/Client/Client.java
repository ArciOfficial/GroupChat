package Client;

import java.io.*;
import java.nio.ByteBuffer;
import java.net.Socket;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import Server.Server;

public class Client {
	
	public static final String TERM = "\r\n";
	public static final String JOIN = "JOINX ";
    public static final String LEAVE = "LEAVE ";
    public static final String MSG = "MESSG ";
    public static final String GROUP = "GROUP ";
    public static final String IMAGE = "IMAGE";
    public static final String IMGNM = "IMGNM ";
    public static final String BDNME = "BDNME ";
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
    
    public void sendImage(byte[] arr) throws IOException {
    	String imgName = IMGNM + " " + getUsername() + TERM;
    	sendMessage(imgName);
    	sendMessage(IMAGE);
    	dos.writeInt(arr.length);
    	for(int i = 0; i < arr.length; i++) {
    		dos.writeByte(arr[i]);
    	}
    	
    }

    public void listenForMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msgFromGroupChat;
                String cmd;
                String msg;
                String name;
                String users;
                String imgName;
                String user;

                while (socket.isConnected()) {
                    try {
                        msgFromGroupChat = dis.readUTF();
                        System.out.println(msgFromGroupChat);
                        System.out.println("Length: " + msgFromGroupChat.length());
                        
                        cmd = msgFromGroupChat.substring(0, CMDLEN);
                        msg = msgFromGroupChat.replace(cmd, "");
                        
                        switch(cmd) {
                        	case JOIN:
                        		name = msg.replace(TERM, "");
                        		gui.users.addUser(name, gui.getDefaultIcon());
                        		gui.chatPanel.showMessage(name + " has joined the chat." + TERM);
                        		break;
                        	case MSG:
                        		gui.chatPanel.showMessage(msg);
                        		break;
                        	case GROUP:
                        		users = msg.replace(TERM, "");
                        		gui.users.addGroupUsers(users, gui.getDefaultIcon());
                        		break;
                        	case IMGNM:
                        		System.out.println("SUCESS");
                        		imgName = msg.replace(TERM, "");
                        		byte[] cmdB = new byte[BUFSIZE];
                        		dis.read(cmdB, 0, CMDLEN);
                        		String imgCmd = new String(cmdB);
                        		if(imgCmd != Server.IMAGE) {
                        			continue;
                        		}
                        		int length = dis.readInt();
                        		System.out.println("IMGLEN:"+length);
                        		byte[] imageBytes = new byte[BUFSIZE];
                        		dis.read(imageBytes, 0, length);
                        		gui.addImage(imgName, imageBytes);
                        		break;
                        	case LEAVE:
                        		user = msg.replace(TERM, "");
                        		gui.chatPanel.showMessage(user + " has left the chat." + TERM);
                        		gui.users.removeUser(user);
                        		closeEverything();
                        		break;
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

        Socket socket = new Socket("localhost", 1131);

        Client client = new Client(socket);
        gui = new ChatGUI(client);

        client.listenForMessage();
    }
    
}

package Client;

import java.awt.BorderLayout;

import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;

public class ChatGUI implements ActionListener
{
	static final String EXIT = "exit";
	static final String IMAGE = "image";
	Client sender;
	ChatUsersList users;
	ChatPanel chatPanel;
	ImageIcon defaultIcon;
	
	JFrame mainChat;
	String userName;

	public ChatGUI( Client sender ) throws IOException 
	{
	
		this.sender = sender;
		
		defaultIcon = new ImageIcon(ImageIO.read(User.class.getResourceAsStream( "user_icon.png" ) ));
		JFrame chatWindow = new JFrame();
		JPanel chatMainPanel = new JPanel( new BorderLayout() );
		chatWindow.add( chatMainPanel );
		
		JPanel centerPanel = new JPanel( new FlowLayout() );
		chatPanel = new ChatPanel( sender );
		users = new ChatUsersList( sender );
		centerPanel.add( users );
		centerPanel.add( chatPanel );
		
		JPanel buttons = new JPanel();
		JButton imageButton = new JButton( "Change Image" );
		buttons.add( imageButton );
		imageButton.setActionCommand( IMAGE );
		imageButton.addActionListener(this);
		JButton exitButton = new JButton( "Exit" );
		buttons.add( exitButton );
		exitButton.setActionCommand( EXIT );
		exitButton.addActionListener(this);
		
		chatMainPanel.add( centerPanel, BorderLayout.CENTER );
		chatMainPanel.add( buttons, BorderLayout.SOUTH );
		
		chatWindow.pack();
		
		userName = JOptionPane.showInputDialog( chatWindow, "Please enter the name you use for chatting (no spaces)",
				"Enter Chat Alias",
				JOptionPane.QUESTION_MESSAGE );
		sender.sendMessage( Client.JOIN + userName + Client.TERM);
		sender.setUsername(userName);
		users.addUser(userName, getDefaultIcon());
		
		chatWindow.setVisible( true );
		
	}
	
	public void actionPerformed( ActionEvent e )
	{
		if ( EXIT.equals(e.getActionCommand() ) )
		{
			sender.sendMessage(Client.LEAVE);
			sender.closeEverything();
			System.exit(0);
		}
		 else if ( IMAGE.equals(e.getActionCommand() ) )
		{
			 JFileChooser fileChooser = new JFileChooser();
			 FileNameExtensionFilter filter = new FileNameExtensionFilter(".png", "png");
			 fileChooser.setFileFilter(filter);
			 
			 int retVal = fileChooser.showOpenDialog(fileChooser);
			 
			 if(retVal == JFileChooser.APPROVE_OPTION){
				 
				 File img = fileChooser.getSelectedFile();
				 long imgSize = img.length();
				 String aux = "";
				 String imgStr = "";
				 
				 if(img != null) {
					 try {
						 FileReader fileReader = new FileReader(img);
					     BufferedReader bufferedReader = new BufferedReader(fileReader);
					     
					     byte[] arr = Files.readAllBytes(img.toPath());
					     
					     bufferedReader.close();
					 	 sender.sendImage(arr);
					 	 addImage(sender.getUsername(), arr);
					 } catch(Exception ex) {
						 System.out.println("Error");
					 }

				 }
			 }
			 
			 
		}
			
	}
	

	public void addImage(String username, byte[] arr) 
	{
		
		ImageIcon imageIcon = new ImageIcon(arr);
//		Image image = imageIcon.getImage();
//		Image newimg = image.getScaledInstance(24, 24, java.awt.Image.SCALE_SMOOTH);
//		imageIcon = new ImageIcon(newimg);
		
		int[] selectedIx = users.getSelectedIndices();
		
		for(int i = 0; i < selectedIx.length; i++) {
			User u = users.listModel.getElementAt(selectedIx[i]);
			
			if(u.username == username) {
				u.imageIcon = imageIcon;
				break;
			}
		}
		
		
	}
	
	public ImageIcon getDefaultIcon() throws IOException 
	{
		return defaultIcon;
	}
	
	public ChatUsersList getUsersList()
	{
		return users;
	}
	
	public ChatPanel getChatPanel()
	{
		return chatPanel;
	}
	
}
package Client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
//import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ChatGUI implements ActionListener
{
	static final String EXIT = "exit";
	static final String PRIVATE = "private";
	Client sender;
	ChatUsersList users;
	ChatPanel chatPanel;
	
	JFrame mainChat;
	String userName;

	public ChatGUI( Client sender ) 
	{
	
		this.sender = sender;
		
		JFrame chatWindow = new JFrame();
		JPanel chatMainPanel = new JPanel( new BorderLayout() );
		chatWindow.add( chatMainPanel );
		
		JPanel centerPanel = new JPanel( new FlowLayout() );
		chatPanel = new ChatPanel( sender );
		users = new ChatUsersList( sender );
		centerPanel.add( users );
		centerPanel.add( chatPanel );
		
		JPanel buttons = new JPanel();
		JButton privateButton = new JButton( "Private Message" );
		buttons.add( privateButton );
		privateButton.setActionCommand( PRIVATE );
		privateButton.addActionListener(this);
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
		
		chatWindow.setVisible( true );
		
	}
	
	public void actionPerformed( ActionEvent e )
	{
		if ( EXIT.equals(e.getActionCommand() ) )
		{
			// Tell the server you are leaving
			sender.sendMessage(Client.LEAVE + Client.TERM);
			System.exit(0);
		}
		/* else if ( PRIVATE.equals(e.getActionCommand() ) )
		{
			// Find out which user is selected
			String name = users.getSelectedUser();
			
			// Launch a private message dialog window for this user
			if ( name != null )
			{
				users.messageUser( name );
			}
			// error message is no one is selected or control button usuability
		} */
			
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
package Client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class ChatPanel extends JPanel implements ActionListener
{
	Client sender;
	String partnerName = null;
	JTextField msgBox;
	JTextArea chatArea;

	public ChatPanel( Client sender ) 
	{
		super( new BorderLayout() );
		this.sender = sender;
		createGUI();
		
	}
	
	public ChatPanel( Client sender, String user ) 
	{
		super( new BorderLayout() );
		this.sender = sender;
		this.partnerName = new String( user );
		createGUI();
		
	}
	
	public void createGUI()
	{
		chatArea = new JTextArea(20, 40);
		JScrollPane scrollPane = new JScrollPane( chatArea ); 
		chatArea.setEditable(false);
		chatArea.setText( "" );
		add( scrollPane, BorderLayout.CENTER );
		
		JPanel sendPanel = new JPanel( new FlowLayout() );
		
		msgBox = new JTextField( 60 );
		msgBox.addActionListener(this);
		sendPanel.add( msgBox );
		JButton sendButton = new JButton( "Send" );
		sendButton.addActionListener(this);
		sendPanel.add( sendButton );
		add( sendPanel, BorderLayout.SOUTH );
		
		this.validate();
	}
	
	
	public void showMessage( String message )
	{
		chatArea.append( message );
	}
	
	public void actionPerformed( ActionEvent e )
	{
		// IF return was pressed in text field OR button was pressed
		// THEN send the message
		String msg = msgBox.getText();
		if ( partnerName == null )
		{
			// This is a message for all - no private partner
			sender.sendMessage( Client.MSG + " " + msg + Client.TERM );
			showMessage(msg + Client.TERM);
			msgBox.setText( "" );
		}
			
	}

}

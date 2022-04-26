package Client;

import java.util.StringTokenizer;

import javax.swing.DefaultListModel;
import javax.swing.JList;

@SuppressWarnings({ "serial", "rawtypes" })
public class ChatUsersList extends JList{
	
	DefaultListModel<String> listModel;
	
	@SuppressWarnings("unchecked")
	public ChatUsersList(Client client) {
		listModel = new DefaultListModel<>();
		this.setModel(listModel);
	}
	
	public void addUser(String user) {
		listModel.addElement(user);
	}
	
	public void removeUser(String user) {
		listModel.removeElement(user);
	}
	
	public void addGroupUsers(String users) {
		StringTokenizer st = new StringTokenizer(users);
		
		while(st.hasMoreTokens()) {
			listModel.addElement(st.nextToken());
		}
	}
	
}

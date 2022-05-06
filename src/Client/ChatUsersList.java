package Client;

import java.util.StringTokenizer;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JList;

@SuppressWarnings({ "serial", "rawtypes" })
public class ChatUsersList extends JList{
	
	DefaultListModel<User> listModel;
	UsersListRenderer renderer;
	
	@SuppressWarnings("unchecked")
	public ChatUsersList(Client client) {
		listModel = new DefaultListModel();
		renderer = new UsersListRenderer();
		this.setModel(listModel);
		this.setCellRenderer(renderer);
	}
	
	public void addUser(String username, ImageIcon imageIcon) {
		listModel.addElement(new User(username, imageIcon));
	}
	
	public void removeUser(String username) {
		listModel.removeElement(username);
	}
	
	public void addGroupUsers(String users, ImageIcon imageIcon) {
		StringTokenizer st = new StringTokenizer(users);
		
		while(st.hasMoreTokens()) {
			listModel.addElement(new User(st.nextToken(), imageIcon));
		}
	}
	
}

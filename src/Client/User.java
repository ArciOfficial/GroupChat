package Client;

import javax.swing.ImageIcon;

public class User {
	
	public String username;
	public ImageIcon imageIcon;
	
	public User(String username, ImageIcon imageIcon) {
		this.username = username;
		this.imageIcon = imageIcon;
	}
	
	@Override
	public String toString() {
		return username;
	}
}

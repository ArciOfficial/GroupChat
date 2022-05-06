package Client;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

@SuppressWarnings("serial")
public class UsersListRenderer extends JLabel implements ListCellRenderer<User>{
	
	@Override
    public Component getListCellRendererComponent(JList<? extends User> list, User user, int index,
        boolean isSelected, boolean cellHasFocus) {
         
        this.setIcon(user.imageIcon);
        this.setText(user.username);
         
        return this;
    }
	
}

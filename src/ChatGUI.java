import javax.swing.*;

public class ChatGUI {
    private JPanel mainPanel;
    private JTextArea taChat;

    public void addMessage(String msg) {
        taChat.append(msg + System.lineSeparator());
    }

}

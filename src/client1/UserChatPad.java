package client1;



import java.awt.BorderLayout;
import java.awt.Panel;
import java.awt.Rectangle;
import java.awt.TextArea;

import javax.swing.JPanel;
import javax.swing.JTextArea;

//�û��������
public class UserChatPad extends JPanel
{
	public JTextArea chatTextArea = new JTextArea("��������", 10, 20);
	public UserChatPad()
	{
		setLayout(new BorderLayout());
		chatTextArea.setAutoscrolls(true);
		chatTextArea.setLineWrap(true);
		add(chatTextArea, BorderLayout.CENTER);
	}
}

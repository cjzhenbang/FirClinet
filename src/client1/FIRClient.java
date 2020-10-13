package client1;





import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JFrame;



// ������ͻ���
public class FIRClient extends JFrame implements ActionListener, KeyListener
{
	
	Socket clientSocket;   // �ͻ����׽ӿ�
	
	DataInputStream inputStream;  // ����������
	
	DataOutputStream outputStream;  // ���������
	
	String chessClientName = null;  // �û���

	String host = null;	   // ������ַ
	
	int port = 4331;   // �����˿�
	
	boolean isOnChat = false;   // �Ƿ�������
    
	boolean isOnChess = false;   // �Ƿ�������
	
	boolean isGameConnected = false;   // ��Ϸ�Ƿ������
	
	boolean isCreator = false;    // �Ƿ�Ϊ��Ϸ������
	
	boolean isParticipant = false;   // �Ƿ�Ϊ��Ϸ������
	
	UserListPad userListPad = new UserListPad();   // �û��б���
	
	UserChatPad userChatPad = new UserChatPad();   // �û�������
	
	UserControlPad userControlPad = new UserControlPad();   // �û�������

	UserInputPad userInputPad = new UserInputPad();  	// �û�������

	FIRPad firPad = new FIRPad();  	// ������
	// �����
	Panel southPanel = new Panel();
	Panel northPanel = new Panel();
	Panel centerPanel = new Panel();
	Panel eastPanel = new Panel();

	// ���췽������������
	public FIRClient()
	{
		super("Java ������ͻ���");
		setLayout(new BorderLayout());
		host = userControlPad.ipInputted.getText();
		
		eastPanel.setLayout(new BorderLayout());
		eastPanel.add(userListPad, BorderLayout.NORTH);
		eastPanel.add(userChatPad, BorderLayout.CENTER);
		eastPanel.setBackground(Color.LIGHT_GRAY);
		
		userInputPad.contentInputted.addKeyListener(this);

		firPad.host = userControlPad.ipInputted.getText();
		centerPanel.add(firPad, BorderLayout.CENTER);
		centerPanel.add(userInputPad, BorderLayout.SOUTH);
		centerPanel.setBackground(Color.LIGHT_GRAY);
		userControlPad.connectButton.addActionListener(this);
		userControlPad.createButton.addActionListener(this);
		userControlPad.joinButton.addActionListener(this);
		userControlPad.cancelButton.addActionListener(this);
		userControlPad.exitButton.addActionListener(this);
		userControlPad.createButton.setEnabled(false);
		userControlPad.joinButton.setEnabled(false);
		userControlPad.cancelButton.setEnabled(false);
		
		southPanel.add(userControlPad, BorderLayout.CENTER);
		southPanel.setBackground(Color.LIGHT_GRAY);

		addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				if (isOnChat)  // ������
				{ 
					try
					{ 
						clientSocket.close();   // �رտͻ����׽ӿ�
					}
					catch (Exception ed){}
				}
				if (isOnChess || isGameConnected)  // ������
				{ 
					try
					{   
						firPad.chessSocket.close();  // �ر�����˿�
					}
					catch (Exception ee){}
				}
				System.exit(0);
			}
		});

		add(eastPanel, BorderLayout.EAST);
		add(centerPanel, BorderLayout.CENTER);
		add(southPanel, BorderLayout.SOUTH);
		pack();
		setSize(670, 560);
		setVisible(true);
		setResizable(false);
		this.validate();
	}

	// ��ָ����IP��ַ�Ͷ˿����ӵ�������
	public boolean connectToServer(String serverIP, int serverPort) throws Exception
	{
		try
		{
			
			clientSocket = new Socket(serverIP, serverPort);  // �����ͻ����׽ӿ�
			
			inputStream = new DataInputStream(clientSocket.getInputStream());  // ����������
			
			outputStream = new DataOutputStream(clientSocket.getOutputStream());  // ���������
			
			FIRClientThread clientthread = new FIRClientThread(this);  // �����ͻ����߳�
			
			clientthread.start();  // �����̣߳��ȴ�������Ϣ
			isOnChat = true;
			return true;
		}
		catch (IOException ex)
		{
			userChatPad.chatTextArea.setText("��������!\n");
		}
		return false;
	}

	// �ͻ����¼�����
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == userControlPad.connectButton)  // ���ӵ�������ť�����¼�
		{ 
			host = firPad.host = userControlPad.ipInputted.getText(); // ȡ��������ַ
			try
			{
				if (connectToServer(host, port))   // �ɹ����ӵ�����ʱ�����ÿͻ�����Ӧ�Ľ���״̬
				{  
					userChatPad.chatTextArea.setText("");
					userControlPad.connectButton.setEnabled(false);
					userControlPad.createButton.setEnabled(true);
					userControlPad.joinButton.setEnabled(true);
					firPad.statusText.setText("���ӳɹ�����ȴ�!");
				}
			}
			catch (Exception ei)
			{
				userChatPad.chatTextArea
						.setText("��������!\n");
			}
		}
		if (e.getSource() == userControlPad.exitButton)   // �뿪��Ϸ��ť�����¼�
		{ 
			if (isOnChat)   // ���û���������״̬��
			{ 
				try
				{
					clientSocket.close();   // �رտͻ����׽ӿ�
				}
				catch (Exception ed){}
			}
			if (isOnChess || isGameConnected) // ���û�������Ϸ״̬��
			{
				try
				{
					firPad.chessSocket.close();  // �ر���Ϸ�˿�
				} 
				catch (Exception ee){}
			}
			System.exit(0);
		}
		if (e.getSource() == userControlPad.joinButton) // ������Ϸ��ť�����¼�
		{ 
			String selectedUser = (String)userListPad.userList.getSelectedItem(); // ȡ��Ҫ�������Ϸ
			if (selectedUser == null || selectedUser.startsWith("[inchess]") || selectedUser.equals(chessClientName))
			{ 
				firPad.statusText.setText("����ѡ��һ���û�!");  // ��δѡ��Ҫ������û�����ѡ�е��û��Ѿ�����Ϸ���������ʾ��Ϣ
			}
			else
			{ // ִ�м�����Ϸ�Ĳ���
				try
				{
					if (!isGameConnected)
					{
						if (firPad.connectServer(firPad.host, firPad.port))   // ����Ϸ�׽ӿ�δ����
						{
							isGameConnected = true;    // �����ӵ������ɹ�
							isOnChess = true;
							isParticipant = true;
							userControlPad.createButton.setEnabled(false);
							userControlPad.joinButton.setEnabled(false);
							userControlPad.cancelButton.setEnabled(true);
							firPad.firThread.sendMessage("/joingame "+ (String)userListPad.userList.getSelectedItem() + " "	+ chessClientName);
						}
					}
					else
					{ // ����Ϸ�˿�������
						isOnChess = true;
						isParticipant = true;
						userControlPad.createButton.setEnabled(false);
						userControlPad.joinButton.setEnabled(false);
						userControlPad.cancelButton.setEnabled(true);
						firPad.firThread.sendMessage("/joingame "+ (String)userListPad.userList.getSelectedItem() + " "	+ chessClientName);
					}
				}
				catch (Exception ee)
				{
					isGameConnected = false;
					isOnChess = false;
					isParticipant = false;
					userControlPad.createButton.setEnabled(true);
					userControlPad.joinButton.setEnabled(true);
					userControlPad.cancelButton.setEnabled(false);
					userChatPad.chatTextArea.setText("��������: \n" + ee);
				}
			}
		}
		if (e.getSource() == userControlPad.createButton) // ������Ϸ��ť�����¼�
		{
			try
			{
				if (!isGameConnected)// ����Ϸ�˿�δ����
				{ 
					if (firPad.connectServer(firPad.host, firPad.port))
					{ 
						isGameConnected = true;// �����ӵ������ɹ�
						isOnChess = true;
						isCreator = true;
						userControlPad.createButton.setEnabled(false);
						userControlPad.joinButton.setEnabled(false);
						userControlPad.cancelButton.setEnabled(true);
						firPad.firThread.sendMessage("/creatgame "+ "[inchess]" + chessClientName);
					}
				}
				else
				{ // ����Ϸ�˿�������
					isOnChess = true;
					isCreator = true;
					userControlPad.createButton.setEnabled(false);
					userControlPad.joinButton.setEnabled(false);
					userControlPad.cancelButton.setEnabled(true);
					firPad.firThread.sendMessage("/creatgame "+ "[inchess]" + chessClientName);
				}
			}
			catch (Exception ec)
			{
				isGameConnected = false;
				isOnChess = false;
				isCreator = false;
				userControlPad.createButton.setEnabled(true);
				userControlPad.joinButton.setEnabled(true);
				userControlPad.cancelButton.setEnabled(false);
				ec.printStackTrace();
				userChatPad.chatTextArea.setText("��������: \n"+ ec);
			}
		}
		if (e.getSource() == userControlPad.cancelButton)  // �˳���Ϸ��ť�����¼�
		{ 
			if (isOnChess)  // ��Ϸ��
			{
				firPad.firThread.sendMessage("/giveup " + chessClientName);
				firPad.setVicStatus(-1 * firPad.chessColor);
				userControlPad.createButton.setEnabled(true);
				userControlPad.joinButton.setEnabled(true);
				userControlPad.cancelButton.setEnabled(false);
				firPad.statusText.setText("�봴���������Ϸ!");
			}
			if (!isOnChess) // ����Ϸ��
			{ 
				userControlPad.createButton.setEnabled(true);
				userControlPad.joinButton.setEnabled(true);
				userControlPad.cancelButton.setEnabled(false);
				firPad.statusText.setText("�봴���������Ϸ!");
			}
			isParticipant = isCreator = false;
		}
	}

	public void keyPressed(KeyEvent e)
	{
		TextField inputwords = (TextField) e.getSource();
		if (e.getKeyCode() == KeyEvent.VK_ENTER)  // ����س������¼�
		{ 
			if (userInputPad.userChoice.getSelectedItem().equals("�����û�"))// �������˷���Ϣ
			{ 
				try
				{
					// ������Ϣ
					outputStream.writeUTF(inputwords.getText());
					inputwords.setText("");
				}
				catch (Exception ea)
				{
					userChatPad.chatTextArea.setText("�������ӵ�������!\n");
					userListPad.userList.removeAll();
					userInputPad.userChoice.removeAll();
					inputwords.setText("");
					userControlPad.connectButton.setEnabled(true);
				}
			}
			else
			{ // ��ָ���˷���Ϣ
				try
				{
					outputStream.writeUTF("/" + userInputPad.userChoice.getSelectedItem()+ " " + inputwords.getText());
					inputwords.setText("");
				}
				catch (Exception ea)
				{
					userChatPad.chatTextArea.setText("�������ӵ�������!\n");
					userListPad.userList.removeAll();
					userInputPad.userChoice.removeAll();
					inputwords.setText("");
					userControlPad.connectButton.setEnabled(true);
				}
			}
		}
	}

	public void keyTyped(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {}
	
	public static void main(String args[])
	{
		FIRClient chessClient = new FIRClient();
	}
}

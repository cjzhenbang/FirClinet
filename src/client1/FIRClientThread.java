package client1;



import java.io.IOException;
import java.util.StringTokenizer;

import javax.swing.DefaultListModel;
import javax.swing.ListModel;

public class FIRClientThread extends Thread
{
	public FIRClient firClient;

	public FIRClientThread(FIRClient firClient)
	{
		this.firClient = firClient;
	}

	public void dealWithMsg(String msgReceived)
	{
		if (msgReceived.startsWith("/userlist "))
		{ 
			StringTokenizer userToken = new StringTokenizer(msgReceived, " ");// ��ȡ�õ���ϢΪ�û��б�
			int userNumber = 0;
			
			firClient.userListPad.userList.removeAll();// ��տͻ����û��б�
			
			firClient.userInputPad.userChoice.removeAllItems();
			firClient.userInputPad.userChoice.addItem("�����û�");// ���ͻ����û����������һ��ѡ��
			//firClient.userInputPad.userChoice.removeAll();
			while (userToken.hasMoreTokens())
			{ // ���յ����û���Ϣ�б��д�������ʱ
				String user = (String) userToken.nextToken(" "); // ȡ���û���Ϣ
				if (userNumber > 0 && !user.startsWith("[inchess]"))
				{ // �û���Ϣ��Чʱ
					firClient.userListPad.userList.add(user);// ���û���Ϣ��ӵ��û��б���
					firClient.userInputPad.userChoice.addItem(user); // ���û���Ϣ��ӵ��û���������
				}
				userNumber++;
			}
			firClient.userInputPad.userChoice.setSelectedIndex(0);// ������Ĭ��ѡ��������
		}
		else if (msgReceived.startsWith("/yourname "))
		{ // �յ�����ϢΪ�û�����ʱ
			firClient.chessClientName = msgReceived.substring(10); // ȡ���û�����
			firClient.setTitle("Java ������ͻ��� " + "�û���:"
					+ firClient.chessClientName); // ���ó���Frame�ı���
		}
		else if (msgReceived.equals("/reject"))
		{ // �յ�����ϢΪ�ܾ��û�ʱ
			try
			{
				firClient.firPad.statusText.setText("���ܼ�����Ϸ!");
				firClient.userControlPad.cancelButton.setEnabled(false);
				firClient.userControlPad.joinButton.setEnabled(true);
				firClient.userControlPad.createButton.setEnabled(true);
			}
			catch (Exception ef)
			{
				firClient.userChatPad.chatTextArea
						.setText("Cannot close!");
			}
			firClient.userControlPad.joinButton.setEnabled(true);
		}
		else if (msgReceived.startsWith("/peer "))
		{ // �յ���ϢΪ��Ϸ�еĵȴ�ʱ
			firClient.firPad.chessPeerName = msgReceived.substring(6);
			if (firClient.isCreator)// ���û�Ϊ��Ϸ������
			{ 
				firClient.firPad.chessColor = 1; // �趨��Ϊ��������
				firClient.firPad.isMouseEnabled = true;
				firClient.firPad.statusText.setText("�ڷ���...");
			}
			else if (firClient.isParticipant)// ���û�Ϊ��Ϸ������
			{ 
				firClient.firPad.chessColor = -1; // �趨��Ϊ�������
				firClient.firPad.statusText.setText("��Ϸ���룬�ȴ�����.");
			}
		}
		else if (msgReceived.equals("/youwin"))
		{ // �յ���ϢΪʤ����Ϣ
			firClient.isOnChess = false;
			firClient.firPad.setVicStatus(firClient.firPad.chessColor);
			firClient.firPad.statusText.setText("�����˳�");
			firClient.firPad.isMouseEnabled = false;
		}
		else if (msgReceived.equals("/OK"))
		{ // �յ���ϢΪ�ɹ�������Ϸ
			firClient.firPad.statusText.setText("��Ϸ�����ȴ�����");
		}

		else if (msgReceived.equals("/error"))
		{ // �յ���Ϣ����
			firClient.userChatPad.chatTextArea.append("�����˳�����.\n");
		}
		else
		{
			firClient.userChatPad.chatTextArea.append(msgReceived + "\n");
			firClient.userChatPad.chatTextArea.setCaretPosition(
			firClient.userChatPad.chatTextArea.getText().length());
		}
	}

	public void run()
	{
		String message = "";
		try
		{
			while (true)
			{
				// �ȴ�������Ϣ������wait״̬
				message = firClient.inputStream.readUTF();
				dealWithMsg(message);
			}
		}
		catch (IOException es){}
	}
}

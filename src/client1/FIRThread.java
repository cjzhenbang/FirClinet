package client1;



import java.io.IOException;
import java.util.StringTokenizer;

public class FIRThread extends Thread
{
	FIRPad currPad; // ��ǰ�̵߳�����
	
	public FIRThread(FIRPad currPad)
	{
		this.currPad = currPad;
	}

	
	public void dealWithMsg(String msgReceived)    // ����ȡ�õ���Ϣ
	{
		if (msgReceived.startsWith("/chess "))   // �յ�����ϢΪ����
		{ 
			StringTokenizer userMsgToken = new StringTokenizer(msgReceived, " ");
			
			String[] chessInfo = { "-1", "-1", "0" };  // ��ʾ������Ϣ�����顢0����Ϊ��x���ꣻ1����λ��y���ꣻ2����λ��������ɫ
			int i = 0; // ��־λ
			String chessInfoToken;
			while (userMsgToken.hasMoreTokens())
			{
				chessInfoToken = (String) userMsgToken.nextToken(" ");
				if (i >= 1 && i <= 3)
				{
					chessInfo[i - 1] = chessInfoToken;
				}
				i++;
			}
			currPad.paintNetFirPoint(Integer.parseInt(chessInfo[0]), Integer.parseInt(chessInfo[1]), Integer.parseInt(chessInfo[2]));
		}
		else if (msgReceived.startsWith("/yourname "))   // �յ�����ϢΪ����
		{ 
			currPad.chessSelfName = msgReceived.substring(10);
		}
		else if (msgReceived.equals("/error"))  // �յ���Ϊ������Ϣ
		{ 
			currPad.statusText.setText("�û������ڣ������¼���!");
		}
	}
	
	// ������Ϣ
	public void sendMessage(String sndMessage)
	{
		try
		{
			currPad.outputData.writeUTF(sndMessage);
		}
		catch (Exception ea)
		{
			ea.printStackTrace();;
		}
	}

	public void run()
	{
		String msgReceived = "";
		try
		{
			while (true)
			{ // �ȴ���Ϣ����
				msgReceived = currPad.inputData.readUTF();
				dealWithMsg(msgReceived);
			}
		}
		catch (IOException es){}
	}
}

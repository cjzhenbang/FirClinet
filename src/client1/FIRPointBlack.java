package client1;



import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;

public class FIRPointBlack extends Canvas
{
	// ��������������

	public FIRPointBlack()
	{
		//setSize(10, 10); // �������Ӵ�С
	
		
	}

	public void paint(Graphics g)
	{ // ������
		g.setColor(Color.black);
		g.fillOval(0, 0, 14, 14);
	}
}

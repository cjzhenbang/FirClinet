package client1;



import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;

public class FIRPointBlack extends Canvas
{
	// 黑棋所属的棋盘

	public FIRPointBlack()
	{
		//setSize(10, 10); // 设置棋子大小
	
		
	}

	public void paint(Graphics g)
	{ // 画棋子
		g.setColor(Color.black);
		g.fillOval(0, 0, 14, 14);
	}
}

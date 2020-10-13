package client1;



import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;

public class FIRPointWhite extends Canvas
{
	FIRPad padBelonged; // °×ÆåËùÊôµÄÆåÅÌ

	public FIRPointWhite(FIRPad padBelonged)
	{
		setSize(20, 20);
		this.padBelonged = padBelonged;
	}

	public void paint(Graphics g)
	{ // »­Æå×Ó
		g.setColor(Color.white);
		g.fillOval(0, 0, 14, 14);
	}
}

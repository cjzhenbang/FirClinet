package client1;



import java.awt.BorderLayout;
import java.awt.List;
import java.awt.Panel;


//用户列表面板
public class UserListPad extends Panel
{
	public List userList = new List(10);
	
	public UserListPad()
	{
		setLayout(new BorderLayout());
		for (int i = 0; i < 10; i++)
		{
			userList.add(i + "." + "无用户");
		}
		add(userList, BorderLayout.CENTER);
	}
}

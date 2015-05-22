package parser;

public class example {
	int x;
	int y;
	void set_x(int x1)
	{
		x = x1;
	}
	void inc ()
	{
		x = x + 1;
		x = x + 2;
	}
	
	void main() 
	{
		
		set_x (2);	
		if (y > 1)
		{
			y = y - 1;
		}
		else
		{
			y = y + 1;
		}
		if (x > 1)
		{
			inc ();
		}
		y = y + 1;
		
	}	
	
	
}

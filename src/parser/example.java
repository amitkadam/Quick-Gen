package parser;

public class example {
	int x;
	int y;
	int z;
	void set_x(int x1)
	{
		x = x1;
	}
	
	void set_z(int z1)
	{
		z = z1;
	}
	
	void inc ()
	{
		x = x + 1;
		x = x + 2;
	}
	
	void main() 
	{
		
		set_x (2);
		y = y + 1;
		
		if (y > 1 && y < 40)
		{
			y = y - 1;
		}
		else
		{
			y = y + 1;
		}
		
		if (x > 1 && x < 50)
		{
			inc ();
		}

		z = z + 5;
		z = z * 2;
		
		if (z > 1 && z < 40)
		{
			z = z - 1;
		}
		else
		{
			z = z + 1;
		}
		
	}	
	
	
}

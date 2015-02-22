package generator;
import java.util.Random;

public class _Boolean {
	boolean[] defaultBooleanArray;
	
	/** Generate random boolean value **/
	public boolean gen() {
		return new Random().nextBoolean();
	}
	
	/** Generate random boolean array of given limit . */
	public boolean[] gen(int limit)
	{
		defaultBooleanArray = new boolean[limit];
		Random randomGenerator = new Random();

		//note a single Random object is reused here
		for (int idx = 1; idx <= limit; ++idx){			
			defaultBooleanArray[idx-1] = randomGenerator.nextBoolean();
		}
		return defaultBooleanArray;
	}
}

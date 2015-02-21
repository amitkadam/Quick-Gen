import java.util.Random;

public class _Integer {
	int defaultLimit = 10;
	int[] defaultIntArray;
	
	private int getRandomInteger(int aStart, int aEnd, Random aRandom){
	    if (aStart > aEnd) {
	      throw new IllegalArgumentException("Start cannot exceed End.");
	    }
	    if (aStart < Integer.MIN_VALUE && aEnd > Integer.MAX_VALUE) {
	    	throw new IllegalArgumentException("Start or ENd cannot exceed Integer range.");
	    }
	    //get the range, casting to byte to avoid overflow problems
	    long range = (long)(aEnd - 1) - (long)aStart + 1;
	    // compute a fraction of the range, 0 <= frac < range
	    long fraction = (long)(range * aRandom.nextDouble());
	    int randomNumber =  (int)(fraction + aStart);
	    return randomNumber;
	  
	}
	
	/** Generate random Integer **/
	public int gen() {
		return (new Random().nextInt());
	}
	
	/** Generate random integer array of given limit in the range Integer.MIN_VALUE..Integer.MAX_VALUE. */
	public int[] gen(int limit)
	{
		defaultIntArray = new int[limit];
		Random randomGenerator = new Random();

		//note a single Random object is reused here
		for (int idx = 1; idx <= limit; ++idx){			
			defaultIntArray[idx-1] = getRandomInteger(Integer.MIN_VALUE, Integer.MAX_VALUE, randomGenerator);
		}
		return defaultIntArray;
	}
	
	/** Generate 10 random integers in the range aStart..aEnd. */
	public int[] gen(int aStart, int aEnd)
	{
		defaultIntArray = new int[defaultLimit];
		Random randomGenerator = new Random();

		//note a single Random object is reused here
		for (int idx = 1; idx <= defaultLimit; ++idx){			
			defaultIntArray[idx-1] = getRandomInteger(aStart, aEnd, randomGenerator);
		}
		return defaultIntArray;
	}
	
	/** Generate random integer array of given limit in the range aStart..aEnd. */
	public int[] gen(int limit, int aStart, int aEnd)
	{
		defaultIntArray = new int[limit];
		Random randomGenerator = new Random();

		//note a single Random object is reused here
		for (int idx = 1; idx <= limit; ++idx){			
			defaultIntArray[idx-1] = getRandomInteger(aStart, aEnd, randomGenerator);
		}
		return defaultIntArray;
	}
}

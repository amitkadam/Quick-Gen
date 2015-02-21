import java.util.Random;

public class _Long {
	int defaultLimit = 10;
	long[] defaultLongArray;

	private long getRandomLong(long aStart, long aEnd, Random aRandom){
	    if (aStart > aEnd) {
	      throw new IllegalArgumentException("Start cannot exceed End.");
	    }
	    if (aStart < Long.MIN_VALUE && aEnd > Long.MAX_VALUE) {
	    	throw new IllegalArgumentException("Start or ENd cannot exceed Integer range.");
	    }
	    //get the range, casting to byte to avoid overflow problems
	    long range = (long)(aEnd - 1) - (long)aStart + 1;
	    // compute a fraction of the range, 0 <= frac < range
	    long fraction = (long)(range * aRandom.nextDouble());
	    long randomNumber =  (long)(fraction + aStart);
	    return randomNumber;	  
	}
	
	/** Generate random number of type long **/
	public long gen() {
		return new Random().nextLong();
	}

	/** Generate random long integers of given limit in the range Integer.MIN_VALUE..Integer.MAX_VALUE. */
	public long[] gen(int limit)
	{
		defaultLongArray = new long[limit];
		Random randomGenerator = new Random();

		//note a single Random object is reused here
		for (int idx = 1; idx <= limit; ++idx){			
			defaultLongArray[idx-1] = getRandomLong(Integer.MIN_VALUE, Integer.MAX_VALUE, randomGenerator);
		}
		return defaultLongArray;
	}
	
	/** Generate 10 random long integers in the range aStart..aEnd. */
	public long[] gen(long aStart, long aEnd)
	{
		defaultLongArray = new long[defaultLimit];
		Random randomGenerator = new Random();

		//note a single Random object is reused here
		for (int idx = 1; idx <= defaultLimit; ++idx){			
			defaultLongArray[idx-1] = getRandomLong(aStart, aEnd, randomGenerator);
		}
		return defaultLongArray;
	}
	
	/** Generate random long integers of given limit in the range aStart..aEnd. */
	public long[] gen(int limit, long aStart, long aEnd)
	{
		defaultLongArray = new long[limit];
		Random randomGenerator = new Random();

		//note a single Random object is reused here
		for (int idx = 1; idx <= limit; ++idx){			
			defaultLongArray[idx-1] = getRandomLong(aStart, aEnd, randomGenerator);
		}
		return defaultLongArray;
	}
}

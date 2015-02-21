import java.util.Random;

public class _Short {
	int defaultLimit = 10;
	short[] defaultShortArray;
		
	private short getRandomShort(int aStart, int aEnd, Random aRandom){
	    if (aStart > aEnd) {
	      throw new IllegalArgumentException("Start cannot exceed End.");
	    }
	    if (aStart < Short.MIN_VALUE && aEnd > Short.MAX_VALUE) {
	    	throw new IllegalArgumentException("Start or ENd cannot exceed Integer range.");
	    }
	    //get the range, casting to byte to avoid overflow problems
	    long range = (long)(aEnd - 1) - (long)aStart + 1;
	    // compute a fraction of the range, 0 <= frac < range
	    long fraction = (long)(range * aRandom.nextDouble());
	    short randomNumber =  (short)(fraction + aStart);
	    return randomNumber;
	  
	}
	
	/** Generate random number of type short **/
	public short gen() {
		return (short) new Random().nextInt((Short.MAX_VALUE + 1) * 2);
	}
	
	/** Generate random short integers of given limit in the range Short.MIN_VALUE..Short.MAX_VALUE. */
	public short[] gen(int limit)
	{
		defaultShortArray = new short[limit];
		Random randomGenerator = new Random();

		//note a single Random object is reused here
		for (int idx = 1; idx <= limit; ++idx){			
			defaultShortArray[idx-1] = getRandomShort(Short.MIN_VALUE, Short.MAX_VALUE, randomGenerator);
		}
		return defaultShortArray;
	}
	
	/** Generate 10 random short integers in the range aStart..aEnd. */
	public short[] gen(int aStart, int aEnd)
	{
		defaultShortArray = new short[defaultLimit];
		Random randomGenerator = new Random();

		//note a single Random object is reused here
		for (int idx = 1; idx <= defaultLimit; ++idx){			
			defaultShortArray[idx-1] = getRandomShort(aStart, aEnd, randomGenerator);
		}
		return defaultShortArray;
	}
	
	/** Generate random short integers of given limit in the range aStart..aEnd. */
	public short[] gen(int limit, int aStart, int aEnd)
	{
		defaultShortArray = new short[limit];
		Random randomGenerator = new Random();

		//note a single Random object is reused here
		for (int idx = 1; idx <= limit; ++idx){			
			defaultShortArray[idx-1] = getRandomShort(aStart, aEnd, randomGenerator);
		}
		return defaultShortArray;
	}
}

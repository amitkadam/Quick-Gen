package generator;
import java.util.Random;

public class _Float {
	int defaultLimit = 10;
	float[] defaultFloatArray;
	
	private float getRandomFloat(float aStart, float aEnd, Random aRandom){
	    if (aStart > aEnd) {
	      throw new IllegalArgumentException("Start cannot exceed End.");
	    }
	    if (aStart < Integer.MIN_VALUE && aEnd > Integer.MAX_VALUE) {
	    	throw new IllegalArgumentException("Start or ENd cannot exceed Integer range.");
	    }
	    //get the range, casting to byte to avoid overflow problems
	    double range = (double)(aEnd - 1) - (double)aStart + 1;
	    // compute a fraction of the range, 0 <= frac < range
	    double fraction = (double)(range * aRandom.nextDouble());
	    float randomNumber =  (float)(fraction + aStart);
	    return randomNumber;	  
	}
	
	/** Generate random number of type float **/
	public float gen() {
		return new Random().nextFloat();
	}
	
	/** Generate random float array of given limit in the range Float.MIN_VALUE..Float.MAX_VALUE. */
	public float[] gen(int limit)
	{
		defaultFloatArray = new float[limit];
		Random randomGenerator = new Random();

		//note a single Random object is reused here
		for (int idx = 1; idx <= limit; ++idx){			
			defaultFloatArray[idx-1] = getRandomFloat(Float.MIN_VALUE, Float.MAX_VALUE, randomGenerator);
		}
		return defaultFloatArray;
	}
	
	/** Generate 10 random float numbers in the range aStart..aEnd. */
	public float[] gen(float aStart, float aEnd)
	{
		defaultFloatArray = new float[defaultLimit];
		Random randomGenerator = new Random();

		//note a single Random object is reused here
		for (int idx = 1; idx <= defaultLimit; ++idx){			
			defaultFloatArray[idx-1] = getRandomFloat(aStart, aEnd, randomGenerator);
		}
		return defaultFloatArray;
	}
	
	/** Generate random float numbers of given limit in the range aStart..aEnd. */
	public float[] gen(int limit, float aStart, float aEnd)
	{
		defaultFloatArray = new float[limit];
		Random randomGenerator = new Random();

		//note a single Random object is reused here
		for (int idx = 1; idx <= limit; ++idx){			
			defaultFloatArray[idx-1] = getRandomFloat(aStart, aEnd, randomGenerator);
		}
		return defaultFloatArray;
	}
}

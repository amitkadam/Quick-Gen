package generator;
import java.util.Random;

public class _Double {
	int defaultLimit = 10;
	double[] defaultDoubleArray;
	
	private double getRandomDouble(double aStart, double aEnd, Random aRandom){
	    if (aStart > aEnd) {
	      throw new IllegalArgumentException("Start cannot exceed End.");
	    }
	    if (aStart < Double.MIN_VALUE && aEnd > Double.MAX_VALUE) {
	    	throw new IllegalArgumentException("Start or ENd cannot exceed Integer range.");
	    }
	    //get the range, casting to byte to avoid overflow problems
	    double range = (double)(aEnd - 1) - (double)aStart + 1;
	    // compute a fraction of the range, 0 <= frac < range
	    double fraction = (double)(range * aRandom.nextDouble());
	    double randomNumber =  (double)(fraction + aStart);
	    return randomNumber;	  
	}
	
	/** Generate random number of type double **/
	public double gen() {
		return new Random().nextDouble();
	}
	
	/** Generate random double array of given limit in the range Float.MIN_VALUE..Float.MAX_VALUE. */
	public double[] gen(int limit)
	{
		defaultDoubleArray = new double[limit];
		Random randomGenerator = new Random();

		//note a single Random object is reused here
		for (int idx = 1; idx <= limit; ++idx){			
			defaultDoubleArray[idx-1] = getRandomDouble(Float.MIN_VALUE, Float.MAX_VALUE, randomGenerator);
		}
		return defaultDoubleArray;
	}
	
	/** Generate 10 random double numbers in the range aStart..aEnd. */
	public double[] gen(double aStart, double aEnd)
	{
		defaultDoubleArray = new double[defaultLimit];
		Random randomGenerator = new Random();

		//note a single Random object is reused here
		for (int idx = 1; idx <= defaultLimit; ++idx){			
			defaultDoubleArray[idx-1] = getRandomDouble(aStart, aEnd, randomGenerator);
		}
		return defaultDoubleArray;
	}
	
	/** Generate random double numbers of given limit in the range aStart..aEnd. */
	public double[] gen(int limit, double aStart, double aEnd)
	{
		defaultDoubleArray = new double[limit];
		Random randomGenerator = new Random();

		//note a single Random object is reused here
		for (int idx = 1; idx <= limit; ++idx){			
			defaultDoubleArray[idx-1] = getRandomDouble(aStart, aEnd, randomGenerator);
		}
		return defaultDoubleArray;
	}
}

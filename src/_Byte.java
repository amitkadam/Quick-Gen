import java.util.Random;

public class _Byte {
	int defaultLimit = 10;
	byte[] defaultBytesArray;

	private static void log(String aMessage) {
		System.out.println(aMessage);
	}

	private byte getRandomByte(int aStart, int aEnd, Random aRandom){
		byte[] randomByte = new byte[1]; 
	    if (aStart > aEnd) {
	      throw new IllegalArgumentException("Start cannot exceed End.");
	    }
	    if (aStart < -128 && aEnd > 127) {
	    	throw new IllegalArgumentException("Start or ENd cannot exceed byte range.");
	    }
	    //get the range, casting to byte to avoid overflow problems
	    byte range = (byte) ((byte)aEnd - (byte)aStart + 0x01);
	    // compute a fraction of the range, 0 <= frac < range
	    aRandom.nextBytes(randomByte);
	    byte fraction = (byte)(range * randomByte[0]);
	    byte randomNumber =  (byte)(fraction + aStart);
	    return randomNumber;
	  
	}
	
	/** Generate random byte **/
	public byte gen() {
		return getRandomByte(-127, 127, new Random());
	}
	
	/** Generate 10 random bytes in the range -127..127. */
	public byte[] gen(int limit)
	{
		defaultBytesArray = new byte[limit];
		Random randomGenerator = new Random();

		//note a single Random object is reused here
		for (int idx = 1; idx <= limit; ++idx){			
			defaultBytesArray[idx-1] = getRandomByte(-127, 127, randomGenerator);
		}
		return defaultBytesArray;
	}
	
	/** Generate 10 random bytes in the range aStart..aEnd. */
	public byte[] gen(int aStart, int aEnd)
	{
		defaultBytesArray = new byte[defaultLimit];
		Random randomGenerator = new Random();

		//note a single Random object is reused here
		for (int idx = 1; idx <= defaultLimit; ++idx){			
			defaultBytesArray[idx-1] = getRandomByte(aStart, aEnd, randomGenerator);
		}
		return defaultBytesArray;
	}
	
	/** Generate 10 random bytes in the range aStart..aEnd. */
	public byte[] gen(int limit, int aStart, int aEnd)
	{
		defaultBytesArray = new byte[limit];
		Random randomGenerator = new Random();

		//note a single Random object is reused here
		for (int idx = 1; idx <= limit; ++idx){			
			defaultBytesArray[idx-1] = getRandomByte(aStart, aEnd, randomGenerator);
		}
		return defaultBytesArray;
	}
}

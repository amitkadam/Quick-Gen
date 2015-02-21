public class _Char {
	char[] defaultCharArray;
	_Byte Bytes = new _Byte();
	
	/** Generate random character in the range a..z **/
	public char gen() {
		return (char)Bytes.gen(1, 97, 122)[0];
	}
	
	/** Generate random char array of given limit in the range a..z. */
	public char[] gen(int limit)
	{
		defaultCharArray = new char[limit];

		//note a single Random object is reused here
		for (int idx = 1; idx <= limit; ++idx){			
			defaultCharArray[idx-1] = gen();
		}
		return defaultCharArray;
	}
}

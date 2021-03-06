package generator;

import java.util.ArrayList;
import java.util.Arrays;

public class Generators {
	int defaultValue = 10;
	_Integer intObj;
	_Short shortObj;
	_Byte byteObj;
	_Boolean booleanObj;
	_Float floatObj;
	_Long longObj;
	_Double doubleObj;
	_Char charObj;
	_String stringObj;
	
	
	Generators() {
		intObj = new _Integer();
		shortObj = new _Short();
		byteObj = new _Byte();
		booleanObj = new _Boolean();
		floatObj = new _Float();
		longObj = new _Long();
		doubleObj = new _Double();
		charObj = new _Char();
		stringObj = new _String();
	}
	
	public Object generate(Object obj) {
		if (obj instanceof _Integer) {
			intObj = new _Integer();
			return intObj.gen(defaultValue);
			
		}
		return intObj.defaultIntArray;
	}

	public int[] Int(int...args) {
		switch (args.length) {
		case 0:	return intObj.gen(1);
		case 1: return intObj.gen(args[0]);
		case 2: return intObj.gen(args[0], args[1]);
		case 3: return intObj.gen(args[0], args[1], args[2]);
		default: throw new IllegalArgumentException("Illegal argument exception.");		
		}
	}
	
	public short[] Short(int...args) {
		switch (args.length) {
		case 0:	return shortObj.gen(1);
		case 1: return shortObj.gen(args[0]);
		case 2: return shortObj.gen(args[0], args[1]);
		case 3: return shortObj.gen(args[0], args[1], args[2]);
		default: throw new IllegalArgumentException("Illegal argument exception.");		
		}
	}
	
	public byte[] Byte(int...args) {
		switch (args.length) {
		case 0:	return byteObj.gen(1);
		case 1: return byteObj.gen(args[0]);
		case 2: return byteObj.gen(args[0], args[1]);
		case 3: return byteObj.gen(args[0], args[1], args[2]);
		default: throw new IllegalArgumentException("Illegal argument exception.");		
		}
	}
	
	public boolean[] Boolean(int...args) {
		switch (args.length) {
		case 0:	return booleanObj.gen(1);
		case 1: return booleanObj.gen(args[0]);
		default: throw new IllegalArgumentException("Illegal argument exception.");		
		}
	}
	
	public float[] Float(float...args) {
		switch (args.length) {
		case 0:	return floatObj.gen(1);
		case 1: return floatObj.gen((int)args[0]);
		case 2: return floatObj.gen(args[0], args[1]);
		case 3: return floatObj.gen((int)args[0], args[1], args[2]);
		default: throw new IllegalArgumentException("Illegal argument exception.");		
		}
	}
	
	public long[] Long(long...args) {
		switch (args.length) {
		case 0:	return longObj.gen(1);
		case 1: return longObj.gen((int)args[0]);
		case 2: return longObj.gen(args[0], args[1]);
		case 3: return longObj.gen((int)args[0], args[1], args[2]);
		default: throw new IllegalArgumentException("Illegal argument exception.");		
		}
	}
	
	public double[] Double(double...args) {
		switch (args.length) {
		case 0:	return doubleObj.gen(1);
		case 1: return doubleObj.gen((int)args[0]);
		case 2: return doubleObj.gen(args[0], args[1]);
		case 3: return doubleObj.gen((int)args[0], args[1], args[2]);
		default: throw new IllegalArgumentException("Illegal argument exception.");		
		}
	}
	
	public char[] Char(int...args) {
		switch (args.length) {
		case 0:	return charObj.gen(1);
		case 1: return charObj.gen(args[0]);
		default: throw new IllegalArgumentException("Illegal argument exception.");		
		}
	}
	
	public String[] String(int...args) {
		switch (args.length) {
		case 0:	return stringObj.gen(1);
		case 1: return stringObj.gen(args[0]);
		default: throw new IllegalArgumentException("Illegal argument exception.");		
		}
	}
	
	public int[] negInt(int...args) {
		switch (args.length) {
		case 0:	return intObj.gen(defaultValue, Integer.MIN_VALUE, 0);
		case 1: return intObj.gen(args[0], Integer.MIN_VALUE, 0);
		default: throw new IllegalArgumentException("Illegal argument exception.");		
		}
	}
		
	public ArrayList Ret (Object...args) {
		ArrayList lst = new ArrayList();
		switch (args.length) {
		case 1: for (int idx = 0; idx < defaultValue; ++idx){			
				lst.add(args[0]);
				}
				return lst;
		case 2: for (int idx = 0; idx < (int)args[1]; ++idx){			
				lst.add(args[0]);
				}
			return lst;
		default: throw new IllegalArgumentException("Illegal argument exception.");		
		}
	}
	
	public int[] Choose (int aStart, int aEnd) {
		return intObj.gen(defaultValue, aStart, aEnd);
	}
	
	private static void log(String aMessage) {
		System.out.println(aMessage);
	}
	
	public static void main(String...strings) {
		Generators gen = new Generators();
		log("random int: " + Arrays.toString (gen.Int()));
		log("random int array: " + Arrays.toString (gen.Int(12)));
		log("random int array of given range: " + Arrays.toString (gen.Int(12, 25, 45)));
		log("return specific element: " + Arrays.toString (gen.Ret("mj", 5).toArray()));
		log("choose random int between given range: " + Arrays.toString (gen.Choose(5, 10)));
		log("random negative int: " + Arrays.toString (gen.negInt(10)));
		log("random char: " + Arrays.toString (gen.Char(5)));
		log("random words: " + Arrays.toString (gen.String(30)));
	}
}

package quick_gen;

import java.util.Scanner;

public class userInput {
	public String javaFilePath;
	public int iterations;
	public symbolMapping[] mapping;
	
	public void setFilePath(String path) {
		this.javaFilePath = path;
	}
	
	public String getFilePath() {
		return this.javaFilePath;
	}
	
	public void setIterations (int i) {
		this.iterations = i;
	}
	
	public int getIterations () {
		return this.iterations;
	}
	
	public static void log (String s) {
		System.out.println(s);
	}
	
	public void getUserInput() {
		Scanner reader = new Scanner(System.in);
		log("Enter java file path: ");
		this.setFilePath(reader.next());
		
		log("Enter minimum number of iterations: ");
		this.setIterations(reader.nextInt());
		
		log("Enter number of symbols for which you want to generate data: ");
		int count = reader.nextInt();
		this.mapping = new symbolMapping[count];
		for (int i = 0; i < count; i++) {
			mapping[i] = new symbolMapping();
			log("Enter symbol name: ");
			mapping[i].symbolName = reader.next();
			//log("Enter it's transformation: ");
			//mapping[i].transformation = reader.next();
			log("Enter it's type: ");
			mapping[i].type = reader.next();
		}
		reader.close();
	}
}

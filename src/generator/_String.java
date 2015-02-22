package generator;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class _String {
	String wordsFile = "./resources/en_words";
	int lines;
	String[] words;

	/* Count number of words */
	private void countLines(String filename) {
		int lines = 0;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			while (reader.readLine() != null) lines++;
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.lines = lines;
	}
	
	/* Read and store words */
	private void readAndStoreWords() {
		BufferedReader br = null;

		try {
			String sCurrentLine;
			int idx = 0;
			br = new BufferedReader(new FileReader(wordsFile));
			while ((sCurrentLine = br.readLine()) != null) {
				words[idx] = sCurrentLine;
				idx++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	_String() {
		/* Count lines of words file and initialize words array */
		countLines(wordsFile);
		words = new String[lines];
		readAndStoreWords();
	}
	
	public String gen() {
		Random randomGenerator = new Random();
		return words[randomGenerator.nextInt(lines)];
	}

	public String[] gen(int limit) {
		Random randomGenerator = new Random();
		String[] wordsArray = new String[limit];
		//note a single Random object is reused here
		for (int idx = 1; idx <= limit; ++idx){			
			wordsArray[idx-1] = words[randomGenerator.nextInt(lines)];
		}
		return wordsArray;
	}
}

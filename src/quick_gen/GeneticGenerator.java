package quick_gen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import generator.Generators;
import parser.Statement;

public class GeneticGenerator {
	// Map to generate data using genetic algorithm
	/*{"exp" "x * 3"
	 * "cnd" "x > 50"
	 * "exp" "x - 2"
	 * "exp" "x * 2"
	 * "cnd" "x < 200"} */

	// Revere the possible conditions, this is to generate -ve data values
	/* {"==" "!="
	 *  "!=" "=="
	 *  "<"  ">="
	 *  ">=" "<"
	 *  ">"  "<="
	 *  "<=" ">"} */

	private static void log(String aMessage) {
		System.out.println(aMessage);
	}

	// Finally convert potential values data to set(to get unique of them)
	// This function will generate initial data in genetic algorithm.
	public static void generateWithCondition(String[] conditions, int iterations) {
		// Use conditions parameter instead of temporary cnds
		ArrayList<String> cnds = new ArrayList<String>();
		cnds.add("x < 200");
		cnds.add("x > 50");
		// Concat all conditions with &&
		String allCnds = new String(cnds.get(0));
		cnds.remove(0);
		for (String s : cnds){
			allCnds = allCnds.concat(" && "+ s);
		}

		Generators gen = new Generators();
		Pattern p = Pattern.compile("-?\\d+");
		Matcher m = p.matcher(allCnds);
		ArrayList<Integer> allMatches = new ArrayList<Integer>();
		while (m.find()) {
			allMatches.add(Integer.parseInt(m.group()));
		}
		Collections.sort(allMatches);
		// use global mapping instead of x
		log("Initial population: "+ Arrays.toString(gen.suchThat(allCnds.replace('x', '$'), gen._Int, iterations, allMatches.get(0), allMatches.get(allMatches.size()-1))));
	}

	public static void generateParents (Statement []s, ArrayList<Integer> initialVals) {
		ArrayList<Integer> parents = new ArrayList<Integer>();
		ArrayList<Integer> failedCases = new ArrayList<Integer>();
		ExpEvaluator eval = new ExpEvaluator();
		for (int idx = 0; idx < initialVals.size(); idx++) {
			int initialValue = initialVals.get(idx);
			int i;
			Map<String, Double> replacer = new HashMap<String, Double>();
			// use global mapping table instead of x
			replacer.put("x", (double)initialValue);
			for(i = 0; i < s.length; i++) {

				if (s[i].type.equals("cnd")) {
					// use global mapping table instead of x
					//log(s[i].code.replaceAll("x", Integer.toString(initialValue)));
					if (eval.evaluateCondition(s[i].code.replaceAll("x", Integer.toString(initialValue))))
						continue;
					else {
						failedCases.add(initialValue);
						break;
					}
				}
				else if (s[i].type.equals("exp")) {
					// use global mapping table instead of x
					replacer.put("x", (double)initialValue);
					initialValue = (int) eval.evaluate(s[i].code, replacer);
				}
			}
			if (i >= s.length)
				parents.add(initialValue);
		}
		log("Parents: "+ parents.toString());
		log("Failed cases: "+ failedCases.toString());
	}
	
	public static Map<String, Integer> fitnessFunction(Statement []s, int initialVal) {
		ArrayList<Integer> parents = new ArrayList<Integer>();
		ArrayList<Integer> failedCases = new ArrayList<Integer>();
		Map<String, Integer> result = new HashMap<String, Integer>();
		ExpEvaluator eval = new ExpEvaluator();
		int initialValue = initialVal;
		int i;
		Map<String, Double> replacer = new HashMap<String, Double>();
		// use global mapping table instead of x
		replacer.put("x", (double)initialValue);
		for(i = 0; i < s.length; i++) {

			if (s[i].type.equals("cnd")) {
				// use global mapping table instead of x
				//log(s[i].code.replaceAll("x", Integer.toString(initialValue)));
				if (eval.evaluateCondition(s[i].code.replaceAll("x", Integer.toString(initialValue))))
					continue;
				else {
					result.put("result", 0);
					result.put("value", initialValue);
					result.put("orig", initialVal);
					failedCases.add(initialValue);
					break;
				}
			}
			else if (s[i].type.equals("exp")) {
				// use global mapping table instead of x
				replacer.put("x", (double)initialValue);
				initialValue = (int) eval.evaluate(s[i].code, replacer);
			}
		}
		if (i >= s.length)
		{
			result.put("result", 1);
			result.put("value", initialValue);
			result.put("orig", initialVal);
			parents.add(initialValue);
		}
		//log("Parents: "+ parents.toString());
		//log("Failed cases: "+ failedCases.toString());
		return result;
	}

	public static void crossOver (Statement []s, ArrayList<Integer> parents) {
		
		// TODO
		/* Make possible groups of parents
		 * Convert both decimal parents into binary
		 * Divide both binary parents into half
		 * Take first half of first parent and second half of second parent
		 * Merge them together and convert it back to decimal
		 * Pass that number through coupling sequence
		 * If it satisfies then add it to the child node  
		*/
		ArrayList<Integer> childs = new ArrayList<Integer>();
		ArrayList<Integer> failedChilds = new ArrayList<Integer>();
		int idx1, idx2;
		for (idx1 = 0; idx1 < parents.size() - 1; idx1++) {
			String parent1 = Integer.toBinaryString(parents.get(idx1));
			final int mid1 = parent1.length() / 2;
			String[] parentParts1 = {
					parent1.substring(0, mid1),
					parent1.substring(mid1),
			};
			for (idx2 = idx1 + 1; idx2 < parents.size(); idx2++) {
				String parent2 = Integer.toBinaryString(parents.get(idx2));
				final int mid2 = parent1.length() / 2;
				String[] parentParts2 = {
						parent2.substring(0, mid2),
						parent2.substring(mid2),
				};
				String binaryGene = parentParts1[0] + parentParts2[1];
				int child = Integer.parseInt(binaryGene, 2);
				
				// Pass child through coupling sequence if it pass all the sequence then add
				Map<String, Integer> result = fitnessFunction(s, child);
				if (result.get("result") == 1) {
					childs.add(result.get("value"));
				} else
					failedChilds.add(result.get("value"));
			}
		}
		log("childs: "+ childs.toString());
		log("Failed child cases: "+ failedChilds.toString());
		
	}
	
	public static void mutation (Statement []s, ArrayList<Integer> parents) {
		// TODO
		/* 
		 * Take one parent at a time
		 * Convert decimal parent into binary bits
		 * Get random position of any binary bit and flip it.
		 * Convert that binary sequence into decimal
		 * Pass that number through coupling sequence
		 * If it satisfies coupling sequence then add it to child node. 
		 * */
		ArrayList<Integer> childs = new ArrayList<Integer>();
		ArrayList<Integer> failedChilds = new ArrayList<Integer>();
		for(int idx = 0; idx < parents.size(); idx++) {
			String binaryParent = Integer.toBinaryString(parents.get(idx));
			int mid = binaryParent.length()/2;
			char singleBit = binaryParent.charAt(mid);
			char newBit;
			if (singleBit == '0')
				newBit = '1';
			else newBit = '0';

			String binaryChild = binaryParent.substring(0,mid) + newBit + binaryParent.substring(mid+1);
			int child = Integer.parseInt(binaryChild, 2);

			// Pass child through coupling sequence if it pass all the sequence then add
			Map<String, Integer> result = fitnessFunction(s, child);
			if (result.get("result") == 1) {
				childs.add(result.get("value"));
			} else
				failedChilds.add(result.get("value"));
		}
		log("childs: "+ childs.toString());
		log("Failed child cases: "+ failedChilds.toString());
	}
	
	public static void main(String...strings) {
		generateWithCondition(null, 10);
		Statement []s = new Statement[5];
		s[0] = new Statement();
		s[0].setType("exp");
		s[0].setCode("x + 3");

		s[1] = new Statement();
		s[1].setType("cnd");
		s[1].setCode("x > 50");

		s[2] = new Statement();
		s[2].setType("exp");
		s[2].setCode("x - 2");

		s[3] = new Statement();
		s[3].setType ("exp");
		s[3].setCode("x * 2");

		s[4] = new Statement();
		s[4].setType("cnd");
		s[4].setCode("x < 250");

		ArrayList<Integer> initialVals = new ArrayList<Integer>();
		initialVals.add(158);
		initialVals.add(141);
		initialVals.add(109);
		initialVals.add(164);
		initialVals.add(190);
		initialVals.add(110);
		initialVals.add(78);
		initialVals.add(66);
		initialVals.add(56);
		initialVals.add(19);
		//log(Integer.toBinaryString(318));
		
		
		// initialVals = generateWithConditios(conds[], iterations);
		generateParents(s, initialVals);
		
		
		ArrayList<Integer> par = new ArrayList<Integer>();
		par.add(220);
		par.add(222);
		par.add(158);
		par.add(134);
		par.add(114);
		
		log("---------------------------------------------------");
		log("CrossOver result: ");
		crossOver(s, par);
		log("---------------------------------------------------");
		log("Mutation result: ");
		mutation(s, par);
	}
}

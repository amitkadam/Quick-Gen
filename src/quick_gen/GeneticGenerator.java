package quick_gen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import generator.Generators;
import parser.Extraction;
import parser.SequenceGen;
import parser.Statement;
import parser.Function;

public class GeneticGenerator {
	// Map to generate data using genetic algorithm
	/*{"exp" "x * 3"
	 * "cnd" "x > 50"
	 * "exp" "x - 2"
	 * "exp" "x + 50"
	 * "cnd" "x < 250"} */

	// Revere the possible conditions, this is to generate -ve data values
	/* {"==" "!="
	 *  "!=" "=="
	 *  "<"  ">="
	 *  ">=" "<"
	 *  ">"  "<="
	 *  "<=" ">"} */

	public static ArrayList<Integer> parents;
	public static ArrayList<Integer> failedParents;
	public static ArrayList<Integer> childs;
	public static ArrayList<Integer> failedChilds;
	public static ArrayList<Integer> initialPopulation;
	public static ArrayList<Function> functions;
	
	
	private static void log(String aMessage) {
		System.out.println(aMessage);
	}

	public static void init() {
		parents = new ArrayList<Integer>();
		failedParents = new ArrayList<Integer>();
		childs = new ArrayList<Integer>();
		failedChilds = new ArrayList<Integer>();
		initialPopulation = new ArrayList<Integer>();
		functions = new ArrayList<Function>();
	}

	public static ArrayList<String> extractConditions(Statement []s) {
		ArrayList<String> cnds = new ArrayList<String>();
		for(int i = 0; i < s.length; i++) {
			if (s[i].type.equals("cnd")) {
				cnds.add(s[i].code);
			}
		}
		return cnds;
	}

	// Finally co/vert potential values data to set(to get unique of them)
	// This function will generate initial data in genetic algorithm.
	public static void generateWithCondition(ArrayList<String> conditions, int iterations, String mappingVariable) {
		// Concat all conditions with &&
		String allCnds = new String(conditions.get(0));
		conditions.remove(0);
		for (String s : conditions){
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
		int startRange = allMatches.get(0);
		int endRange = allMatches.size() > 1 ? allMatches.get(allMatches.size()-1) : Integer.MAX_VALUE;
		int[] initialPop = gen.suchThat(allCnds.replaceAll(mappingVariable, "\\$"), gen._Int, iterations, startRange, endRange);
		for (int i = 0; i < initialPop.length; i++)
			initialPopulation.add(Integer.valueOf(initialPop[i]));

		log("Initial population: "+ initialPopulation.toString());
	}

	public static void generateParents (Statement []s, ArrayList<Integer> initialVals, String mappingVariable) {
		//ArrayList<Integer> parents = new ArrayList<Integer>();
		//ArrayList<Integer> failedCases = new ArrayList<Integer>();
		//ExpEvaluator eval = new ExpEvaluator();
		for (int idx = 0; idx < initialVals.size(); idx++) {
			int initialValue = initialVals.get(idx);
			// Pass child through coupling sequence if it pass all the sequence then add
			Map<String, Integer> result = fitnessFunction(s, initialValue, mappingVariable);
			if (result.get("result") == 1) {
				parents.add(result.get("value"));
			} else
				failedParents.add(result.get("value"));
		}
		log("Parents: "+ parents.toString());
		log("Failed cases: "+ failedParents.toString());
	}

	public static Map<String, Integer> fitnessFunction(Statement []s, int initialVal, String mappingVariable) {
		ArrayList<Integer> parents = new ArrayList<Integer>();
		ArrayList<Integer> failedCases = new ArrayList<Integer>();
		Map<String, Integer> result = new HashMap<String, Integer>();
		ExpEvaluator eval = new ExpEvaluator();
		int initialValue = initialVal;
		int i;
		Map<String, Double> replacer = new HashMap<String, Double>();
		replacer.put(mappingVariable, (double)initialValue);
		for(i = 0; i < s.length; i++) {

			if (s[i].type.equals("cnd")) {
				//log(s[i].code.replaceAll("x", Integer.toString(initialValue)));
				if (eval.evaluateCondition(s[i].code.replaceAll(mappingVariable, Integer.toString(initialValue))))
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
				replacer.put(mappingVariable, (double)initialValue);
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
		return result;
	}

	public static void crossOver (Statement []s, ArrayList<Integer> parents, String mappingVariable) {

		// TODO
		/* Make possible groups of parents
		 * Convert both decimal parents into binary
		 * Divide both binary parents into half
		 * Take first half of first parent and second half of second parent
		 * Merge them together and convert it back to decimal
		 * Pass that number through coupling sequence
		 * If it satisfies then add it to the child node
		 */
		//ArrayList<Integer> childs = new ArrayList<Integer>();
		//ArrayList<Integer> failedChilds = new ArrayList<Integer>();
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
				Map<String, Integer> result = fitnessFunction(s, child, mappingVariable);
				if (result.get("result") == 1) {
					childs.add(result.get("value"));
				} else
					failedChilds.add(result.get("value"));
			}
		}
		//log("childs: "+ childs.toString());
		//log("Failed child cases: "+ failedChilds.toString());

	}

	public static void mutation (Statement []s, ArrayList<Integer> parents, String mappingVariable) {
		// TODO
		/*
		 * Take one parent at a time
		 * Convert decimal parent into binary bits
		 * Get random position of any binary bit and flip it.
		 * Convert that binary sequence into decimal
		 * Pass that number through coupling sequence
		 * If it satisfies coupling sequence then add it to child node.
		 * */
		//ArrayList<Integer> childs = new ArrayList<Integer>();
		//ArrayList<Integer> failedChilds = new ArrayList<Integer>();
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
			Map<String, Integer> result = fitnessFunction(s, child, mappingVariable);
			if (result.get("result") == 1) {
				childs.add(result.get("value"));
			} else
				failedChilds.add(result.get("value"));
		}
		//log("childs: "+ childs.toString());
		//log("Failed child cases: "+ failedChilds.toString());
	}

	public static void removeDuplicate() {
		//initialPopulation = new ArrayList<Integer> (new LinkedHashSet<Integer>(initialPopulation));
		//parents = new ArrayList<Integer> (new LinkedHashSet<Integer>(parents));
		failedParents = new ArrayList<Integer> (new LinkedHashSet<Integer>(failedParents));
		childs = new ArrayList<Integer> (new LinkedHashSet<Integer>(childs));
		failedChilds = new ArrayList<Integer> (new LinkedHashSet<Integer>(failedChilds));
	}

	public static void main(String...strings) {
		//Statement []s = new Statement[5];
		Generators gen = new Generators();
		SequenceGen seq_gen = new SequenceGen();
		/*s[0] = new Statement();
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
		s[3].setCode("x + 50");

		s[4] = new Statement();
		s[4].setType("cnd");
		s[4].setCode("x < 150");
        */	
		init();
		userInput user = new userInput();
		user.getUserInput();
		Extraction e = new Extraction();
		e.extractFunctionsDetails(user.getFilePath());
		
		for (int i = 0; i < user.mapping.length; i++) {
			if (user.mapping[i].type.equals("boolean")) {
				gen.Boolean(user.iterations);
				continue;
			}
			else if (user.mapping[i].type.equals("String")) {
				gen.String(user.iterations);
				continue;
			}
			else if (user.mapping[i].type.equals("char")) {
				gen.Char(user.iterations);
				continue;
			}
			else if (user.mapping[i].type.equals("int")) {
				ArrayList<Statement> sequence = new ArrayList<Statement>();
				sequence = seq_gen.genSequence(e.functions, "main", user.mapping[i].symbolName);
				Statement[] s = new Statement[sequence.size()];
				sequence.toArray(s);
				ArrayList<String> conds = extractConditions(s);
				if(conds.size() == 0) {
					// It has to be number of iterations
					log("No conditions found...");
					gen.Int(12);
					return;
				}
				generateWithCondition(conds, 10, user.mapping[i].symbolName);
				initialPopulation = new ArrayList<Integer> (new LinkedHashSet<Integer>(initialPopulation));
				generateParents(s, initialPopulation, user.mapping[i].symbolName);

				parents = new ArrayList<Integer> (new LinkedHashSet<Integer>(parents));
				log("---------------------------------------------------");
				log("CrossOver result: ");
				crossOver(s, parents, user.mapping[i].symbolName);
				removeDuplicate();
				log("childs: "+ childs.toString());
				log("Failed child cases: "+ failedChilds.toString());
				log("---------------------------------------------------");
				log("Mutation result: ");
				mutation(s, parents, user.mapping[i].symbolName);
				removeDuplicate();
				log("childs: "+ childs.toString());
				log("Failed child cases: "+ failedChilds.toString());
			}
		}
	}
}

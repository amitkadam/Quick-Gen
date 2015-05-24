package parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SequenceGen {

	/**
	 * @param args
	 */
	boolean var_first_occurance = true;
	
	
	/*
	 * Given two arraylists - keys_list & vals_list
	 * returns map of it.
	 * ex, keys_list = ["a" "b"]
	 *     vals_list = ["1" "2"]
	 * returns map as - {"a" 1
	 *                   "b" 2}
	 */
	Map<String, String> createMap(ArrayList<String> keys_list, ArrayList<String> vals_list)
	{
		Map<String, String> m = new HashMap<String, String>();
		for (int i = 0; i< keys_list.size(); i++)
		{
			m.put(keys_list.get(i), vals_list.get(i));	
		}
		return m;
		
	}
	
	/*
	 * Given a string and map of replacer, replaces values with replacer in given string
	 * Ex, data is " c = a + b"
	 *     replacer is {"a" 1 
	 *                  "b" 2}
	 *     returns - "c = 1 + 2"
	 */
	String replaceWithGivenReplacers (String data, Map<String, String> replacers)
	{
		Iterator<String> iterator = replacers.keySet().iterator();
		while(iterator.hasNext()){
			Object key   = iterator.next();
			Object value = replacers.get(key);
			data = data.replaceAll((String) key, (String) value);
		}
		return data;
	}
	
	/*
	 * Given a list of function objects and required function name
	 * @returns function object of that function
	 */
	Function getFunctionDetails(ArrayList<Function> functions, String function_name)
	{
		Iterator<Function> iterator = functions.iterator();
		while (iterator.hasNext())
		{
			Function f = iterator.next();
			if (function_name.equals(f.getName()))
			{
				return f;
			}
		}
		return null;
	}	
	

	ArrayList<Statement> genSequenceForFunction(ArrayList<Function> functions, String function_name, ArrayList<String> actual_params)
	{
		Statement statement;
		String statement_type,statement_code;
		ArrayList <Statement> sequence = new ArrayList<Statement>();
		FunctionCall fn_call;
		Function function = getFunctionDetails (functions, function_name);
		Iterator<Statement> function_body = function.getBody().iterator();
		Map<String, String> replacers = createMap(function.getParameters(), actual_params); 
		
		
		while (function_body.hasNext())
		{
			statement = function_body.next();
			statement_type = statement.getType();
			statement_code = replaceWithGivenReplacers(statement.getCode(), replacers);
			statement.setCode(statement_code);
			if (statement_type.equals("function_call"))
			{
				//handle function call
				// get function name and parameters
				fn_call = new FunctionCall();
				fn_call.getFunctionCallDetails(statement_code);
				ArrayList<Statement> fn_call_sequence = genSequenceForFunction (functions, fn_call.getFunction_name(), fn_call.getParameters()); 
				sequence.addAll(fn_call_sequence);
				
			}
			else if(statement_type.equals("exp"))
			{
				//handle exp
				sequence.add(statement);
			}
			else
			{
				//add everything else to sequence as it is
				sequence.add(statement);
			}
		}
		
		return sequence;
		
	}
	
	boolean statementHasVar (String statement, String var)
	{
		Pattern p_label = Pattern.compile(JavaGrammer.label);
		Matcher m_label = p_label.matcher(statement);
		while(m_label.find())
		{
			if (var.equals(m_label.group()))
			{
				return true;
			}
		}
		return false;
	}
	
	/*
	 *  If string is assignment like, x = x + 1 and var is v,
	 *  then returns `x + 1`
	 */
	Statement format_assignment(Statement statement, String var)
	{
		String code = statement.getCode();
		Pattern p_label = Pattern.compile(JavaGrammer.label);
		Pattern p_equal_to = Pattern.compile(JavaGrammer.equal_to);
		Matcher m_label = p_label.matcher(code);
		Matcher m_equal_to = p_equal_to.matcher(code);
		
		while(m_label.find())
		{
			if (var.equals(m_label.group()))
			{
				m_equal_to.region(m_label.end(), code.length());
				if (m_equal_to.find())
				{
					String s = code.substring(m_equal_to.end());
					statement.setCode(s);
				}
			}
		}
		return statement;
	}
	
	public ArrayList<Statement> genSequence(ArrayList<Function> functions, String start_fn, String var)
	{
		ArrayList <Statement> sequence = new ArrayList<Statement>();
		ArrayList <Statement> var_sequence = new ArrayList<Statement>();
		String statement_type, statement_code;
		boolean ignore_statement = false;
	
		sequence = genSequenceForFunction(functions, start_fn, new ArrayList<String>());
		
		for(int i = 0; i< sequence.size(); i++)
		{
			Statement s = sequence.get(i);
			statement_type = s.getType();
			statement_code = s.getCode();
	
			// if `if` cond is doing checking on some other var, then ignore_statement is true
			if(ignore_statement)
			{
				// if statement type is ignore & code is `}`, it means if/else block is getting closed and we should consider other statements 
				if(statement_type.equals("ignore") && statement_code.equals("}"))
				{
					// but if next statement is else, then we should ignore that block as well
					if (!sequence.get(i+1).getType().equals("cond-else"))
					{
						ignore_statement = false;
					}
				}
			}
			else
			{
				// if statement is if and it doesn't contains var we are looking for, set ingnore_statement = true	
				if(statement_type.equals("cond-if") && !statementHasVar(statement_code, var))
				{
					ignore_statement = true;
				}
				else
				{	
					if (statement_type.equals("ignore"))
					{
						var_sequence.add(s);
					}
					else if (statementHasVar(statement_code, var))
					{	
						if (!var_first_occurance)
						{ 
							var_sequence.add(format_assignment(s, var));
						}
						else
							var_first_occurance = false;
					}
				}	
			}	
		}
		Iterator<Statement> iterator = var_sequence.iterator();
		while(iterator.hasNext())
		{
			iterator.next().print();
		}
	return var_sequence;
	}
	
	/*
	 * Un-comment it it to check just working of sequence generator
	 */
	/*
	public static void main(String[] args) {
		Extraction e = new Extraction();
		ArrayList<Function> functions;
		e.extractFunctionsDetails("/Users/Shalaka/Documents/Quick-Gen/src/parser/example.java");
		SequenceGen s = new SequenceGen();
		s.genSequence(e.functions, "main" , "x");
	}*/


}

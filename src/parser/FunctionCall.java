package parser;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FunctionCall {
	String function_name;
	ArrayList<String> parameters;
	/**
	 * @return the function_name
	 */
	public String getFunction_name() {
		return function_name;
	}
	/**
	 * @param function_name the function_name to set
	 */
	public void setFunction_name(String function_name) {
		this.function_name = function_name;
	}
	/**
	 * @return the parameters
	 */
	public ArrayList<String> getParameters() {
		return parameters;
	}
	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(ArrayList<String> parameters) {
		this.parameters = parameters;
	}
	
	/*
	 * Given a string, returns function name & its parameter list
	 */
	public void getFunctionCallDetails(String s)
	{
		Pattern p_label = Pattern.compile(JavaGrammer.label);
		Pattern p_is_fun = Pattern.compile(JavaGrammer.opening_circular_brace);
		Matcher m_label = p_label.matcher(s);
		Matcher m_is_fun = p_is_fun.matcher(s);
		m_label.find();
		function_name = m_label.group();
		parameters = new ArrayList<String>();
		m_is_fun.find();
		m_label.region(m_is_fun.end(), s.length());
		while(m_label.find())
		{
			parameters.add(m_label.group());
		}
	}
}

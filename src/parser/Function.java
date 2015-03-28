package parser;

import java.util.ArrayList;
import java.util.Iterator;

public class Function {

	String name;
	Boolean has_return_type;
	ArrayList<String> parameters;
	ArrayList<Statement> body;

	/**
	 * @return the body
	 */
	public ArrayList<Statement> getBody() {
		return body;
	}


	/**
	 * @param body the body to set
	 */
	public void setBody(ArrayList<Statement> body) {
		this.body = body;
	}


	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * @return the has_return_type
	 */
	public Boolean getHas_return_type() {
		return has_return_type;
	}


	/**
	 * @param has_return_type the has_return_type to set
	 */
	public void setHas_return_type(Boolean has_return_type) {
		this.has_return_type = has_return_type;
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

	public void print()
	{
		System.out.println("******** Function *******");
		System.out.println("name: " + name);
		System.out.println("has_return type: " + has_return_type);
		System.out.println("parameters: " + parameters);
		System.out.println("Function body is => ");
		Iterator<Statement> iterator = body.iterator();
		while (iterator.hasNext())
		{
			iterator.next().print();
		}
	}
}

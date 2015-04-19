package parser;

import java.io.*;
import java.util.Stack;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import parser.JavaGrammer;

public class Extraction {
	static Scanner in = new Scanner(System.in);
	ArrayList<CodeLine> file_contents;
	int line_num = 0;
	int total_lines;
	int block_start, block_end;
	boolean is_fun;
	Pattern p_class_name = Pattern.compile(JavaGrammer.class_name);
	Pattern p_class = Pattern.compile(JavaGrammer.class1);
	Pattern p_data_type = Pattern.compile(JavaGrammer.datatype);
	Pattern p_label = Pattern.compile(JavaGrammer.label);
	Pattern p_is_fun = Pattern.compile(JavaGrammer.opening_circular_brace);
	Pattern p_block_start = Pattern.compile(JavaGrammer.block_start);
	Pattern p_block_end = Pattern.compile(JavaGrammer.block_end);
	Pattern p_if_cond = Pattern.compile(JavaGrammer.if_cond);
	Pattern p_else_cond = Pattern.compile(JavaGrammer.else_cond);
	Pattern p_opening_cir_brace = Pattern
			.compile(JavaGrammer.opening_circular_brace);
	Pattern p_closing_cir_brace = Pattern
			.compile(JavaGrammer.closing_circular_brace);
	ArrayList<Function> functions = new ArrayList<Function>();

	public ArrayList<CodeLine> readFileInArrayList(String file_path) {
		ArrayList<CodeLine> file_contents = new ArrayList<CodeLine>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(file_path));
			String line = br.readLine();
			int index = 1;
			while (line != null) {
				CodeLine cl = new CodeLine();
				cl.setIndex(index++);
				cl.setCode(line);
				file_contents.add(cl);
				line = br.readLine();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return file_contents;
	}

	public ArrayList<Function> extractFunctionsDetails(String filePath) {
		file_contents = readFileInArrayList(filePath);

		Matcher m_class = null;
		Matcher m_class_name = null;
		Matcher m_data_type = null;
		Matcher m_label = null;
		String class_name = null;
		Boolean found_class = false;
		int pos = 0;
		Boolean has_return_type = null;
		Function fn;

		ArrayList<Function> functions = new ArrayList<Function>();

		total_lines = file_contents.size();

		// process file line by line
		while (line_num < total_lines) {
			CodeLine c = file_contents.get(line_num++);
			String data = c.getCode().trim();
			pos = 0;
			// Find class-name if it is not already
			if (found_class != true) {
				class_name = get_class_name(data, m_class, m_class_name,
						p_class, p_class_name);
				if (class_name != null)
					// if found class, set found_class to true
					found_class = true;
			}
			// extract variable/function details
			else {
				// check for data-type declaration
				m_data_type = p_data_type.matcher(data);
				if (m_data_type.find()) {
					if (m_data_type.start() == 0) {
						pos = m_data_type.end(0);
						if (m_data_type.group().equals("void"))
							has_return_type = false;
						else
							has_return_type = true;
					} else
						has_return_type = false;
				}

				// Check for label(variable/function name)
				m_label = p_label.matcher(data);
				m_label.region(pos, data.length());
				if (m_label.find()) {
					String label = m_label.group();
					// Check if that label is function name
					int label_is_function = is_label_function(p_is_fun,
							m_label.end(0), data);
					if (label_is_function != 0) {
						pos = label_is_function;
						fn = new Function();
						fn.setName(label);
						fn.setHas_return_type(has_return_type);
						// get function parameters
						fn.setParameters(get_function_parameters(pos, data,
								m_label));
						ArrayList<Statement> body = processFunctionBody(m_label);
						fn.setBody(body);
						functions.add(fn);
					}
				}
			}
		}
		return functions;
	}

	private ArrayList<Statement> processFunctionBody(Matcher m_label) {
		// as after getting function name, we need to process same line also,
		// but line_num is getting incremented in calling function before this
		// call.
		// line_num--;
		int block_start_after = line_num - 1;
		get_block_boundries(block_start_after);
		int fn_start = block_start;
		int fn_end = block_end;
		String data = file_contents.get(fn_start).getCode();
		int pos = 0;
		Matcher m_block_start = p_block_start.matcher(data);
		ArrayList<Statement> statements = new ArrayList<Statement>();
		Statement s;
		/*
		 * Check if function body curly braces start, if starts on function
		 * declaration line, move to next line, if function body 1st statement
		 * starts on same line where brace is present, ignore that brace and
		 * read code of that line.
		 */
		if (m_block_start.find()) {
			if (m_block_start.end() == data.length())
				fn_start++;
			else
				pos = m_block_start.end();
		}

		for (int ln_num = fn_start; ln_num < fn_end; ln_num++) {

			data = file_contents.get(ln_num).getCode();
			// Check for `if` condition
			// System.out.println ("line num is " + ln_num + "fn_start is " +
			// fn_start);
			String cond = ifHasIfCond_Getcond(data, pos);
			if (cond != null) {
				// create statement obj for cond
				try {
					get_block_boundries(ln_num);
				} catch (Exception e) {
					block_start = 0;
				}
				s = new Statement();
				s.setType("cond-if");
				s.setCode(cond);
				s.setLine_no(ln_num);
				s.setIndex(ln_num - fn_start);
				if (block_start != 0 && block_start <= ln_num + 1) {
					s.setBlock_start_line_no(block_start);
					s.setBlock_end_line_no(block_end);
					s.setBlock_start_index(block_start - fn_start);
					s.setBlock_end_index(block_end - fn_start);
				}
			}
			// check for `else` part
			else if (hasElseCond(data)) {
				s = new Statement();
				try {
					get_block_boundries(ln_num);
				} catch (Exception e) {
					block_start = 0;
				}
				s.setType("cond-else");
				s.setCode("else");
				s.setLine_no(ln_num);
				s.setIndex(ln_num - fn_start);
				if (block_start != 0 && block_start <= ln_num + 1) {
					s.setBlock_start_line_no(block_start);
					s.setBlock_end_line_no(block_end);
					s.setBlock_start_index(block_start - fn_start);
					s.setBlock_end_index(block_end - fn_start);
				}
			}
			// check if line has opening brackets for block start
			else if (lineHasBlockStart(data)) {
				s = new Statement();
				s.setType("ignore");
				s.setCode("{");
				s.setLine_no(ln_num);
				s.setIndex(ln_num - fn_start);
			}
			// check if line has closing bracket for block end
			else if (lineHasBlockEnd(data)) {
				s = new Statement();
				s.setType("ignore");
				s.setCode("}");
				s.setLine_no(ln_num);
				s.setIndex(ln_num - fn_start);
			}
			// if it line has `(` then it has function call
			else if (is_label_function(p_is_fun, 0, data) != 0) {
				s = new Statement();
				s.setType("function_call");
				data = removeWhiteSpaceAndSemiColon(data);
				s.setCode(data);
				s.setLine_no(ln_num);
				s.setIndex(ln_num - fn_start);
			}
			// if it is none of the above then add it as exp
			else {
				s = new Statement();
				s.setType("exp");
				data = removeWhiteSpaceAndSemiColon(data);
				s.setCode(data);
				s.setLine_no(ln_num);
				s.setIndex(ln_num - fn_start);
			}
			statements.add(s);
		}
		line_num = block_end;
		return statements;
	}

	private String removeWhiteSpaceAndSemiColon(String data) {
		data = data.trim();
		data = data.replace(";", "");
		return data;
	}

	private boolean hasElseCond(String data) {
		Matcher m_else_cond = p_else_cond.matcher(data);
		if (m_else_cond.find())
			return true;
		return false;
	}

	private boolean lineHasBlockEnd(String data) {
		Matcher m_block_end = p_block_end.matcher(data);
		if (m_block_end.find())
			return true;
		return false;
	}

	private boolean lineHasBlockStart(String data) {
		Matcher m_block_start = p_block_start.matcher(data);
		if (m_block_start.find())
			return true;
		return false;
	}

	/*
	 * If finds `if` word then
	 * 
	 * @returns if condition as string string
	 */
	private String ifHasIfCond_Getcond(String data, int pos) {
		// TODO Auto-generated method stub
		Matcher m_if_cond = p_if_cond.matcher(data);
		if (m_if_cond.find()) {
			Matcher m_opening_cir_brace = p_opening_cir_brace.matcher(data);
			Matcher m_closing_cir_brace = p_closing_cir_brace.matcher(data);
			m_opening_cir_brace.find();
			m_closing_cir_brace.find();
			return data.substring(m_opening_cir_brace.end(),
					m_closing_cir_brace.end() - 1);
		}
		return null;
	}

	/*
	 * Given a line number after which block will start, finds the block start
	 * and block end line number and sets block_start and block_end variables.
	 */
	private void get_block_boundries(int start_index) {
		Stack<Integer> s = new Stack<Integer>();
		int index = start_index, pos = 0;
		String data = null;
		Matcher m_block_start = null;
		Matcher m_block_end = null;
		// find block start line i.e,`{` brace
		while (s.empty()) {
			data = file_contents.get(index).getCode();
			m_block_start = p_block_start.matcher(data);
			if (m_block_start.find()) {
				s.push(index);
				block_start = index;
				pos = m_block_start.end();
			} else {
				index++;
			}
		}
		data = file_contents.get(index).getCode();
		m_block_end = p_block_end.matcher(data);
		while (!s.empty()) {
			int line_length = data.length();
			Boolean block_start_flag = false;
			Boolean block_end_flag = false;
			m_block_start.region(pos, line_length);
			m_block_end.region(pos, line_length);
			// if new block start, push it on stack
			if (m_block_start.find()) {
				block_start_flag = true;
				pos = m_block_start.end();
				m_block_start.region(pos, line_length);
				m_block_end.region(pos, line_length);
				s.push(index);
			}
			// if block end, pop from stack
			if (m_block_end.find()) {
				block_end_flag = true;
				pos = m_block_end.end();
				m_block_start.region(pos, line_length);
				m_block_end.region(pos, line_length);
				s.pop();
				block_end = index;
			}
			// if no block start or block end, move to next line
			if (!block_start_flag || !block_end_flag) {
				index++;
				pos = 0;
				data = file_contents.get(index).getCode();
				m_block_start = p_block_start.matcher(data);
				m_block_end = p_block_end.matcher(data);
			}
		}
	}

	private ArrayList<String> get_function_parameters(int start_pos,
			String data, Matcher m_label) {
		// m_label.find(start_pos);
		// flag will be 1 when function will get any data_type and
		// it will be 0 when function get variable name.
		int flag = 0;
		ArrayList<String> parameters = new ArrayList<String>();
		while (m_label.find()) {
			if (flag == 1) {
				parameters.add(m_label.group());
				flag--;
			} else
				flag++;
		}
		return parameters;
	}

	/*
	 * Checks if given label is function
	 * 
	 * @returns position of opening brace if label is fn name else returns 0
	 */
	private int is_label_function(Pattern p_is_fun, int start_pos, String data) {
		Matcher m_is_fun;
		m_is_fun = p_is_fun.matcher(data);
		m_is_fun.region(start_pos, data.length());
		if (m_is_fun.find()) {
			return m_is_fun.end();
		} else
			return 0;
	}

	private String get_class_name(String data, Matcher m_class,
			Matcher m_class_name, Pattern p_class, Pattern p_class_name) {
		m_class = p_class.matcher(data);
		m_class_name = p_class_name.matcher(data);
		if (m_class.find()) {
			m_class_name.region(m_class.end(), data.length());
			m_class_name.find();
			return m_class_name.group();
		} else
			return null;
	}

	public static void main(String[] args) {
		Extraction e = new Extraction();
		e.extractFunctionsDetails("/Users/Shalaka/Documents/Quick-Gen/src/parser/example.java");

	}
}

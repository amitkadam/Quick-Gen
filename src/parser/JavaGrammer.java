package parser;

public class JavaGrammer {
	static String class1="class";
	static String class_name = "[a-zA-Z][a-zA-Z0-9_$]*";
	static String if_cond = "if";
	static String else_cond = "else";
	static String keyword =" for| while| do| System|out|println|for";
	static String datatype = " boolean|char|byte|short|long|float|double|void|int ";
	static String extra="public|private|protected|abstract|final|static|const";
	static String class_end = "}";
	static String extend ="extends";
	static String implement ="implements";
	static String any ="[a-z A-Z0-9_$\\.,\\*]*";
	static String label = "[a-zA-Z0-9_$]+";
	static String opening_circular_brace = "\\(";
	static String fun_call = "\\.";
	static String closing_circular_brace = "\\)";
	static String block_start = "\\{";
	static String block_end = "\\}";
	static String mem_allocation = "new";
	static String template="[<>]";
	static String end_statment=";";
	static String new_statment="\\n";
	public static String listenerMethod[]={"getActionCommand","itemStateChanged"};
	public static String ignoreMethod[]={"equals"};}

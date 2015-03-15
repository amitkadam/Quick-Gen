package parser;

public class Statement {
	String type;
	String code;
	int line_no; //This line number in file
	int index;   //This is index of statement in function
	int block_start_line_no;
	int block_end_line_no;
	int block_start_index;
	int block_end_index;
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * @return the line_no
	 */
	public int getLine_no() {
		return line_no;
	}
	/**
	 * @param line_no the line_no to set
	 */
	public void setLine_no(int line_no) {
		this.line_no = line_no;
	}
	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}
	/**
	 * @param index the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}
	/**
	 * @return the block_start_line_no
	 */
	public int getBlock_start_line_no() {
		return block_start_line_no;
	}
	/**
	 * @param block_start_line_no the block_start_line_no to set
	 */
	public void setBlock_start_line_no(int block_start_line_no) {
		this.block_start_line_no = block_start_line_no;
	}
	/**
	 * @return the block_end_line_no
	 */
	public int getBlock_end_line_no() {
		return block_end_line_no;
	}
	/**
	 * @param block_end_line_no the block_end_line_no to set
	 */
	public void setBlock_end_line_no(int block_end_line_no) {
		this.block_end_line_no = block_end_line_no;
	}
	/**
	 * @return the block_start_index
	 */
	public int getBlock_start_index() {
		return block_start_index;
	}
	/**
	 * @param block_start_index the block_start_index to set
	 */
	public void setBlock_start_index(int block_start_index) {
		this.block_start_index = block_start_index;
	}
	/**
	 * @return the block_end_index
	 */
	public int getBlock_end_index() {
		return block_end_index;
	}
	/**
	 * @param block_end_index the block_end_index to set
	 */
	public void setBlock_end_index(int block_end_index) {
		this.block_end_index = block_end_index;
	}
	
	
}

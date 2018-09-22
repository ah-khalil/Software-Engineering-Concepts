package edu.curtin.comp3003.filesearcher;

import java.util.*;

public class DataElement extends QueueElement{
	private String file_name;

	public DataElement(String file_name){
		this.file_name = file_name;
	}

	public String get_file_name(){
		return this.file_name;
	}
}
package edu.brandeis.cs.lappsgrid.stanford.corenlp;

import org.lappsgrid.api.Data;

import edu.brandeis.cs.lappsgrid.stanford.corenlp.api.IParser;

public class Parser extends AbstractStanfordCoreNLPWebService implements
		IParser {

	public Parser() {
		this.init(PROP_PARSE);
	}

	@Override
	public long[] requires() {
		return TYPES_REQUIRES;
	}

	@Override
	public long[] produces() {
		return TYPES_PRODUCES;
	}

	
	@Override
	public Data configure(Data arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Data execute(Data arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String parse(String sentence) {
		// TODO Auto-generated method stub
		return null;
	}

}

package edu.brandeis.cs.lappsgrid.stanford.corenlp;

import org.lappsgrid.api.Data;

import edu.brandeis.cs.lappsgrid.stanford.corenlp.api.ISplitter;

public class Splitter extends AbstractStanfordCoreNLPWebService implements
		ISplitter {

	public Splitter() {
		this.init(PROP_SENTENCE_SPLIT);
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

}

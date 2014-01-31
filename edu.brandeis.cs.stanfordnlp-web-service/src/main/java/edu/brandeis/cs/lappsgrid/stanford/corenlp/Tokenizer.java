package edu.brandeis.cs.lappsgrid.stanford.corenlp;

import org.lappsgrid.api.Data;

import edu.brandeis.cs.lappsgrid.stanford.corenlp.api.ITokenizer;

public class Tokenizer extends AbstractStanfordCoreNLPWebService implements
		ITokenizer {

	public Tokenizer() {
		this.init(PROP_TOKENIZE);
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
	public Data execute(Data arg0) {
		// TODO Auto-generated method stub
		return null;
	}


}

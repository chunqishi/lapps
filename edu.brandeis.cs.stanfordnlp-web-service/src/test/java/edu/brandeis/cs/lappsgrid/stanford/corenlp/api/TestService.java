package edu.brandeis.cs.lappsgrid.stanford.corenlp.api;


import junit.framework.Assert;
import org.anc.lapps.serialization.Container;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.lappsgrid.api.Data;
import org.lappsgrid.core.DataFactory;
import org.lappsgrid.discriminator.Types;

import java.io.IOException;
import java.io.StringReader;

/**
 * Created by Chunqi SHI (shicq@cs.brandeis.edu) on 3/5/14.
 */
public class TestService {

    public static final String PAYLOAD_RESOURCE = "payload.txt";

    protected java.lang.String payload = null;
    protected Data data = null;
    protected Data ret = null;
    protected Container container = null;

    @Before
    public void data() throws IOException {
        java.io.InputStream in =  this.getClass().getClassLoader().getResourceAsStream(PAYLOAD_RESOURCE);
        payload = IOUtils.toString(in);
        data = DataFactory.json(payload);
        container = new Container(payload);
    }

    @Test
    public void test() {
//        System.out.println(payload);
        Assert.assertEquals("", payload, data.getPayload());
        Assert.assertEquals("", Types.JSON, data.getDiscriminator());
//        System.out.println(container.getText());
    }

}

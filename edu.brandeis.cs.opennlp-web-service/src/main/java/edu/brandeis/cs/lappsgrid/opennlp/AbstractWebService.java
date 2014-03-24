package edu.brandeis.cs.lappsgrid.opennlp;

import edu.brandeis.cs.lappsgrid.api.opennlp.IVersion;
import org.anc.lapps.serialization.Container;
import org.lappsgrid.api.Data;
import org.lappsgrid.api.LappsException;
import org.lappsgrid.api.WebService;
import org.lappsgrid.discriminator.DiscriminatorRegistry;
import org.lappsgrid.discriminator.Types;

import java.util.Map;

/**
 * Created by shicq on 3/6/14.
 */
public abstract class AbstractWebService implements WebService , IVersion {
    protected static void putFeature(Map mapFeature, String key, Object obj) {
        if (key != null && obj != null) {
            mapFeature.put(key, obj.toString());
        }
    }

    public static final Container getContainer(Data input) throws LappsException
    {
        long type = input.getDiscriminator();
        if (type == Types.ERROR) {
            // Data objects with an ERROR discriminator should not be
            // passed in.
            throw new LappsException(input.getPayload());
        }
        else if (type == Types.TEXT) {
            Container container = new Container();
            container.setText(input.getPayload());
            return container;
        }
        else if (type == Types.JSON) {
            return new Container(input.getPayload());
        }
        String typeName = DiscriminatorRegistry.get(type);
        throw new LappsException("Unexpected Data object type: " + typeName);
    }


}

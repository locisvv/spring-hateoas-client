package com.svv.jaxrs;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.fasterxml.jackson.jaxrs.xml.JacksonXMLProvider;
import com.svv.providers.HalJsonMapperProvider;
import com.svv.providers.HalJsonRepresentationTypeProvider;
import com.svv.providers.HalXmlMapperProvider;
import com.svv.providers.HalXmlRepresentationTypeProvider;
import org.glassfish.jersey.client.ClientProperties;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

/**
 * @author Vasyl Spachynskyi
 * @version $Id:
 * @since 09.12.2015
 */
public class RestClient {
    private WebTarget rootTarget;

    public RestClient() {
        Client client = ClientBuilder.newBuilder().build();
        client.property(ClientProperties.CONNECT_TIMEOUT, 200);
        client.property(ClientProperties.READ_TIMEOUT, 200);

        rootTarget = client.target("http://localhost:8080/rest/");

        JacksonJsonProvider halJsonProvider = new HalJsonRepresentationTypeProvider();
        halJsonProvider.setMapper(new HalJsonMapperProvider().getContext(null));

        JacksonXMLProvider halXmlProvider = new HalXmlRepresentationTypeProvider();
        halXmlProvider.setMapper(new HalXmlMapperProvider().getContext(null));

        rootTarget
                .register(halJsonProvider)
                .register(halXmlProvider)
                .register(HalJsonMessageBodyReader.class);
    }

    public WebTarget getRootTarget() {
        return rootTarget;
    }
}
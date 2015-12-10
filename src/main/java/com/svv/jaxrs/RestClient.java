package com.svv.jaxrs;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;
import com.sun.jersey.multipart.impl.MultiPartWriter;
import com.svv.providers.CustomRepresentationTypeProvider;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.springframework.hateoas.core.DefaultRelProvider;
import org.springframework.hateoas.hal.Jackson2HalModule;

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

        JacksonJsonProvider customRepresentationTypeProvider = new CustomRepresentationTypeProvider();
        customRepresentationTypeProvider.setMapper(getHallMapper());

        rootTarget = client.target("http://localhost:8080/rest/");

        rootTarget
                .register(customRepresentationTypeProvider)
                .register(JacksonFeature.class)
                .register(MultiPartWriter.class);
    }

    private ObjectMapper getHallMapper() {
        ObjectMapper halObjectMapper = new ObjectMapper();
        halObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        halObjectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        AnnotationIntrospector primary = new JaxbAnnotationIntrospector();
        AnnotationIntrospector secondary = new JacksonAnnotationIntrospector();
        AnnotationIntrospector pair = AnnotationIntrospector.pair(secondary, primary);
        halObjectMapper.setAnnotationIntrospector(pair);

        halObjectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        halObjectMapper
                .setHandlerInstantiator(new Jackson2HalModule.
                        HalHandlerInstantiator(new DefaultRelProvider(), null, false));

        Jackson2HalModule jackson2HalModule = new Jackson2HalModule();
//        jackson2HalModule.addDeserializer(Resource.class, new HalResourceDeserializer());

        halObjectMapper.registerModule(jackson2HalModule);

        return halObjectMapper;
    }

    public WebTarget getRootTarget() {
        return rootTarget;
    }
}

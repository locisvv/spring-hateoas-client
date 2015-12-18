package com.svv.providers;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
/**
 * @author Vasyl Spachynskyi
 * @version $Id:
 * @since 16.12.2015
 */

@Provider
@Consumes({
        "application/json",
        "application/hal+json",
        "text/json"})
@Produces({
        "application/json",
        "application/hal+json",
        "text/json"})
public class HalJsonRepresentationTypeProvider extends JacksonJaxbJsonProvider {

    @Override
    public boolean isReadable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return super.isReadable(aClass, type, annotations, mediaType);
    }

    @Override
    public boolean isWriteable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return super.isWriteable(aClass, type, annotations, mediaType);
    }
}

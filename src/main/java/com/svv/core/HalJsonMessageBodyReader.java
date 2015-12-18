package com.svv.core;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.svv.providers.HalJsonMapperProvider;
import org.reflections.Reflections;
import org.springframework.hateoas.hal.Jackson2HalModule;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

/**
 * @author Vasyl Spachynskyi
 * @version $Id:
 * @since 17.12.2015
 */
@Consumes({"application/hal+json"})
public class HalJsonMessageBodyReader implements MessageBodyReader<HalResource> {

    private Map<String, Class> rootRelationMap;
    private HalJsonMapperProvider mapperProvider;
    private Jackson2HalModule.HalLinkListDeserializer linkDeserializer;

    public HalJsonMessageBodyReader() {
        mapperProvider = new HalJsonMapperProvider();
        linkDeserializer = new Jackson2HalModule.HalLinkListDeserializer();
        rootRelationMap = getRelationsByTypeAnnotation();
    }

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return type == HalResource.class;
    }

    @Override
    public HalResource readFrom(Class<HalResource> type, Type genericType, Annotation[] annotations, MediaType mediaType,
                                MultivaluedMap<String, String> httpHeaders, InputStream inputStream)
            throws IOException, WebApplicationException {

        Class entityType = ((Class) ((ParameterizedTypeImpl) genericType).getActualTypeArguments()[0]);
        ObjectMapper objectMapper = mapperProvider.getContext(type);

        JsonParser jp = objectMapper.getFactory().createParser(inputStream);
        initForReading(jp);

        return parseHalResource(objectMapper, jp, entityType);
    }

    private HalResource parseHalResource(ObjectMapper objectMapper, JsonParser jp, Class entityType) throws IOException {
        HalResource halResource = new HalResource();

        jp.nextToken();

        TokenBuffer jsonBuffer = new TokenBuffer(jp);
        jsonBuffer.writeStartObject();

        for (; jp.getCurrentToken() != JsonToken.END_OBJECT; jp.nextToken()) {
            String propName = jp.getCurrentName();

            jp.nextToken();

            if (propName.equals("_links")) {
                halResource.add(linkDeserializer.deserialize(jp, objectMapper.getDeserializationContext()));
            } else if (propName.equals("_embedded")) {
                halResource.setContent(parseEmbedded(objectMapper, jp, entityType));
            }
            jsonBuffer.writeFieldName(propName);
            jsonBuffer.copyCurrentStructure(jp);
        }
        jsonBuffer.writeEndObject();

        halResource.setEntity(objectMapper.readValue(jsonBuffer.asParser(), entityType));

        return halResource;
    }

    private Map<String, Object> parseEmbedded(ObjectMapper objectMapper, JsonParser jp, Class entityType) throws IOException {
        Map<String, Type> relMap = createRelationMap(entityType);
        Map<String, Object> result = new HashMap<String, Object>();

        // links is an object, so we parse till we find its end.
        while (!JsonToken.END_OBJECT.equals(jp.nextToken())) {
            if (!JsonToken.FIELD_NAME.equals(jp.getCurrentToken())) {
                throw new JsonParseException("Expected relation name", jp.getCurrentLocation());
            }

            String relation = jp.getCurrentName();
            Class embeddedType = getEmbeddedType(relation, relMap);
            Object value;
            if (JsonToken.START_ARRAY.equals(jp.nextToken())) {
                List<HalResource> halResources = new ArrayList<HalResource>();
                while (!JsonToken.END_ARRAY.equals(jp.nextToken())) {
                    halResources.add(parseHalResource(objectMapper, jp, embeddedType));
                }
                value = halResources;
            } else {
                value = parseHalResource(objectMapper, jp, embeddedType);
            }
            result.put(relation, value);
        }
        return result;
    }

    private void initForReading(JsonParser jp) throws IOException {
        if (jp.getCurrentToken() == null) {
            if (jp.nextToken() == null) {
                throw JsonMappingException.from(jp, "No content to map due to end-of-input");
            }
        }
    }

    private Map<String, Type> createRelationMap(Class entityType) {
        Map<String, Type> relMap = new HashMap<String, Type>();

        for (Method method : entityType.getMethods()) {
            if (method.getAnnotation(Relation.class) != null) {
                relMap.put(method.getAnnotation(Relation.class).value(), method.getGenericReturnType());
            }
        }
        return relMap;
    }

    private Map<String, Class> getRelationsByTypeAnnotation() {
        Map<String, Class> result = new HashMap<String, Class>();

        Reflections reflections = new Reflections("com.svv.dto");
        Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(Relation.class);
        for (Class<?> aClass : annotatedClasses) {
            Relation annotation = aClass.getAnnotation(Relation.class);
            result.put(annotation.value(), aClass);
            result.put(annotation.collection(), aClass);
        }
        return result;
    }

    public Class getEmbeddedType(String relation, Map<String, Type> relMap) {

        if (relMap.get(relation) != null) {
            if (relMap.get(relation) instanceof ParameterizedTypeImpl) {
                ParameterizedTypeImpl relationType = (ParameterizedTypeImpl) relMap.get(relation);
                if (Collection.class.isAssignableFrom(relationType.getRawType())) {
                    return (Class) relationType.getActualTypeArguments()[0];
                }
                return relationType.getRawType();
            } else {
                return (Class)relMap.get(relation);
            }
        }

        return rootRelationMap.get(relation);
    }
}
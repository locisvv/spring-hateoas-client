package com.svv.core;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.ContainerDeserializerBase;

import java.io.IOException;

/**
 * @author Vasyl Spachynskyi
 * @version $Id:
 * @since 17.12.2015
 */
public class HalResourceDeserializer extends ContainerDeserializerBase<HalResource> implements
        ContextualDeserializer {

    private JavaType contentType;

    public HalResourceDeserializer() {
        this(HalResource.class, null);
    }

    public HalResourceDeserializer(JavaType vc) {
        this(null, vc);
    }

    @SuppressWarnings("deprecation")
    private HalResourceDeserializer(Class<?> type, JavaType contentType) {
        super(type);
        this.contentType = contentType;
    }

    @Override
    public JavaType getContentType() {
        return null;
    }

    @Override
    public JsonDeserializer<Object> getContentDeserializer() {
        return null;
    }

    @Override
    public HalResource deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        return null;
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property)
            throws JsonMappingException {

        JavaType vc = property.getType().getContentType();
        return new HalResourceDeserializer(vc);
    }
}

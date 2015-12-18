package com.svv.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Arrays;
import java.util.Map;

/**
 * @author Vasyl Spachynskyi
 * @version $Id:
 * @since 26.11.2015
 */
@XmlRootElement(name = "resource")

public class HalResource<EntityType> extends ResourceSupport {

    @JsonUnwrapped
    private EntityType entity;
    private Map<String, Object> content;

    public HalResource() {
    }

    public HalResource(Link... links) {
        add(Arrays.asList(links));
    }

    public EntityType getEntity() {
        return entity;
    }

    public void setEntity(EntityType entity) {
        this.entity = entity;
    }

    @XmlElement(name = "embedded")
    @JsonProperty("_embedded")
    @JsonDeserialize(using = HalResourceDeserializer.class)
    public Map<String, Object> getContent() {
        return content;
    }

    public void setContent(Map<String, Object> content) {
        this.content = content;
    }
}
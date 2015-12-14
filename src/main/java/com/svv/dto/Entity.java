package com.svv.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * @author Vasyl Spachynskyi
 * @version $Id:
 * @since 10.12.2015
 */

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")

@JsonSubTypes({
        @JsonSubTypes.Type(value = Album.class, name = "album"),
        @JsonSubTypes.Type(value = Artist.class, name = "artist")
})
public abstract class Entity {
}

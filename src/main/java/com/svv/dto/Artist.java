package com.svv.dto;

/**
 * Created by vsabadosh on 17/11/15.
*/

import com.svv.core.Relation;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Music Artist/Group.
 */
@XmlRootElement()
@Relation(value = "artist", collection = "artists")
public class Artist extends ClientResource {

    private String id;
    private String name;

    public Artist(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Artist() {}

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}

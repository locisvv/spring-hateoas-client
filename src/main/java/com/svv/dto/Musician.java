package com.svv.dto;

import com.svv.core.Relation;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Vasyl Spachynskyi
 * @version $Id:
 * @since 15.12.2015
 */

@XmlRootElement
@Relation(value = "musician", collection = "musicians")
public class Musician extends ClientResource {

    private long id;
    private String name;
    private String instrument;


    public Musician() {}

    public Musician(long id, String name, String instrument) {
        this.id = id;
        this.name = name;
        this.instrument = instrument;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstrument() {
        return instrument;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }
}
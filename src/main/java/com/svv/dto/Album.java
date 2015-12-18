package com.svv.dto;

import com.svv.core.Relation;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by vsabadosh on 17/11/15.
 */
@XmlRootElement
public class Album extends ClientResource {

    private String id;
    private String title;
    private int stockLevel;
    private Artist artist;
    private List<Musician> musicians;

    public Album() {}

    public Album(String id, String title, int stockLevel) {
        this.id = id;
        this.title = title;
        this.stockLevel = stockLevel;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    @Relation(value = "artist")
    public Artist getArtist() {
        return artist;
    }

    public int getStockLevel() {
        return stockLevel;
    }

    public void setStockLevel(int stockLevel) {
        this.stockLevel = stockLevel;
    }

    @XmlElementWrapper(name = "musicians")
    @XmlElement(name = "musician")
    @Relation("musicians")
    public List<Musician> getMusicians() {
        return musicians;
    }

    public void setMusicians(List<Musician> musicians) {
        this.musicians = musicians;
    }

    public Album copyAlbum() {
        return new Album(id, title, stockLevel);
    }
}
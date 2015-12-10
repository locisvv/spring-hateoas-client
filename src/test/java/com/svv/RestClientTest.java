package com.svv;

import com.svv.dto.Album;
import com.svv.dto.Artist;
import com.svv.jaxrs.HalResource;
import com.svv.jaxrs.RestClient;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import javax.ws.rs.core.GenericType;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.Assert.*;

/**
 * @author Vasyl Spachynskyi
 * @version $Id:
 * @since 09.12.2015
 */
public class RestClientTest {

    private static final String ARTIST_URI = "http://localhost:8080/rest/artist/1";
    private static final String ALBUM_URI = "http://localhost:8080/rest/albums/1";
    private static RestClient restClient;

    @BeforeClass
    public static void init() {
        restClient = new RestClient();
    }

    @Test
    public void getFirstArtistTest() {
        Resource<Artist> actualArtist = restClient.getRootTarget().path("artist/1").request().accept("application/hal+json")
                .get(new GenericType<Resource<Artist>>() {});

        Artist artist = new Artist("1", "Opeth");
        Link self = new Link(ARTIST_URI);
        Link my = new Link(ARTIST_URI, "my");
        Resource<Artist> expectedArtist = new Resource<Artist>(artist, self, my);

        assertEquals(expectedArtist.getLinks(), actualArtist.getLinks());
        assertEquals(expectedArtist.getContent().getName(), actualArtist.getContent().getName());
    }

    @Test
    public void getFirstAlbumTest() {
        HalResource<Album, Artist> actualArtist = restClient.getRootTarget().path("albums/1").request()
                .accept("application/hal+json").get(new GenericType<HalResource<Album, Artist>>() {});

        Album album = new Album("1", "Heritage", "1", 2);
        Link self = new Link(ALBUM_URI);
        Link artistLink = new Link(ARTIST_URI, "artist");

        HalResource<Album, Artist> expectedArtist = new HalResource(new ArrayList(), self, artistLink);
        expectedArtist.setEntity(album);

        assertEquals(expectedArtist.getLinks(), actualArtist.getLinks());
        assertEquals(expectedArtist.getEntity().getTitle(), actualArtist.getEntity().getTitle());
        assertTrue(actualArtist.getContent().isEmpty());
    }

    @Test
    public void getFirstAlbumWithEmbeddedTest() {
        HalResource<Album, Artist> actualArtist = restClient.getRootTarget().path("albums/1").queryParam("embedded", true)
                .request().accept("application/hal+json").get(new GenericType<HalResource<Album, Artist>>() {});

        Artist artist = new Artist("1", "Opeth");
        Album album = new Album("1", "Heritage", "1", 2);
        Link self = new Link(ALBUM_URI);
        Link artistLink = new Link(ARTIST_URI, "artist");

        HalResource<Album, Artist> expectedArtist = new HalResource(Collections.singleton(artist), self, artistLink);
        expectedArtist.setEntity(album);

        assertEquals(expectedArtist.getLinks(), actualArtist.getLinks());
        assertEquals(expectedArtist.getEntity().getTitle(), actualArtist.getEntity().getTitle());
        assertEquals(expectedArtist.getContent().iterator().next().getName(), actualArtist.getContent().iterator().next().getName());
    }

}
package com.svv;

import com.svv.dto.Album;
import com.svv.dto.Artist;
import com.svv.jaxrs.HalResource;
import com.svv.jaxrs.RestClient;
import org.springframework.hateoas.Resource;

import javax.ws.rs.core.GenericType;

/**
 * @author Vasyl Spachynskyi
 * @version $Id:
 * @since 09.12.2015
 */
public class Main {
    public static void main(String[] args) {
        RestClient restClient = new RestClient();

        restClient.getRootTarget().path("artist/1").request().accept("application/hal+json")
                .get(new GenericType<Resource<Artist>>() {});

        restClient.getRootTarget().path("albums/1").queryParam("embedded",true).request().accept("application/hal+json")
                .get(new GenericType<HalResource<Album,Artist>>() {});

        restClient.getRootTarget().path("albums/1").request().accept("application/hal+json")
                .get(new GenericType<HalResource<Album,Artist>>() {});
    }
}

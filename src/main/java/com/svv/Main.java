package com.svv;

import com.svv.dto.Album;
import com.svv.jaxrs.HalResource;
import com.svv.jaxrs.RestClient;

import javax.ws.rs.core.GenericType;

/**
 * @author Vasyl Spachynskyi
 * @version $Id:
 * @since 09.12.2015
 */
public class Main {
    public static void main(String[] args) {
        RestClient restClient = new RestClient();

        restClient.getRootTarget().path("albums/1").queryParam("embedded", true).request().accept("application/hal+json")
                .get(new GenericType<HalResource<Album>>() {});

        restClient.getRootTarget().path("albums/1").queryParam("embedded", true).request().accept("application/hal+xml")
                .get(new GenericType<Album>() {});
    }
}
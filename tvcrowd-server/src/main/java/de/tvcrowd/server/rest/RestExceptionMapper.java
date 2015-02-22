/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.tvcrowd.server.rest;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import org.glassfish.grizzly.utils.Exceptions;

/**
 *
 * @author Marcel
 */
@Provider
public class RestExceptionMapper implements
        ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception ex) {
        ex.printStackTrace();
        return Response.status(500).entity(Exceptions.getStackTraceAsString(ex)).type("text/plain").build();
    }
}

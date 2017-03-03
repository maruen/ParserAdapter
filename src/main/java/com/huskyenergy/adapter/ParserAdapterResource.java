/*
 *    Licensed Materials - Property of IBM
 *    5725-I43 (C) Copyright IBM Corp. 2015, 2016. All Rights Reserved.
 *    US Government Users Restricted Rights - Use, duplication or
 *    disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 */

package com.huskyenergy.adapter;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.ibm.mfp.adapter.api.ConfigurationAPI;
import com.ibm.mfp.adapter.api.OAuthSecurity;

@Api(value = "Sample Adapter Resource")
@Path("/resource")
public class ParserAdapterResource {

    static Logger logger = Logger.getLogger(ParserAdapterResource.class.getName());

    @Context
    ConfigurationAPI configApi;

    @ApiOperation(value = "Returns 'Hello from resource'", 
                  notes = "A basic example of a resource returning a constant string.")
    
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Hello message returned") })
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getResourceData() {
        logger.info("Logging info message...");
        return "Hello from resource";
    }

    @ApiOperation(value = "Query Parameter Example",
                  notes = "Example of passing query parameters to a resource. Returns a greeting containing the name that was passed in the query parameter.")
   
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Greeting message returned") })
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/greet")
    public String helloUser( @ApiParam(value = "Name of the person to greet", required = true) 
                             @QueryParam("name") String name) {
        
        return "Hello " + name + "!";
    }

    @ApiOperation(value = "Multiple Parameter Types Example",
                  notes = "Example of passing parameters using 3 different methods: path parameters, headers, and form parameters. A JSON object containing all the received parameters is returned.")
    
    @ApiResponses(value = { @ApiResponse(code = 200, message = "A JSON object containing all the received parameters returned.") })
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{path}")
    public Map<String, String> enterInfo(
            @ApiParam(value = "The value to be passed as a path parameter", required = true) @PathParam("path") String path,
            @ApiParam(value = "The value to be passed as a header"        , required = true) @HeaderParam("Header") String header,
            @ApiParam(value = "The value to be passed as a form parameter", required = true) @FormParam("form") String form) {
        
        Map<String, String> result = new HashMap<String, String>();

        result.put("path", path);
        result.put("header", header);
        result.put("form", form);

        return result;
    }

    @ApiOperation(value = "Configuration Example",
                  notes = "Example usage of the configuration API. A property name is read from the query parameter, and the value corresponding to that property name is returned.")
   
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Property value returned."),
                            @ApiResponse(code = 404, message = "Property value not found.") })
    @GET
    @Path("/prop")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getPropertyValue(@ApiParam(value = "The name of the property to lookup", required = true)
                                     @QueryParam("propertyName") String propertyName) {
        
        
        String value = configApi.getPropertyValue(propertyName);
        if (value != null) {
            return Response.ok("The value of " + propertyName + " is: " + value).build();
        }
        else {
            return Response.status(Status.NOT_FOUND).entity("No value for " + propertyName + ".").build();
        }

    }

    @ApiOperation(value = "Unprotected Resource",
                  notes = "Example of an unprotected resource, this resource is accessible without a valid token.")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "A constant string is returned") })
    @GET
    @Path("/unprotected")
    @Produces(MediaType.TEXT_PLAIN)
    @OAuthSecurity(enabled = false)
    public String unprotected() {
        return "Hello from unprotected resource!";
    }

    @ApiOperation(value = "Custom Scope Protection",
                  notes = "Example of a resource that is protected by a custom scope. To access this resource a valid token for the scope 'myCustomScope' must be acquired.")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "A constant string is returned") })
    @GET
    @Path("/protected")
    @Produces(MediaType.TEXT_PLAIN)
    @OAuthSecurity(scope = "myCustomScope")
    public String customScopeProtected() {
        return "Hello from a resource protected by a custom scope!";
    }
    
    
    
    

}

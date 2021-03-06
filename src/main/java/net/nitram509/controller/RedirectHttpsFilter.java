package net.nitram509.controller;

import org.glassfish.jersey.server.ContainerRequest;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.net.URI;

@Provider
public class RedirectHttpsFilter implements ContainerRequestFilter {

  public static final String X_FORWARDED_PROTO = "X-Forwarded-Proto";

  @Context
  HttpServletResponse response;

  @Override
  public void filter(ContainerRequestContext ctx) throws IOException {
    String x_forwarded_proto_header = ctx.getHeaderString(X_FORWARDED_PROTO);
    if (x_forwarded_proto_header != null && !x_forwarded_proto_header.startsWith("https")) {
      ContainerRequest request = (ContainerRequest) ctx.getRequest();
      String path = (request.getPath(false) != null) ? request.getPath(false) : "";
      URI baseUri = request.getBaseUri();
      String port = baseUri.getPort() > 0 ? (":" + baseUri.getPort()) : "";
      response.sendRedirect("https://" + baseUri.getHost() + port + "/" + path);
    }
  }

}
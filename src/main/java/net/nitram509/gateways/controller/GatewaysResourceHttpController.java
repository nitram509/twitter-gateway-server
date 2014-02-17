/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.nitram509.gateways.controller;

import net.nitram509.controller.SessionVisitor;
import net.nitram509.gateways.api.Gateway;
import net.nitram509.gateways.api.UserId;
import net.nitram509.gateways.repository.TweetGateway;
import net.nitram509.gateways.repository.TweetGatewayRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.net.URISyntaxException;

import static javax.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED;

@Path("/gateways")
public class GatewaysResourceHttpController {

  TweetGatewayRepository repository = TweetGateway.getRepository();
  IdGenerator idGenerator = new IdGenerator();

  @POST
  @Consumes({APPLICATION_FORM_URLENCODED})
  public Response postGateways(@FormParam("suffix") String suffix,
                               @Context HttpServletRequest request) throws URISyntaxException {

    final HttpSession session = request.getSession(false);
    if (session != null) {
      final SessionVisitor sessionVisitor = new SessionVisitor(session);
      if (sessionVisitor.isAuthenticatedUser()) {
        final UserId currentUser = sessionVisitor.loadCurrentUser();
        createNewGateway(currentUser, suffix);
      }
    }

    return Response.seeOther(new URI("/index.html")).build();
  }

  private void createNewGateway(UserId currentUser, String suffix) {
    Gateway gateway = new Gateway(idGenerator.nextId());
    gateway.setSuffix(suffix);
    gateway.setOwner(currentUser);
    repository.save(gateway);
  }
}
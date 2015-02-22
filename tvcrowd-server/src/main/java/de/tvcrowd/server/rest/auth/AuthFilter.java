package de.tvcrowd.server.rest.auth;

import de.tvcrowd.server.Main;
import de.tvcrowd.server.entity.TVCrowdUser;
import de.tvcrowd.server.entity.manager.BaseManager;
import java.io.IOException;
import java.security.Principal;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import static javax.ws.rs.core.SecurityContext.BASIC_AUTH;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author Marcel Carlé <mc@marcel-carle.de>
 */
@Provider
@PreMatching
public class AuthFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        final String username = requestContext.getHeaderString("username");
        final String password = requestContext.getHeaderString("password");

        if (username != null && password != null) {
            // user has provided user AND password data

            TVCrowdUser user = BaseManager.get().find(TVCrowdUser.class, username);
            if (user == null || !password.equals(user.getPassword())) {
                // password is incorrect or user does not exist
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            } else {
                requestContext.setSecurityContext(new SecurityContext() {

                    @Override
                    public Principal getUserPrincipal() {
                        return () -> username;
                    }

                    @Override
                    public boolean isUserInRole(String role) {
                        return Main.USER_ROLE.equals(role);
                    }

                    @Override
                    public boolean isSecure() {
                        return true;
                    }

                    @Override
                    public String getAuthenticationScheme() {
                        return BASIC_AUTH;
                    }
                });
            }
        } else {
            // anoym user
            requestContext.setSecurityContext(new SecurityContext() {

                @Override
                public Principal getUserPrincipal() {
                    return () -> "ANONYM";
                }

                @Override
                public boolean isUserInRole(String role) {
                    return false;
                }

                @Override
                public boolean isSecure() {
                    return true;
                }

                @Override
                public String getAuthenticationScheme() {
                    return BASIC_AUTH;
                }
            });
        }
    }

}

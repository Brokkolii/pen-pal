package com.girnghuber.penpal.auth;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.Provider;

import java.security.Principal;

@Provider
@Priority(Priorities.AUTHENTICATION) // Ensures this filter runs early
public class AppleTokenFilter implements ContainerRequestFilter {

    @Inject
    AppleTokenService appleTokenService;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        // Extract the Authorization header
        String authorizationHeader = requestContext.getHeaderString("Authorization");

        // If there's no Authorization header or it's not Bearer token, abort the request
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            return;
        }

        // Extract the token from the header
        String token = authorizationHeader.substring("Bearer".length()).trim();

        try {
            DecodedJWT decodedJWT = appleTokenService.validateAppleToken(token);
            String userId = decodedJWT.getSubject();

            // Set custom SecurityContext with user information
            requestContext.setSecurityContext(new SecurityContext() {
                @Override
                public Principal getUserPrincipal() {
                    return () -> userId; // User ID as Principal
                }

                @Override
                public boolean isUserInRole(String role) {
                    return false;
                }

                @Override
                public boolean isSecure() {
                    return requestContext.getUriInfo().getRequestUri().getScheme().equals("https");
                }

                @Override
                public String getAuthenticationScheme() {
                    return "Bearer";
                }
            });


        } catch (Exception e) {
            // If token is invalid or anything fails, abort the request
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }

}

package com.jacdemanec.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Optional;

import static com.jacdemanec.auth.AppTokenProvider.addAuthentication;
import static com.jacdemanec.auth.AppTokenProvider.getUserFromToken;

@Component
public class RestFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(RestFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        Optional<String> userFromToken = getUserFromToken(request);
        if (!userFromToken.isPresent()) {
            response.sendError(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        logger.info("AUTHORIZED REST FILTER");

        addAuthentication(response, userFromToken.get());
        filterChain.doFilter(request, servletResponse);
    }

    @Override
    public void destroy() {
    }

}

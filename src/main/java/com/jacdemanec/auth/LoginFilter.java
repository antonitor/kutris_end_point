package com.jacdemanec.auth;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Enumeration;
import java.util.Random;
import java.util.stream.Collectors;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.jacdemanec.model.AppPlayer;
import com.jacdemanec.repository.PlayerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoginFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(LoginFilter.class);
    private static final JsonFactory jsonFactory = new JacksonFactory();
    private final GoogleAuthCodeExange googleAuthCodeExange;
    private final PlayerRepository repository;

    @Autowired
    public LoginFilter(GoogleAuthCodeExange googleAuthCodeExange, PlayerRepository playerRepository) {
        this.googleAuthCodeExange = googleAuthCodeExange;
        this.repository = playerRepository;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        logHeaders(request);

        //String authCode = ((HttpServletRequest) servletRequest).getHeader("Authorization");
        String authCode = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        logger.info("LOGIN FILTER -> AUTH CODE : " + authCode);
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        if (!authCode.isEmpty()) {

            final String userID;
            try {
                userID = googleAuthCodeExange.exchange(authCode);
                if (userID != null ){
                    if (repository.findByGpgsId(userID)!=null) {
                        AppPlayer appPlayer = repository.findByGpgsId(userID);
                        logger.info("FOUND USER IN DB WITH GPGS ID " + userID + " and alias " + appPlayer.getAliasString());
                        AppTokenProvider.addAuthentication(response, jsonFactory.toString(appPlayer));
                        request.setAttribute("userID", userID);
                    } else {
                        String anonymous = "anonymous" + new Random().nextInt(9999);
                        AppPlayer appPlayer = new AppPlayer(anonymous, userID,0,0,0,1);
                        logger.info("GENERATED A NEW USER IN DB WITH GPGS ID " + userID + " and alias " + anonymous);
                        repository.save(appPlayer);
                        AppTokenProvider.addAuthentication(response, userID);
                        request.setAttribute("userID", userID);
                        logger.info("RESPONSE HEADER Authorization:" + response.getHeader("Authorization"));
                    }
                    filterChain.doFilter(servletRequest, response);
                    return;
                }
            } catch (GeneralSecurityException | InvalidAuthCodeException e) {
                // Not valid authCode, we will send HTTP 401 back
                logger.info("LOGIN FILTER -> " + e.getMessage());
            }
        }
        logger.info("LOGIN FILTER ->  AuthCode NOT FOUND");
        ((HttpServletResponse) servletResponse).sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Override
    public void destroy() {
    }

    private void logHeaders(HttpServletRequest req) {
        Enumeration<String> headerNames = req.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            Enumeration<String> headers = req.getHeaders(headerName);
            while (headers.hasMoreElements()) {
                String headerValue = headers.nextElement();
                logger.info("HEADER -> " + headerName + ":" + headerValue);

            }

        }
    }
}
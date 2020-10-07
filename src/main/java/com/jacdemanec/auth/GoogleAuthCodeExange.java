package com.jacdemanec.auth;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.http.HttpExecuteInterceptor;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

import com.google.api.services.games.Games;
import com.google.api.services.games.model.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.GeneralSecurityException;



@Component
public class GoogleAuthCodeExange {

    private static final Logger logger = LoggerFactory.getLogger(GoogleAuthCodeExange.class);

    private static final HttpTransport transport = new NetHttpTransport();
    private static final JsonFactory jsonFactory = new JacksonFactory();
    private static final String CLIENT_ID = "";
    private static final String CLIENT_SECRET = "";

    public String exchange(String authCode) throws GeneralSecurityException, IOException, InvalidAuthCodeException {
        return GoogleAuthCodeExange.exchangeAuthCode(authCode);
    }

    private static String exchangeAuthCode(String authCode) throws GeneralSecurityException, IOException, InvalidAuthCodeException {

        logger.info("Exchanging authCode: " + authCode + " for token");
        GoogleTokenResponse tokenResponse =
                new GoogleAuthorizationCodeTokenRequest(
                        transport,
                        jsonFactory,
                        "https://oauth2.googleapis.com/token",
                        CLIENT_ID,
                        CLIENT_SECRET,
                        authCode,
                        "")
                        .execute();

        String accessToken = tokenResponse.getAccessToken();
        logger.info("ACCESS TOKEN : " + accessToken);

        GoogleIdToken idToken = tokenResponse.parseIdToken();
        logger.info("GOOGLE ID TOKEN " + idToken.toString());

        GoogleIdToken.Payload payload = idToken.getPayload();
        logger.info("GOOGLE PAYLOAD " + payload.getSubject());

        String userId = payload.getSubject();  // Use this value as a key to identify a user.
        logger.info("USER ID (SUBJECT): " + userId);

        return userId;
    }
}
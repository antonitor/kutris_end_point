package com.jacdemanec.auth;


import com.google.api.client.googleapis.auth.oauth2.*;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


import java.io.IOException;


@Component
public class GoogleAuthCodeExchange {

    private static final Logger logger = LoggerFactory.getLogger(GoogleAuthCodeExchange.class);

    private static final HttpTransport transport = new NetHttpTransport();
    private static final JsonFactory jsonFactory = new JacksonFactory();
    private static final String CLIENT_ID = "";
    private static final String CLIENT_SECRET = "";

    public String exchange(String authCode) throws IOException {
        return GoogleAuthCodeExchange.exchangeAuthCode(authCode);
    }

    private static String exchangeAuthCode(String authCode) throws IOException {

        //logger.info("Exchanging authCode: " + authCode + " for token");
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

        GoogleIdToken idToken = tokenResponse.parseIdToken();

        GoogleIdToken.Payload payload = idToken.getPayload();

        String userId = payload.getSubject();  // Use this value as a key to identify a user.

        return userId;
    }
}
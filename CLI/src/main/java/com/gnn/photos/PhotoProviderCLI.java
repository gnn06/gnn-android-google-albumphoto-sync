package com.gnn.photos;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.Credentials;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.UserCredentials;
import com.google.common.collect.ImmutableList;
import com.google.photos.library.v1.PhotosLibraryClient;
import com.google.photos.library.v1.PhotosLibrarySettings;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.List;

import gnn.com.photos.service.PhotoProvider;
import gnn.com.photos.service.RemoteException;

public class PhotoProviderCLI extends PhotoProvider {

    private static final List<String> REQUIRED_SCOPES =
            ImmutableList.of(
                    "https://www.googleapis.com/auth/photoslibrary.readonly",
                    "https://www.googleapis.com/auth/photoslibrary.appendonly");

    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final int LOCAL_RECEIVER_PORT = 61984;

    private static final String credentialFileName = "code_secret_client_183374548973-2rjac7sodb13qrro1t86l63m1joi869m.apps.googleusercontent.com.json";

    private final File credentialsFolder;

    public PhotoProviderCLI(File credentialFolder) {
        this.credentialsFolder = credentialFolder;
    }

    @Override
    protected PhotosLibraryClient getClient() throws RemoteException {
        try {
            PhotosLibrarySettings settings =
                    PhotosLibrarySettings.newBuilder()
                            .setCredentialsProvider(
                                    FixedCredentialsProvider.create(
                                            getUserCredentials()))
                            .build();
            return PhotosLibraryClient.initialize(settings);
        } catch (IOException | GeneralSecurityException exception) {
            throw new RemoteException(exception);
        }
    }

    private Credentials getUserCredentials()
            throws IOException, GeneralSecurityException {
        InputStream credentialStream = getClass().getClassLoader().getResourceAsStream(credentialFileName);
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(
                        JSON_FACTORY, new InputStreamReader(credentialStream));
        String clientId = clientSecrets.getDetails().getClientId();
        String clientSecret = clientSecrets.getDetails().getClientSecret();

        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        GoogleNetHttpTransport.newTrustedTransport(),
                        JSON_FACTORY,
                        clientSecrets,
                        REQUIRED_SCOPES)
                        .setDataStoreFactory(new FileDataStoreFactory(credentialsFolder))
                        .setAccessType("offline")
                        .build();
        LocalServerReceiver receiver =
                new LocalServerReceiver.Builder().setPort(LOCAL_RECEIVER_PORT).build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
        System.out.println("accessToken " + credential.getAccessToken());
        System.out.println("refreshToken " + credential.getRefreshToken());
        return UserCredentials.newBuilder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setRefreshToken(credential.getRefreshToken())
                .setAccessToken(new AccessToken(credential.getAccessToken(), null))
                .build();
    }
}

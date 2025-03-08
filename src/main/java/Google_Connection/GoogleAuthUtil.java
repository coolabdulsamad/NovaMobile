/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Google_Connection;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import java.util.Collections;

public class GoogleAuthUtil {
    private static final String CLIENT_ID = "259728391678-3nv3pqbbu68k4965g67ts58uhs4l0p72.apps.googleusercontent.com";
    private static final String REDIRECT_URI = "http://localhost:8080/oauth2callback";
    private static final java.util.List<String> SCOPE = Collections.singletonList("email profile");

    public static String getLoginUrl() {
        return new GoogleAuthorizationCodeRequestUrl(CLIENT_ID, REDIRECT_URI, SCOPE).build();
    }
}



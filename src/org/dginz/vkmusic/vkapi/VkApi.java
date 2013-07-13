package org.dginz.vkmusic.vkapi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Dmitriy Ginzburg
 */
public class VkApi {

    public static final String VK_API_PREFIX = "https://api.vk.com/method/";
    protected int applicationId, userId;
    protected String accessToken;

    public VkApi(int applicationId, String formData) {
        this.applicationId = applicationId;
        Pattern accessTokenPattern = Pattern.compile("access_token\\=([^\\&]*)\\&");
        Matcher accessTokenMatcher = accessTokenPattern.matcher(formData);
        if (!accessTokenMatcher.find()) {
            throw new RuntimeException("Unexpected form data. Something goes wrong?!");
        }
        accessToken = accessTokenMatcher.group(1);
        userId = Integer.parseInt(formData.substring(formData.indexOf("user_id=") + "user_id=".length()));
    }

    public VkApi(VkApi api) {
        this.applicationId = api.applicationId;
        this.userId = api.userId;
        this.accessToken = new String(api.accessToken);
    }

    @Override
    public String toString() {
        return "applicationId: " + applicationId + "; userId: " + userId + "; accessToken: " + accessToken;
    }

    public String generateQuery(String methodName, String parameters) {
        return VK_API_PREFIX + methodName + "?" + parameters + "&access_token=" + accessToken;
    }

    private static String getPage(String page) throws IOException {
        URL url = new URL(page);
        URLConnection urlConnection = url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(
                urlConnection.getInputStream(), "UTF-8"));
        String inputLine;
        StringBuilder result = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            result.append(inputLine);
        }
        in.close();

        return result.toString();
    }

    public String submitQuery(String methodName, String parameters) throws IOException {
        return getPage(generateQuery(methodName, parameters));
    }
}

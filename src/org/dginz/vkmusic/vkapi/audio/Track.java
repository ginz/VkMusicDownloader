package org.dginz.vkmusic.vkapi.audio;

import org.json.simple.JSONObject;

/**
 *
 * @author Dmitry Ginzburg <dmitry.a.ginzburg@gmail.com>
 */
public class Track {

    private String artist, title, url;
    private int duration, genre;

    public Track(JSONObject jsonObject) {
        artist = jsonObject.get("artist").toString().replace("/", "&").replace("\\", "&");
        title = jsonObject.get("title").toString().replace("/", "&").replace("\\", "&");
        url = jsonObject.get("url").toString();
        duration = Integer.parseInt(jsonObject.get("duration").toString());
        try {
            genre = Integer.parseInt(jsonObject.get("genre").toString());
        } catch (NullPointerException ex) {
        }
    }

    public String getArtist() {
        return artist;
    }

    public String getArtistDirName() {
        String[] parts = artist.split(" ");
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < parts.length - 1; ++i) {
            result.append(toProperCase(parts[i] + " "));
        }
        result.append(toProperCase(parts[parts.length - 1]));
        return result.toString();
    }

    private static String toProperCase(String s) {
        return s.substring(0, 1).toUpperCase()
                + s.substring(1).toLowerCase();
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return artist + " - " + title + " (" + duration + " seconds)";
    }
}

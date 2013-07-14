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
        artist = jsonObject.get("artist").toString().replace("/", ",").replace("\\", ",");
        title = jsonObject.get("title").toString().replace("/", ",").replace("\\", ",");
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

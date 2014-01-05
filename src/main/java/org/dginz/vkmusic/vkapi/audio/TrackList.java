package org.dginz.vkmusic.vkapi.audio;

import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Dmitry Ginzburg <dmitry.a.ginzburg@gmail.com>
 */
public class TrackList extends ArrayList <Track>{

    //List<Track> trackList = null;

    public TrackList(JSONArray jsonArray) {
        for (Object trackObject : jsonArray) {
            this.add(new Track((JSONObject)trackObject));
        }
    }

    public List<Track> getTrackList() {
        return this;
    }
}
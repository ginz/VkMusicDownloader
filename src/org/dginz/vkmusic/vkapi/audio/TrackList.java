package org.dginz.vkmusic.vkapi.audio;

import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Dmitry Ginzburg <dmitry.a.ginzburg@gmail.com>
 */
public class TrackList {

    List<Track> trackList = null;

    public TrackList(JSONArray jsonArray) {
        trackList = new ArrayList<>();
        for (Object trackObject : jsonArray) {
            trackList.add(new Track((JSONObject)trackObject));
        }
    }

    public List<Track> getTrackList() {
        return trackList;
    }
}
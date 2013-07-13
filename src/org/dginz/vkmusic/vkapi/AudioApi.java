package org.dginz.vkmusic.vkapi;

import java.io.IOException;

/**
 *
 * @author Dmitriy Ginzburg
 */
public class AudioApi extends VkApi {

    public AudioApi(int applicationId, String formData) {
        super(applicationId, formData);
    }
    
    public AudioApi (VkApi api) {
        super (api);
    }

    public String getAllMyAudioJson() throws IOException {
        return submitQuery("audio.get", "");
    }
}

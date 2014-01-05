package org.dginz.vkmusic;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.control.ProgressBar;
import org.dginz.vkmusic.vkapi.audio.Track;
import org.dginz.vkmusic.vkapi.audio.TrackList;

/**
 *
 * @author Dmitry Ginzburg <dmitry.a.ginzburg@gmail.com>
 */
public class Downloader implements Runnable {

    List<Track> trackList = null;
    ProgressBar current = null;
    ProgressBar global = null;
    File dir = null;
    boolean artistDir = false;
    private boolean running = false;

    public Downloader(TrackList trackList, ProgressBar current, ProgressBar global, File dir, boolean artistDir) {
        this.trackList = trackList;
        this.current = current;
        this.global = global;
        this.dir = dir;
        this.artistDir = artistDir;
    }

    public Downloader(TrackList trackList, ProgressBar current, ProgressBar global) {
        this(trackList, current, global, new File("."), false);
    }

    public void setDir(File dir) {
        this.dir = dir;
    }

    public void setArtistDir(boolean artistDir) {
        this.artistDir = artistDir;
    }

    public void stop() {
        running = false;
    }

    public boolean isRunning() {
        return running;
    }

    @Override
    public void run() {
        running = true;
        try {
            System.err.println("Running downloader with artistDir=" + artistDir + ", dir=" + dir.getCanonicalPath());
        } catch (IOException ex) {
            Logger.getLogger(Downloader.class.getName()).log(Level.SEVERE, null, ex);
        }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                current.setProgress(0);
                global.setProgress(0);
            }
        });
        int n = trackList.size();
        for (int i = 0; i < n && running; ++i) {
            Track track = trackList.get(i);
            try {
                URLConnection conn = new URL(track.getUrl()).openConnection();
                InputStream is = conn.getInputStream();
                File path = null;
                if (artistDir) {
                    File artistDirectory = new File(dir.getAbsolutePath() + File.separator + track.getArtist());
                    if (artistDirectory.exists()) {
                        if (!artistDirectory.isDirectory()) {
                            System.err.println("Can't create artist directory, file exists and it's not direcotory: " + artistDirectory.getCanonicalPath());
                        }
                    } else {
                        if (!artistDirectory.mkdir()) {
                            System.err.println("Can't create artist directory: " + artistDirectory.getCanonicalPath());
                        }
                    }
                    path = new File(artistDirectory.getAbsolutePath() + File.separator + track.getTitle() + ".mp3");
                } else {
                    path = new File(dir.getAbsolutePath() + File.separator + track.getArtist() + " - " + track.getTitle() + ".mp3");
                }
                OutputStream os = new FileOutputStream(path);
                int mp3Size = fileSize(conn);
                byte[] buffer = new byte[4096];
                int len, sumlen = 0;
                while ((len = is.read(buffer)) > 0) {
                    os.write(buffer, 0, len);
                    sumlen += len;
                    final int sumlenF = sumlen, mp3SizeF = mp3Size;
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            current.setProgress((double) sumlenF / mp3SizeF);
                        }
                    });
                }
                os.close();
                final int iF = i, nF = n;
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        global.setProgress((double) (iF + 1) / nF);
                    }
                });
            } catch (MalformedURLException ex) {
                Logger.getLogger(Downloader.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Downloader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        running = false;
    }

    private int fileSize(URLConnection conn) {
        List<String> values = conn.getHeaderFields().get("Content-Length");
        if (values != null && !values.isEmpty()) {
            String sLength = values.get(0);
            if (sLength != null) {
                return Integer.parseInt(sLength);
            }
        }
        return Integer.MAX_VALUE;
    }
}

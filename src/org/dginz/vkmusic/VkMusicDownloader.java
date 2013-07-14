package org.dginz.vkmusic;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker.State;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.dginz.vkmusic.vkapi.audio.AudioApi;
import org.dginz.vkmusic.vkapi.audio.Track;
import org.dginz.vkmusic.vkapi.audio.TrackList;

/**
 *
 * @author Dmitry Ginzburg <dmitry.a.ginzburg@gmail.com>
 */
public class VkMusicDownloader extends Application {
    
    public static final int VK_APP_ID = 3763243, VK_APP_MASK = 8;
    public static final String REDIRECT_URL = "https://oauth.vk.com/blank.html";
    public static final String VK_AUTH_URL = "https://oauth.vk.com/authorize?client_id=" + VK_APP_ID + "&display=page&response_type=token&scope=" + VK_APP_MASK + "&redirect_url=" + REDIRECT_URL;
    public static final String APPLICATION_TITLE = "Vk Music Downloader";
    public static final String LOGIN_SUCCESS_PAGE = "blank.html#", LOGIN_FAILURE_PAGE = "blank.html#error";
    private volatile boolean loginSuccess = false, loginFailure = false;
    private String formData = null;
    
    private void changeState(String Url) {
        if (Url.contains(LOGIN_FAILURE_PAGE)) {
            loginFailure = true;
        } else if (Url.contains(LOGIN_SUCCESS_PAGE)) {
            loginSuccess = true;
            try {
                formData = URLDecoder.decode(Url.substring(Url.indexOf(LOGIN_SUCCESS_PAGE) + LOGIN_SUCCESS_PAGE.length()), "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(VkMusicDownloader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    @Override
    public void start(final Stage primaryStage) {
        primaryStage.setTitle(APPLICATION_TITLE);
        final WebView view = new WebView();
        final WebEngine engine = view.getEngine();
        engine.load(VK_AUTH_URL);
        engine.getLoadWorker().stateProperty().addListener(
                new ChangeListener<State>() {
            @Override
            public void changed(ObservableValue ov, State oldState, State newState) {
                if (newState == State.SUCCEEDED) {
                    changeState(engine.getLocation());
                }
            }
        });
        primaryStage.setScene(new Scene(view));
        primaryStage.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!loginSuccess && !loginFailure && primaryStage.isShowing());
                if (loginFailure || (!primaryStage.isShowing())) {
                    Platform.exit();
                } else {
                    try {
                        AudioApi api = new AudioApi(VK_APP_ID, formData);
                        final TrackList tracks = api.getTrackList();
                        final ListView<Track> list = new ListView<>();
                        ObservableList<Track> items = FXCollections.observableArrayList(tracks.getTrackList());
                        list.setItems(items);
                        Label destLabel = new Label("Choose destination directory: ");
                        final TextField dest = new TextField();
                        Button browse = new Button("Browse...");
                        browse.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent t) {
                                DirectoryChooser fc = new DirectoryChooser();
                                
                                File dir = fc.showDialog(primaryStage);
                                if (dir != null) {
                                    dest.setText(dir.toString());
                                }
                            }
                        });
                        HBox browseLayout = new HBox(5);
                        browseLayout.getChildren().addAll(destLabel, dest, browse);
                        final VBox mainLayout = new VBox(8);
                        Button downloadAll = new Button("Download all");
                        mainLayout.getChildren().addAll(browseLayout, downloadAll, list);
                        final ProgressBar current = new ProgressBar();
                        final ProgressBar global = new ProgressBar();
                        System.out.println(dest.getText());
                        final Downloader downloader = new Downloader(tracks, current, global, new File(dest.getText()));
                        final Thread downloaderThread = new Thread(downloader);
                        downloadAll.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent t) {
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        mainLayout.getChildren().add(2, current);
                                        mainLayout.getChildren().add(3, global);
                                    }
                                });
                                downloader.setDir(new File(dest.getText()));
                                downloaderThread.start();
                            }
                        });
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                primaryStage.setScene(new Scene(mainLayout));
                            }
                        });
                    } catch (IOException ex) {
                        Logger.getLogger(VkMusicDownloader.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }).start();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}

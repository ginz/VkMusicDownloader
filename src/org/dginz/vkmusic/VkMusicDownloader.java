/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dginz.vkmusic;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

/**
 *
 * @author dginzburg
 */
public class VkMusicDownloader extends Application {

    public static final int VK_APP_ID = 3763243;
    public static final String VK_AUTH_PAGE = "http://vk.com/login.php?app=" + VK_APP_ID + "&layout=popup&type=browser";
    public static final String APPLICATION_TITLE = "Vk Music Downloader";
    public static final String LOGIN_SUCCESS_PAGE = "login_success.html#session=", LOGIN_FAILURE_PAGE = "login_failure.html";
    private boolean loginSuccess = false, loginFailure = false;
    private String session = null;
    
    private void changeState(String Url) {
        if (Url.contains(LOGIN_SUCCESS_PAGE)) {
            loginSuccess = true;
            try {
                session = URLDecoder.decode(Url.substring(Url.indexOf(LOGIN_SUCCESS_PAGE) + LOGIN_SUCCESS_PAGE.length()), "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(VkMusicDownloader.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println (session);
        } else if (Url.contains(LOGIN_FAILURE_PAGE)) {
            loginFailure = true;
        }
    }

    @Override
    public void start(final Stage primaryStage) {
        primaryStage.setTitle(APPLICATION_TITLE);
        final WebView view = new WebView();
        final WebEngine engine = view.getEngine();
        engine.load(VK_AUTH_PAGE);
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
        /*Button btn = new Button();
         btn.setText("Say 'Hello World'");
         btn.setOnAction(new EventHandler<ActionEvent>() {
         @Override
         public void handle(ActionEvent event) {
         System.out.println("Hello World!");
         }
         });
        
         StackPane root = new StackPane();
         root.getChildren().add(btn);
        
         Scene scene = new Scene(root, 300, 250);
        
         primaryStage.setTitle("Hello World!");
         primaryStage.setScene(scene);
         primaryStage.show();*/
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

package player;


import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

public class SendMovieInfo implements Runnable {
    int movieId, currentSecond;

    SendMovieInfo(int movieId, int currentSecond){
        this.movieId = movieId;
        this.currentSecond = currentSecond;
    }

    @Override
    public void run() {
        String uri="http://127.0.0.1:8080/tvcrowd/player/update/"+movieId+"/"+currentSecond;
        HttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(uri);
        httpPost.setHeader("username", "user1");
        httpPost.setHeader("password", "123456");
        httpPost.setHeader("Content-Type", "application/json");
        try {
            client.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

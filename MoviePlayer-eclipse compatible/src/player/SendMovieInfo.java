package player;


import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

public class SendMovieInfo extends Thread {

    public void doInBackground(int movieId, int currentSecond) throws IOException {
        String uri="http://127.0.0.1:8080/tvcrowd/player/update/"+movieId+"/"+currentSecond;
        HttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(uri);
        httpPost.setHeader("username", "user1");
        httpPost.setHeader("password", "123456");
        httpPost.setHeader("Content-Type", "application/json");
        client.execute(httpPost);
    }


}

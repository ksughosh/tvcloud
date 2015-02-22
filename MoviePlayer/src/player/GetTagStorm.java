package player;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import de.tvcrowd.lib.dto.TagDto;
import de.tvcrowd.lib.dto.TagStormDto;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

/**
 * Created by sughoshkumar on 14/07/14.
 */
public class GetTagStorm extends Thread {
    int movieId;
    int period;

    GetTagStorm(int movieId, int period) {
        this.movieId = movieId;
        this.period = period;
    }


    public List<TagStormDto> doInBackground() throws IOException {
        HttpResponse response = null;
        HttpClient client = HttpClients.createDefault();
        String uri = "http://127.0.0.1:8080/tvcrowd/player/list/tagstorms/" + movieId + "/" + period;
        InputStream localInputStream = null;
        HttpGet httpGet = new HttpGet(uri);
        httpGet.setHeader("username", "user1");
        httpGet.setHeader("password", "123456");
        httpGet.setHeader("Content-Type", "application/json");

        response = client.execute(httpGet);
        if (response.getStatusLine().getStatusCode() != 200) {
            System.out.println("error Server returned nothing status: " + response.getStatusLine().getStatusCode());
            return Collections.EMPTY_LIST;
        }
        localInputStream = response.getEntity().getContent();
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(localInputStream));
        String input;
        while ((input = bufferedReader.readLine()) != null) {
            stringBuilder.append(input);
        }
        bufferedReader.close();
        Gson gson = new Gson();
        Type listType = new TypeToken<List<TagStormDto>>(){}.getType();

        List<TagStormDto> tagStormDtos = gson.fromJson(stringBuilder.toString(), listType);

        return tagStormDtos;
    }
}

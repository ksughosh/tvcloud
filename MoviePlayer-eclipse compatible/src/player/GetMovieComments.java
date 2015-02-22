package player;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import de.tvcrowd.lib.dto.TagDto;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by sughoshkumar on 13/07/14.
 */
//public class GetMovieComments extends Thread {
//    private TotalNumberOfCommentsWithTimeStamp doInBackground() throws IOException {
//        String uri = " "; // assign the uri for the link!
//        HttpClient httpClient = HttpClients.createDefault();
//        HttpResponse response = null;
//        InputStream localInputStream = null;
//        HttpGet httpGet = new HttpGet(uri);
//        httpGet.setHeader("username","user1");
//        httpGet.setHeader("password","123456");
//        httpGet.setHeader("Content-Type","application/json");
//        response = httpClient.execute(httpGet);
//        if (response.getStatusLine().getStatusCode() != 200) {
//            System.out.println("errorCode" + response.getStatusLine().getStatusCode());
//        }
//        StringBuilder stringBuilder = new StringBuilder();
//        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(localInputStream));
//        try {
//            String input;
//            while ((input = bufferedReader.readLine()) != null) {
//                stringBuilder.append(input);
//            }
//            bufferedReader.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        Gson gson = new Gson();
//        Type listType = new TypeToken<List<TotalNumberOfCommentsWithTimeStamp>>() {
//        }.getType();
//        List<TotalNumberOfCommentsWithTimeStamp> totalNumberOfCommentsWithTimeStamps = gson.fromJson(stringBuilder.toString(), listType);
//        if (!totalNumberOfCommentsWithTimeStamps.isEmpty()) {
//            totalNumberOfCommentsWithTimeStamps = gson.fromJson(stringBuilder.toString(), listType);
//        }
//        return totalNumberOfCommentsWithTimeStamps;  // Get the object with the number of comments and the timestamp.
//    }
//}

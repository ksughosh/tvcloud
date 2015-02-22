package com.example.sughoshkumar.disfinal;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

import de.tvcrowd.lib.dto.WatchingDto;

public class GetMovieInfo extends AsyncTask <Void, Void, WatchingDto> {

    @Override
    protected WatchingDto doInBackground(Void... voids) {
        HttpClient httpClient = new DefaultHttpClient();
        String uri="http://10.0.2.2:8080/tvcrowd/app/get/watching";
        InputStream localInputStream = null;
        HttpResponse response = null;
        HttpGet httpGet = new HttpGet(uri);
        httpGet.setHeader("username", "user1");
        httpGet.setHeader("password", "123456");
        httpGet.setHeader("Content-Type", "application/json");
        try {
            response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() != 200) {
                System.out.println("errorCode" + response.getStatusLine().getStatusCode());
            }
            localInputStream = response.getEntity().getContent();
            // read what is the response

        } catch (IOException e) {
            e.printStackTrace();
        }

        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(localInputStream));
        try {
            String input;
            while ((input = bufferedReader.readLine()) != null) {
                stringBuilder.append(input);
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        Type listType = new TypeToken<WatchingDto>() {
        }.getType();

        WatchingDto watchingDto = gson.fromJson(stringBuilder.toString(), listType);
        if (watchingDto != null) {
            watchingDto = gson.fromJson(stringBuilder.toString(), listType);
        }
        return watchingDto;
    }
}

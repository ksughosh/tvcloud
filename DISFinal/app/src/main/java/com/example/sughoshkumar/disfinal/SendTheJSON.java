package com.example.sughoshkumar.disfinal;

import android.os.AsyncTask;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import de.tvcrowd.lib.dto.CreateTagDto;
import de.tvcrowd.lib.dto.TagDto;

public class SendTheJSON extends AsyncTask<CreateTagDto, Void, TagDto> {

    private Gson gson = new Gson();

    @Override
    protected TagDto doInBackground(CreateTagDto... createTagDtos) {
        if (createTagDtos.length == 1) {
            CreateTagDto createTagDto = createTagDtos[0];
            HttpClient httpClient = new DefaultHttpClient();
            String uri = "http://10.0.2.2:8080/tvcrowd/app/create/tag";
            HttpPost httpPost = new HttpPost(uri);
            httpPost.setHeader("username", "user1");
            httpPost.setHeader("password", "123456");
            httpPost.setHeader("Content-Type", "application/json");

            try {
                httpPost.setEntity(new StringEntity(gson.toJson(createTagDto)));
                httpClient.execute(httpPost);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}

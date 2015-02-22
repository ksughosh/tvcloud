package com.example.sughoshkumar.disfinal;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import de.tvcrowd.lib.dto.*;

import static android.widget.AdapterView.OnItemClickListener;


public class MainView extends Activity {
    Button tagHere;
    Button tagLater;
    ListView listView;
    TextView textView;
    static boolean didLoad;
    public int flag = 0;
    public ArrayAdapter<String> adapter;
    public ArrayList<String> comments = new ArrayList<String>();
    ArrayList<Integer> secondsTaggedLater = new ArrayList<Integer>();

    private void showNewTagArea(boolean visible) {
        LinearLayout editText = (LinearLayout) findViewById(R.id.newTagArea);

        editText.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public static void sendNewTagTimed(int movieId, int seconds, String text) {
        CreateTagDto createTagDto = new CreateTagDto();

        createTagDto.setMovieId(movieId);
        createTagDto.setSeconds(seconds);
        createTagDto.setComment(text);

        new SendTheJSON().execute(createTagDto);
    }

    private void sendNewTag(String text) {
        WatchingDto watchingDto;

        int currentSecond = 0;
        int movieId = 0;

        try {
            watchingDto = new GetMovieInfo().execute().get();

            movieId = watchingDto.getMovieId();
            currentSecond = watchingDto.getSecond();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        sendNewTagTimed(movieId, currentSecond, text);
    }

    private void setupNewTagArea() {
        final EditText edit = (EditText) findViewById(R.id.newTagField);

        edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    sendNewTag(v.getText().toString());

                    v.setText("");
                    edit.clearFocus();

                    final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(edit.getWindowToken(), 0);

                    showNewTagArea(false);

                    return true;
                }

                return false;
            }
        });
    }
    void callMethod() {
        Intent intent = new Intent(this, FinishTags.class);

        intent.putIntegerArrayListExtra("secondsTaggedLater", secondsTaggedLater);
        startActivity(intent);
    }
    private void toggleMovieDone(boolean movieDone) {
        int visibility = movieDone ? View.GONE : View.VISIBLE;

        findViewById(R.id.tagLaterButton).setVisibility(visibility);
        findViewById(R.id.tagButton).setVisibility(visibility);
        showNewTagArea(false);
        findViewById(R.id.listView).setVisibility(visibility);

        findViewById(R.id.movieDoneText).setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view);
        tagHere = (Button) findViewById(R.id.tagButton);
        tagLater = (Button) findViewById(R.id.tagLaterButton);
        listView = (ListView) findViewById(R.id.listView);
        textView = (TextView) findViewById(R.id.textView);

        setupNewTagArea();

        final Handler tagRefreshHandler = new Handler();
        final int delay = 1000; //milliseconds
        adapter = new ArrayAdapter<String>(MainView.this, R.layout.small_font_text, comments);
        tagRefreshHandler.postDelayed(new Runnable() {
            int prevSecond = -1;
            int count = 0;
            boolean loaded = false;
            public void run() {
                List<TagDto> result = null;

                try {
                    WatchingDto watchingDto = new GetMovieInfo().execute().get();
                    if(watchingDto.getSecond() == prevSecond ){
                        count++;
                    }

                    result = new GetTheJSON().execute().get();

                    comments.clear();

                    for (TagDto t : result) {
                        comments.add(lookUpUsers(t.getUsername()) + ": " + t.getComment());
                    }
                    prevSecond = watchingDto.getSecond();
                    adapter.notifyDataSetChanged();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                if ( count > 3 && !(didLoad)) {
                    didLoad = true;
                    toggleMovieDone(true);
                } else {
                    tagRefreshHandler.postDelayed(this, delay);
                }
                }
        }, 0);


        tagHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNewTagArea(true);

                View textField = findViewById(R.id.newTagField);

                textField.requestFocus();
                final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                inputMethodManager.showSoftInput(textField, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        tagLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentSeconds = 0;

                try {
                    currentSeconds = new GetMovieInfo().execute().get().getSecond();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                secondsTaggedLater.add(currentSeconds);

                Toast.makeText(MainView.this, "Tagged for later.", Toast.LENGTH_SHORT).show();

                //toggleMovieDone(true);
            }
        });

        findViewById(R.id.movieDoneText).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainView.this, FinishTags.class);

                intent.putIntegerArrayListExtra("secondsTaggedLater", secondsTaggedLater);
                startActivity(intent);
            }
        });

        //get the number of available comments within the 10 seconds interval of video
        //and increment the numberOfComments variable.
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int i, long l) {
                listView.setFocusable(true);
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainView.this);
                builder.setMessage("Like Comment?").setPositiveButton("Like", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // increment the likeCounter and update it in the database!
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_view, menu);
        getMenuInflater().inflate(R.menu.finish_tags, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_finish_tags) {
            Intent intent = new Intent(this, FinishTags.class);

            intent.putIntegerArrayListExtra("secondsTaggedLater", secondsTaggedLater);
            startActivity(intent);
            
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public String lookUpUsers(String name){
        if(name.equalsIgnoreCase("user0"))
            return "Felix";
        else if ( name.equalsIgnoreCase("user1"))
            return "Marcel";
        else if ( name.equalsIgnoreCase("user2"))
            return "Sughosh";
        else if (name.equalsIgnoreCase("user3"))
            return "Pavel";
        else if ( name.equalsIgnoreCase("user4"))
            return "Felix";
        else if ( name.equalsIgnoreCase("user5"))
            return "Sughosh";
        else if ( name.equalsIgnoreCase("user6"))
            return "Marcel";
        else if ( name.equalsIgnoreCase("user7"))
            return "Pavel";
        else if ( name.equalsIgnoreCase("user8"))
            return "Sughosh";
        else if ( name.equalsIgnoreCase("user9"))
            return "Felix";
        else
            return "Bharath";
    }
}




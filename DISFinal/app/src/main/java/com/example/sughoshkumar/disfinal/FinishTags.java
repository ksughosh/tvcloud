package com.example.sughoshkumar.disfinal;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.sughoshkumar.disfinal.R;

import java.util.ArrayList;
import java.util.List;

public class FinishTags extends Activity {
    public ListView listView;
    public VideoView videoView;
    public MyAdapter myAdapter;

    private static class UnfinishedTag {
        int seconds;
        public String text = "";

        UnfinishedTag(int seconds) {
            this.seconds = seconds;
            this.text = "Tag at: " + seconds;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_tags);
        ArrayAdapter<UnfinishedTag> adapter;
        listView = (ListView) findViewById(R.id.listView);
        videoView = (VideoView) findViewById(R.id.videoView);
        ArrayList<UnfinishedTag> unfinishedTags = new ArrayList<UnfinishedTag>();

        List<Integer> secondsTagged = getIntent().getIntegerArrayListExtra("secondsTaggedLater");

        for(Integer seconds : secondsTagged) {
            unfinishedTags.add(new UnfinishedTag(seconds));
        }

        if (unfinishedTags.size() == 0){
            Toast.makeText(FinishTags.this, "No Tags For Later!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(FinishTags.this, MainView.class);
            intent.putExtra("reload",true);
            startActivity(intent);
        }

        myAdapter = new MyAdapter(unfinishedTags);
        listView.setItemsCanFocus(true);
        listView.setAdapter(myAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                UnfinishedTag clickedTag = (UnfinishedTag) adapterView.getItemAtPosition(i);

                playVideo(clickedTag.seconds);
            }

        });

    }

    private static boolean isBetween(int x, int lower, int upper) {
        return lower <= x && x <= upper;
    }

    public void playVideo( int currentSeconds ){
        if(isBetween(currentSeconds,61,71)){
            videoView.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.lightning_1_01_to_1_11);
            videoView.start();
        }
        else if (isBetween(currentSeconds, 133,143)){
            videoView.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.outfit_2_13_to_2_23);
            videoView.start();
        }
        else if (isBetween(currentSeconds, 143, 153)){
            videoView.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.speeding_2_23_to_2_33);
            videoView.start();
        }
        else if(isBetween(currentSeconds,105, 115)){
            videoView.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.slowmotion_1_45_to_1_55);
            videoView.start();
        }
        else{
            videoView.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.tornado_4_40_to_4_50);
            videoView.start();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    public void onClick(View v, int currentSecond){
        new Thread(new Runnable() {
            @Override
            public void run() {
                VideoView videoView = (VideoView) findViewById(R.id.videoView);

                videoView.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.tornado_4_40_to_4_50);
            }
        });
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
        return super.onOptionsItemSelected(item);
    }

    public class MyAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        public ArrayList<UnfinishedTag> myItems;

        public MyAdapter(ArrayList<UnfinishedTag> tags) {
            mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            myItems = tags;
            notifyDataSetChanged();
        }

        public int getCount() {
            return myItems.size();
        }

        public Object getItem(int position) {
            return myItems.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.item, null);
                holder.caption = (EditText) convertView.findViewById(R.id.ItemCaption);
                holder.discardButton = (Button) convertView.findViewById(R.id.ItemDiscardButton);
                holder.caption.setTag(myItems.get(position));
                //Fill EditText with the value you have in data source
                holder.caption.setHint(myItems.get(position).text);
                //holder.caption.setText(myItems.get(position).text);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.caption.setId(position);

            //we need to update adapter once we finish with editing
            holder.caption.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasFocus) {
                    final EditText Caption = (EditText) v;
                    UnfinishedTag tag = (UnfinishedTag) Caption.getTag();

                    if (!hasFocus){
                        //final int position = v.getId();
                        tag.text = Caption.getText().toString();
                    } else {
                        playVideo(tag.seconds);
                    }
                }
            });

            holder.caption.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEND) {
                        UnfinishedTag tag = myItems.get(v.getId());

                        MainView.sendNewTagTimed(2, tag.seconds, v.getText().toString());

                        myItems.remove(v.getId());

                        notifyDataSetChanged();

                        return true;
                    }

                    return false;
                }
            });

            holder.discardButton.setId(position);

            holder.discardButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myItems.remove(view.getId());

                    notifyDataSetChanged();
                }
            });

            return convertView;
        }
    }

    class ViewHolder {
        EditText caption;
        Button discardButton;
    }
}

package com.yathannsh;

/**
 * Created by yathannsh on 1/20/2016.
 */
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.loklak.android.data.MessageEntry;
import org.loklak.android.data.Timeline;
import org.loklak.android.harvester.TwitterScraper;
import org.loklak.android.tools.LogLines;
import org.loklak.android.wok.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class Main2Activity extends AppCompatActivity {
    public static Context context;
    Button button;
    EditText mEdit;
    static ProgressDialog progress;
    static ListView listView;
    static ArrayList<String> listItems;
    static ArrayAdapter<String> adapter;
    static List<Tweet> tweets = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        context = this.getApplicationContext();
        mEdit = (EditText) findViewById(R.id.editText);
        button = (Button) findViewById(R.id.button);
        listView = (ListView) findViewById(R.id.listView);
        listItems = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItems);
        listView.setAdapter(adapter);

        addListenerOnButton();
    }

    public static boolean isConnectedWifi() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm == null ? null : cm.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected() && networkInfo.getType() == ConnectivityManager.TYPE_WIFI);
    }

    public void addListenerOnButton() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                listItems.clear();
                adapter.notifyDataSetChanged();
                if (isConnectedWifi()) {
                    String search_string = mEdit.getText().toString();
                    if (search_string == null || search_string.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please enter some text to search.", Toast.LENGTH_LONG).show();
                    } else {
                        progress = ProgressDialog.show(Main2Activity.this, "Loading", "Finding results for " + search_string, true);
                        new LoadThread().execute(search_string, null, null);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please connect to a wifi!", Toast.LENGTH_LONG).show();
                }
            }

        });

    }

    private static class LoadThread extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            Log.i("harvest", "To be searched " + params[0]);
            Timeline tl = TwitterScraper.search(params[0], Timeline.Order.CREATED_AT);
            Log.i("harvest", "fetched timeline: " + tl.size());
            tweets = new LinkedList<>();
            for (MessageEntry me : tl) {
                tweets.add(new Tweet(me.getScreenName(), tl.getUser(me).getProfileImageUrl(), me.getText(0, ""), me.getCreatedAt()));
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }

        @Override
        protected void onPostExecute(Void result) {
            setData();
            progress.dismiss();
        }
    }

    public static void setData() {
        try {
            if (tweets.isEmpty()) {
                listItems.add("No records found!");
                adapter.notifyDataSetChanged();
            }
            for (Tweet me : tweets) {
                listItems.add(Html.fromHtml(me.getCreated_at() + " from @" + me.getUsername() + "<br/>" + me.getMessage()).toString());
                adapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }


}

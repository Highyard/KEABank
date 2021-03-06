package com.example.kea_bank.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.kea_bank.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class NewsFeedActivity extends AppCompatActivity {

    ListView newsFeedList;

    ArrayList<String> titles;
    ArrayList<String> links;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);
        newsFeedList = findViewById(R.id.newsFeedList);

        titles = new ArrayList<>();
        links = new ArrayList<>();

        newsFeedList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Uri uri = Uri.parse(links.get(position));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);

            }
        });

        new ProcessInBackground().execute();

    }

    public InputStream getInputStream(URL url) {

        try {
            //openConnection() returns instance that represents a connection to the remote object referred to by the URL
            //getInputStream() returns a stream that reads from the open connection
            return url.openConnection().getInputStream();
        }

        catch (IOException e) {
            return null;
        }
    }

    private class ProcessInBackground extends AsyncTask<Integer, Void, Exception> {

        ProgressDialog progressDialog = new ProgressDialog(NewsFeedActivity.this);

        Exception exception = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.setMessage(getResources().getString(R.string.loading_rss));
            progressDialog.show();
        }

        @Override
        protected Exception doInBackground(Integer... params) {

            try {
                URL url = new URL("http://feeds.reuters.com/reuters/businessNews");

                //creates new instance of PullParserFactory that can be used to create XML pull parsers
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();

                //Specifies whether the parser produced by this factory will provide support
                //for XML namespaces
                factory.setNamespaceAware(false);

                //creates a new instance of a XML pull parser using the currently configured
                //factory features
                XmlPullParser xpp = factory.newPullParser();

                // We will get the XML from an input stream
                xpp.setInput(getInputStream(url), "UTF_8");

                // We will use a boolean to determine whether we are inside the correct tag or not. In this case
                // the tag is <item>
                boolean insideItem = false;

                // Returns the type of current event: START_TAG, END_TAG, START_DOCUMENT, END_DOCUMENT etc..
                int eventType = xpp.getEventType(); //loop control variable

                while (eventType != XmlPullParser.END_DOCUMENT) {

                    //if we are at a START_TAG (opening tag)
                    if (eventType == XmlPullParser.START_TAG) {

                        //if the tag is called "item"
                        if (xpp.getName().equalsIgnoreCase("item")) {

                            insideItem = true;
                        }
                        //if the tag is called "title"
                        else if (xpp.getName().equalsIgnoreCase("title")) {

                            if (insideItem) {
                                // extract the text between <title> and </title>
                                titles.add(xpp.nextText());
                            }
                        }
                        //if the tag is called "link"
                        else if (xpp.getName().equalsIgnoreCase("link")) {

                            if (insideItem) {
                                // extract the text between <link> and </link>
                                links.add(xpp.nextText());
                            }
                        }
                    }
                    //if we are at an END_TAG and the END_TAG is called "item"
                    else if (eventType == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item")) {

                        insideItem = false;
                    }
                    //move to next element
                    eventType = xpp.next();
                }


            }
            catch (MalformedURLException e) {

                exception = e;
            }

            catch (XmlPullParserException e) {
                exception = e;
            }

            catch (IOException e) {
                exception = e;
            }

            return exception;
        }

        @Override
        protected void onPostExecute(Exception s) {
            super.onPostExecute(s);

            ArrayAdapter<String> adapter = new ArrayAdapter<>(NewsFeedActivity.this, android.R.layout.simple_list_item_1, titles);

            newsFeedList.setAdapter(adapter);


            progressDialog.dismiss();
        }
    }
}

package com.example.tonio.tp_android;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.tonio.tp_android.R;
import com.example.tonio.tp_android.deezer.GsonRequest;
import com.example.tonio.tp_android.deezer.GsonSearchAlbum;
import com.example.tonio.tp_android.deezer.GsonSearchSong;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class SongPage extends AppCompatActivity {

    HashMap song;
    private GsonSearchSong list_song_response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        String id_album = extras.getString("id_album");

        final ListView lv= (ListView)findViewById(R.id.list_song);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "https://api.deezer.com/album/"+id_album+"/tracks";

        //Toast.makeText(getApplicationContext(), id_album, Toast.LENGTH_LONG).show();

        GsonRequest request = new GsonRequest(url, GsonSearchSong.class, null,
                new Response.Listener<GsonSearchSong>() {
                    @Override
                    public void onResponse(GsonSearchSong response) {
                        Toast.makeText(getApplicationContext(), response.getData().get(0).toString(), Toast.LENGTH_LONG).show();
                        list_song_response = response;
                        final ArrayList<HashMap<String,String>> list_song = new ArrayList<HashMap<String, String>>();

                        for(int i=0 ; i<response.getData().size() ; i++) { // response is the artist list
                            song = new HashMap<String,String>();
                            song.put("id_song",response.getData().get(i).getId().toString());
                            song.put("name_song",response.getData().get(i).getTitle());
                            song.put("preview_song",response.getData().get(i).getPreview());
                            list_song.add(song);
                        }
                        SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(),
                                list_song, R.layout.song_card, new String[]{"name_song"}, new int[]{R.id.title}){
                            public View getView(int position, View convertView, ViewGroup parent){
                                View view = super.getView(position,convertView,parent);
                                ImageView image = (ImageView)view.findViewById(R.id.image);
                                return view;
                            }
                        };
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            public void onItemClick(AdapterView<?> parent, View view,
                                                    int position, long id) {

                                HashMap<String,String> song = (HashMap<String,String>)lv.getItemAtPosition(position);
                                final String preview = song.get("preview_song");
                                
                                MediaPlayer mp = new MediaPlayer();
                                try {
                                    mp.setDataSource(preview);
                                    mp.prepare();
                                    mp.start();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                        lv.setAdapter(adapter);
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        queue.add(request);
    }

}

package com.example.tonio.tp_android;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.tonio.tp_android.deezer.Album;
import com.example.tonio.tp_android.deezer.Artist;
import com.example.tonio.tp_android.deezer.GsonRequest;
import com.example.tonio.tp_android.deezer.GsonSearchAlbum;
import com.example.tonio.tp_android.deezer.GsonSearchArtist;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class AlbumPage extends AppCompatActivity {

    HashMap album;
    private GsonSearchAlbum list_album_response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        String id_artist = extras.getString("id_artist");



        final ListView lv= (ListView)findViewById(R.id.list_album);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url ="https://api.deezer.com/artist/"+id_artist+"/albums";

        GsonRequest request = new GsonRequest(url, GsonSearchAlbum.class, null,
                new Response.Listener<GsonSearchAlbum>() {
                    @Override
                    public void onResponse(GsonSearchAlbum response) {
                        list_album_response = response;
                        final ArrayList<HashMap<String,String>> list_album = new ArrayList<HashMap<String, String>>();

                        for(int i=0 ; i<response.getData().size() ; i++) { // response is the artist list
                            album = new HashMap<String,String>();
                            album.put("id_album",response.getData().get(i).getId().toString());
                            album.put("name_album",response.getData().get(i).getTitle());
                            list_album.add(album);
                        }

                        SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(),
                                list_album, R.layout.cell_cards, new String[]{"name_album"}, new int[]{R.id.title}){
                            public View getView(int position, View convertView, ViewGroup parent){
                                View view = super.getView(position,convertView,parent);
                                ImageView image = (ImageView)view.findViewById(R.id.image);
                                //TODO image url
                                Picasso.with(getApplicationContext()).load(list_album_response.getData().get(position).getCoverMedium()).into(image);
                                return view;
                            }
                        };
                        lv.setAdapter(adapter);
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            public void onItemClick(AdapterView<?> parent, View view,
                                                    int position, long id) {

                                HashMap<String,String> album = (HashMap<String,String>)lv.getItemAtPosition(position);
                                final String idAlbum = album.get("id_album");

                                Intent intent = new Intent(AlbumPage.this, SongPage.class);
                                intent.putExtra("id_album",idAlbum);
                                startActivityForResult(intent,0);
                            }
                        });
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

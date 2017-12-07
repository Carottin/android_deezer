package com.example.tonio.tp_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.tonio.tp_android.deezer.Artist;
import com.example.tonio.tp_android.deezer.GsonRequest;
import com.example.tonio.tp_android.deezer.GsonSearchArtist;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    HashMap artist;
    private GsonSearchArtist list_author;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);;


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        final ListView lv= (ListView)findViewById(R.id.list_artist);
        SearchView search = (SearchView) menu.findItem(R.id.search1).getActionView();

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url ="https://api.deezer.com/search/artist?q="+query;


                GsonRequest request = new GsonRequest(url, GsonSearchArtist.class, null,
                        new Response.Listener<GsonSearchArtist>() {
                            @Override
                            public void onResponse(GsonSearchArtist response) {
                                list_author = response;
                                final ArrayList<HashMap<String,String>> list_artist = new ArrayList<HashMap<String, String>>();
                                for(int i=0 ; i<response.getData().size() ; i++) { // response is the artist list
                                    artist = new HashMap<String,String>();
                                    artist.put("id_artist",response.getData().get(i).getId());
                                    artist.put("name_artist",response.getData().get(i).getName());
                                    list_artist.add(artist);
                                }

                                // fill in the grid_item layout
                                SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(),
                                        list_artist, R.layout.cell_cards, new String[]{"name_artist"}, new int[]{R.id.title}){
                                    public View getView(int position, View convertView,ViewGroup parent){
                                        View view = super.getView(position,convertView,parent);
                                        ImageView image = (ImageView)view.findViewById(R.id.image);
                                        //TODO image url
                                        Picasso.with(getApplicationContext()).load(list_author.getData().get(position).getPictureMedium()).into(image);
                                        return view;
                                    }
                                };
                                lv.setAdapter(adapter);
                                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    public void onItemClick(AdapterView<?> parent, View view,
                                                            int position, long id) {

                                        HashMap<String,String> artist = (HashMap<String,String>)lv.getItemAtPosition(position);
                                        final String idArtist = artist.get("id_artist");
                                        final String NAME = artist.get("name_artist");

                                        Intent intent = new Intent(MainActivity.this, AlbumPage.class);
                                        intent.putExtra("id_artist",idArtist);
                                        startActivityForResult(intent,0);
                                    }
                                });
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //mTextView.setText("That didn't work!");
                            }
                        }
                );
                queue.add(request);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

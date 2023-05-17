package com.zybooks.moviedatabaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentContainerView;

import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;


public class MainActivity extends AppCompatActivity {

    Button btnPositive;
    Button btnNegative;
    FragmentContainerView fragmentContainerView;
    ImageView imgPoster;
    SharedPreferences sharedpreferences;
    TextView txtTitle;
    TextView txtInfo;

    //private Toolbar toolbar;
    public static final String MyPREFERENCES = "MyPrefs";
    public static String movieTitle;
    public static String movieInfo;
    public static String moviePoster;

    public static JSONObject movieObject = new JSONObject();
    public static JSONObject responseObject = new JSONObject();
    public static JSONObject responseNestedObject = new JSONObject();
    public static JSONArray responseArray = new JSONArray();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        if (savedInstanceState==null)
//        {
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new fragment_item()).commit();
//        }

        try {
            writeToInternalFile();
            //String fileContents = readFromInternalFile();
            //Log.d(TAG, "File contents = " + fileContents);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }

        fragmentContainerView = (FragmentContainerView) findViewById(R.id.fragmentContainerView);
        btnPositive = (Button) findViewById(R.id.btnPositive);
        btnNegative = (Button) findViewById(R.id.btnNegative);
        imgPoster = (ImageView) findViewById(R.id.imgPoster);
        Glide.with(this).load("https://image.tmdb.org/t/p/original/wwemzKWzjKYJFfCeiB57q3r4Bcm.png").into(imgPoster);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtInfo = (TextView) findViewById(R.id.txtInfo);
        //toolbar=findViewById(R.id.myToolBar);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
//        fragmentContainerView.onVis

        try {
            createMovieObject();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        resetPref();
        updateTxt();
        updateVisibility();
        //updatePoster();



        imgPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(MainActivity.this, "Positive", Toast.LENGTH_LONG).show();


                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean("movie_poster_state",false);
                editor.putBoolean("movie_info_state",true);
                editor.commit();
                updateVisibility();
                updateBtnTxt();

            }
        });
        btnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(MainActivity.this, "Positive", Toast.LENGTH_LONG).show();
                //apiRequestBy();
                FileOutputStream outputStream = null;
                try {
                    outputStream = openFileOutput("watchlist", Context.MODE_PRIVATE);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                PrintWriter writer = new PrintWriter(outputStream);

                try {
                    writer.println(movieObject.getInt("id")+";"+movieObject.getString("title"));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                writer.close();


            }
        });
        btnNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(MainActivity.this, "Negative", Toast.LENGTH_LONG).show();
                //testStringToJson();
                getCollection(responseObject);
            }
        });
        txtInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(MainActivity.this, "Positive", Toast.LENGTH_LONG).show();

                // TODO this click listener needs to show a trailer
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean("movie_poster_state",false);
                editor.putBoolean("movie_info_state",false);
                editor.putBoolean("movie_trailer_state",true);
                editor.commit();
                updateVisibility();
                updateBtnTxt();

            }
        });
    }//!onCreate()

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        MenuItem.OnActionExpandListener onActionExpandListener=new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(@NonNull MenuItem menuItem) {
                Toast.makeText(MainActivity.this, "Search is Expanded",Toast.LENGTH_SHORT).show();
                return true;
            }


            @Override
            public boolean onMenuItemActionCollapse(@NonNull MenuItem menuItem) {
                Toast.makeText(MainActivity.this, "Search is Collapse",Toast.LENGTH_SHORT).show();
                return true;
            }
        };
        menu.findItem(R.id.menuSearch).setOnActionExpandListener(onActionExpandListener);
        SearchView searchView=(SearchView) menu.findItem(R.id.menuSearch).getActionView();
        searchView.setQueryHint("Title: ");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(MainActivity.this, "Press Poster Image for Movie Description",Toast.LENGTH_LONG).show();
                apiSearch(query);
                resetPref();
                updateVisibility();
                updateBtnTxt();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Toast.makeText(MainActivity.this, "Text Change",Toast.LENGTH_SHORT).show();;
                return false;
            }
        });

        return true;

        }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.menuHome:
                Toast.makeText(this,"Home",Toast.LENGTH_LONG).show();
                //updateState("movie_poster_state",true);
                //closeFragment();
                resetPref();
                updateVisibility();
                updateBtnTxt();
                break;
            case R.id.menuList:
                Toast.makeText(this,"Watchlist",Toast.LENGTH_LONG).show();
                updateState("parent_list_view_state",true);
                updateVisibility();
                updateBtnTxt();
                break;



        }
        return super.onOptionsItemSelected(item);
    }


    protected void testFirstUse() {
        if (sharedpreferences.getBoolean("first_time_use", false)) {
            txtTitle.setText("!!Welcome!!");
            txtInfo.setText("Insert First Use Directions here ");

        }
    }


    protected  void createMovieObject() throws JSONException {

        movieObject.put("id",0);
        movieObject.put("title","No Title");
        movieObject.put("poster","No Poster Path");
        movieObject.put("info","No Info");



    }

    protected void updateMovieObject(@NonNull JSONObject o) throws JSONException{

        movieObject.put("id",o.getInt("id"));
        movieObject.put("title",o.getString("original_title"));
        movieObject.put("poster",o.getString("poster_path"));
        movieObject.put("info",o.getString("overview"));

        txtTitle.setText(movieObject.getString("title"));
        Glide.with(this).load("https://image.tmdb.org/t/p/w500/"+movieObject.getString("poster")).into(imgPoster);
        txtInfo.setText(movieObject.getString("info"));

    }

    protected void updateMovieObjectSearch(@NonNull JSONObject o) throws JSONException{
        JSONArray results = o.getJSONArray("results");
        JSONObject firstResult = results.getJSONObject(0);
        movieObject.put("title",firstResult.getString("original_title"));
        movieObject.put("poster",firstResult.getString("poster_path"));
        movieObject.put("info",firstResult.getString("overview"));

        txtTitle.setText(movieObject.getString("title"));
        Glide.with(this).load("https://image.tmdb.org/t/p/w500/"+movieObject.getString("poster")).into(imgPoster);
        txtInfo.setText(movieObject.getString("info"));

    }
    protected void resetPref() {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.putInt("selected_list_id", -1);
        editor.putInt("selected_movie_id", -1);
        editor.putBoolean("first_time_use", false);
        editor.putBoolean("new_list_state", false);
        editor.putBoolean("delete_list_state", false);
        editor.putBoolean("parent_list_view_state", false);
        editor.putBoolean("child_list_view_state", false);
        editor.putBoolean("movie_poster_state", true);
        editor.putBoolean("movie_info_state", false);
        editor.putBoolean("movie_trailer_state", false);
        editor.putString("movie_object", "-1");
        editor.putString("api_key","8a0b124ba93ae2f067965f8895e7c0d2");
        editor.putString("api_response","NA");
        editor.commit();
    }

    protected void updateState(String s, boolean b){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean("first_time_use", false);
        editor.putBoolean("new_list_state", false);
        editor.putBoolean("delete_list_state", false);
        editor.putBoolean("parent_list_view_state", false);
        editor.putBoolean("child_list_view_state", false);
        editor.putBoolean("movie_poster_state", true);
        editor.putBoolean("movie_info_state", false);
        editor.putBoolean("movie_trailer_state", false);

        editor.putBoolean(s,b);
        editor.commit();
    }
    protected void setAPIResponseSP(String r){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("api_response",r);
        editor.commit();

    }
    protected void stringToJSON(String r){
        String myJSON = r;
        try
        {
            responseObject = new JSONObject(myJSON);
            System.out.println("JSON Object: "+responseObject);
        }
        catch (JSONException e)
        {
            System.out.println("Error "+e.toString());
        }
    }
    protected void updateBtnTxt() {
    }
    protected void updatePoster(){
        try {
            Glide.with(this).load("https://image.tmdb.org/t/p/w500/"+movieObject.getString("poster")).into(imgPoster);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
    protected void updateTxt() {
        if (sharedpreferences.getBoolean("first_time_use", false)) {
            btnPositive.setText("NA");
            btnNegative.setText("NA");
            txtTitle.setText("Welcome!");
            //txtInfo.setText("Insert Directions");
        } else if (sharedpreferences.getBoolean("new_list_state", false)) {
            //hide imgPoster
            //hide txtInfo
            //show fragList
            btnPositive.setText("Add");
            btnNegative.setText("Cancel");
            txtTitle.setText("Lists of Lists");
        } else if (sharedpreferences.getBoolean("delete_list_state", false)) {
            //hide imgPoster
            //hide txtInfo
            //show fragList
            btnPositive.setText("Yes");
            btnNegative.setText("No");
            txtTitle.setText("Target List Title");
        } else if (sharedpreferences.getBoolean("parent_list_state", false)) {
            //hide imgPoster
            //hide txtInfo
            //show fragList
            btnPositive.setText("View");
            btnNegative.setText("Remove");
            txtTitle.setText("Lists of Lists");
        } else if (sharedpreferences.getBoolean("child_list_state", false)) {
            //hide imgPoster
            //hide txtInfo
            //show fragList
            btnPositive.setText("View");
            btnNegative.setText("Remove");
            txtTitle.setText("Target List Title");
        } else if (sharedpreferences.getBoolean("movie_poster_state", false)) {
            //hide txtInfo
            //hide fragList
            //show imgPoster
            btnPositive.setText("Add");
            btnNegative.setText("Remove");
            txtTitle.setText("Welcome!");
            txtInfo.setText("Insert Directions");
        } else if (sharedpreferences.getBoolean("movie_info_state", false)) {
            //hide imgPoster
            //hide fragList
            //show txtInfo
            btnPositive.setText("Add");
            btnNegative.setText("Remove");
            //txtTitle.setText("Movie Title");
            //txtInfo.setText("Get Movie Info from API");
        } else if (sharedpreferences.getBoolean("movie_trailer_state", false)) {
            //hide imgPoster
            //hide fragList
            //show txtInfo
            //sho movieTrailer
            btnPositive.setText("Add");
            btnNegative.setText("Remove");
            txtTitle.setText("Movie Title");

        }
    }

    protected void updateVisibility(){
        if (sharedpreferences.getBoolean("first_time_use", false)) {
            //hide imgPoster
            //hide fragList
            //show txtInfo
            //imgPoster.setVisibility(View.GONE);
        }else if (sharedpreferences.getBoolean("delete_list_state", false)) {
            //hide imgPoster
            //hide txtInfo
            //show fragList
            fragmentContainerView.setVisibility(View.VISIBLE);
            imgPoster.setVisibility(View.GONE);
            txtInfo.setVisibility(View.GONE);

        } else if (sharedpreferences.getBoolean("parent_list_view_state", false)) {
            //hide imgPoster
            //hide txtInfo
            //show fragList
            fragmentContainerView.setVisibility(View.VISIBLE);
            imgPoster.setVisibility(View.GONE);
            txtInfo.setVisibility(View.GONE);

        } else if (sharedpreferences.getBoolean("movie_poster_state", false)) {
            //hide txtInfo
            //hide fragList
            //show imgPoster
            fragmentContainerView.setVisibility(View.GONE);
            imgPoster.setVisibility(View.VISIBLE);
            txtInfo.setVisibility(View.GONE);


        } else if (sharedpreferences.getBoolean("movie_info_state", false)) {
            //hide imgPoster
            //hide fragList
            //show txtInfo
            fragmentContainerView.setVisibility(View.GONE);
            imgPoster.setVisibility(View.GONE);
            txtInfo.setVisibility(View.VISIBLE);
            //txtInfo.setText("Get Movie Description from API.");

        }


    }
    protected void testObjectToArray(){

    }
    protected void getCollection(JSONObject o){

        try{
            responseNestedObject = responseObject.getJSONObject("belongs_to_collection");
            txtInfo.setText(responseNestedObject.getString("name"));
        }
        catch (JSONException e)
        {
            System.out.println("Error "+e.toString());
        }

    }
    protected void testStringToJson(){
        txtTitle.setText("StringToJSON");
        try{
            txtTitle.setText(responseObject.getString("name"));
        }
        catch (JSONException e)
        {
            System.out.println("Error "+e.toString());
        }

    }
    protected void apiSearch(String q){
        q.replace(' ','+' );
        Toast.makeText(MainActivity.this, "API Search: "+q.replace(' ','+'),Toast.LENGTH_SHORT).show();
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.themoviedb.org/3/search/movie?api_key=8a0b124ba93ae2f067965f8895e7c0d2&query="+q;


        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        txtInfo.setText("Response is: " + response);
                        setAPIResponseSP(response);
                        stringToJSON(response);
                        //txtInfo.setText(sharedpreferences.getString("api_response", "No Data"));
                        try {
                            updateMovieObjectSearch(responseObject);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                txtInfo.setText("That didn't work!");
            }

        });

        // Add the request to the RequestQueue.
        // queue.add(stringRequest);
        MySingleton.getInstance(MainActivity.this).addToRequestQueue(stringRequest);
    }
    protected void apiRequestByID(int i){

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.themoviedb.org/3/movie/"+i+"?api_key=8a0b124ba93ae2f067965f8895e7c0d2&language=en-US";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        txtInfo.setText("Response is: " + response);
                        setAPIResponseSP(response);
                        stringToJSON(response);
                        //txtInfo.setText(sharedpreferences.getString("api_response", "No Data"));
                        try {
                            updateMovieObject(responseObject);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                txtInfo.setText("That didn't work!");
            }

        });

        // Add the request to the RequestQueue.
       // queue.add(stringRequest);
        MySingleton.getInstance(MainActivity.this).addToRequestQueue(stringRequest);
    }

    private void writeToInternalFile() throws IOException {
        FileOutputStream outputStream = openFileOutput("watchlist", Context.MODE_PRIVATE);
        PrintWriter writer = new PrintWriter(outputStream);
        writer.println("75780;Jack Reacher");
        writer.println("11544;Lilo & Stitch");
        writer.println("20760;Lilo & Stitch 2: Stitch Has a Glitch");
        writer.println("21316;Leroy & Stitch");
        writer.close();
    }

    private int getMovieID(String s){

        int midPoint = s.indexOf(';');
        int id = 0;
        id = Integer.parseInt(s.substring(0,midPoint));
        return id;
    }

    void updateMovie(String s){
        apiRequestByID(getMovieID(s));
        try {
            txtTitle.setText(movieObject.getString("title"));
            Glide.with(this).load("https://image.tmdb.org/t/p/w500/"+movieObject.getString("poster")).into(imgPoster);
            txtInfo.setText(movieObject.getString("info"));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


    }

    public void closeFragment() {
        resetPref();
        updateVisibility();
        updateBtnTxt();

    }

}//!class Main Activity
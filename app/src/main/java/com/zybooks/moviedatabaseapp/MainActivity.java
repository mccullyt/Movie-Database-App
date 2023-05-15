package com.zybooks.moviedatabaseapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    Button btnPositive;
    Button btnNegative;

    ImageView imgPoster;
    TextView txtTitle;
    TextView txtInfo;

    public static final String MyPREFERENCES = "MyPrefs";
    public static final String SharedSum = "Sum";
    public static final String sharedInt = "test_int";
    public static String sharedBool = "test_bool";
    public static String myJSON;

    SharedPreferences sharedpreferences;

    int sum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPositive = (Button) findViewById(R.id.btnPositive);
        btnNegative = (Button) findViewById(R.id.btnNegative);
        imgPoster = (ImageView) findViewById(R.id.imgPoster);
        Glide.with(this).load("https://image.tmdb.org/t/p/original/wwemzKWzjKYJFfCeiB57q3r4Bcm.png").into(imgPoster);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtInfo = (TextView) findViewById(R.id.txtInfo);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        //txtTitle.setText( sharedpreferences.getString(SharedSum,"No Data"));
        //txtInfo.setText( String.valueOf(sharedpreferences.getBoolean("test_bool",true)));
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

                Toast.makeText(MainActivity.this, "Positive", Toast.LENGTH_LONG).show();


                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.commit();
                //txtTitle.setText(sharedpreferences.getString(SharedSum,"No Data"));
                //txtTitle.setText(sharedpreferences.getString(SharedSum, "No Data"));
                //testSimpleRequest();
                testAdvRequest();


            }
        });
        btnNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(MainActivity.this, "Negative", Toast.LENGTH_LONG).show();
                sum = sum - 1;
                String strSum = Integer.toString(sum);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(SharedSum, strSum);
                editor.commit();
                txtTitle.setText(sharedpreferences.getString(SharedSum, "No Data"));
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

    protected void updateBtnTxt() {
    }

//    protected void testFirstUse() {
//        if (sharedpreferences.getBoolean("first_time_use", false)) {
//            txtTitle.setText("!!Welcome!!");
//            txtInfo.setText("Insert First Use Directions here ");
//
//        }
//    }

    protected void updatePoster(){
        Glide.with(this).load("https://image.tmdb.org/t/p/w500/wwemzKWzjKYJFfCeiB57q3r4Bcm.png").into(imgPoster);
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
protected void setAPIResponseSP(String r){
    SharedPreferences.Editor editor = sharedpreferences.edit();
    editor.putString("api_response",r);
    editor.commit();

}
    protected void updateTxt() {
        if (sharedpreferences.getBoolean("first_time_use", false)) {
            btnPositive.setText("NA");
            btnNegative.setText("NA");
            txtTitle.setText("Welcome!");
            txtInfo.setText("Insert Directions");
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
            txtTitle.setText("Movie Title");
            txtInfo.setText("Get Movie Info from API");
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
            imgPoster.setVisibility(View.GONE);
        } else if (sharedpreferences.getBoolean("new_list_state", false)) {
            //hide imgPoster
            //hide txtInfo
            //show fragList
            imgPoster.setVisibility(View.GONE);
        } else if (sharedpreferences.getBoolean("delete_list_state", false)) {
            //hide imgPoster
            //hide txtInfo
            //show fragList
            imgPoster.setVisibility(View.GONE);
        } else if (sharedpreferences.getBoolean("parent_list_state", false)) {
            //hide imgPoster
            //hide txtInfo
            //show fragList
            imgPoster.setVisibility(View.GONE);
        } else if (sharedpreferences.getBoolean("child_list_state", false)) {
            //hide imgPoster
            //hide txtInfo
            //show fragList
            imgPoster.setVisibility(View.GONE);
        } else if (sharedpreferences.getBoolean("movie_poster_state", false)) {
            //hide txtInfo
            //hide fragList
            //show imgPoster
            txtInfo.setVisibility(View.GONE);
            imgPoster.setVisibility(View.VISIBLE);

        } else if (sharedpreferences.getBoolean("movie_info_state", false)) {
            //hide imgPoster
            //hide fragList
            //show txtInfo
            txtInfo.setVisibility(View.VISIBLE);
            txtInfo.setText("Get Movie Description from API.");
            imgPoster.setVisibility(View.GONE);

        } else if (sharedpreferences.getBoolean("movie_trailer_state", false)) {
            //hide imgPoster
            //hide fragList
            //show txtInfo
            //sho movieTrailer
            //TODO - Update this block to show a movie trailer and hide movie description
            txtInfo.setVisibility(View.VISIBLE);
            txtInfo.setText("Get Movie Trailer From Description from API.");
            imgPoster.setVisibility(View.GONE);

        }
    }
    protected void testAdvRequest(){

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.themoviedb.org/3/movie/75780?api_key=8a0b124ba93ae2f067965f8895e7c0d2&language=en-US";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        txtInfo.setText("Response is: " + response);
                        setAPIResponseSP(response);
                        txtInfo.setText(sharedpreferences.getString("api_response", "No Data"));
                        myJSON = response;
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
    protected void testSimpleRequest(){

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.themoviedb.org/3/movie/75780?api_key=8a0b124ba93ae2f067965f8895e7c0d2&language=en-US";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        txtInfo.setText("Response is: " + response);
                        myJSON = response;
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                txtInfo.setText("That didn't work!");
            }

        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);

    }
}//!class Main Activity
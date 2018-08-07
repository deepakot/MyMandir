package com.example.deepak.mymandir;

import android.app.ProgressDialog;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.deepak.mymandir.Adapters.StoryListAdapter;
import com.example.deepak.mymandir.Constants.AppConstants;
import com.example.deepak.mymandir.Models.StoryData;
import com.example.deepak.mymandir.Presenter.MyOnclickListener;
import com.example.deepak.mymandir.Presenter.StoryPresenter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements MyOnclickListener {

    private RecyclerView recyclerView;
    private StoryListAdapter sAdapter;
    ProgressDialog pDialog;
    private static ArrayList<StoryData> postList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        sendRequest();
        sAdapter = new StoryListAdapter(postList,MainActivity.this);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(sAdapter);
    }

    private void sendRequest() throws JSONException {
        pDialog = new ProgressDialog(MainActivity.this);
        pDialog.setMessage(AppConstants.LOADING);
        pDialog.show();
        pDialog.setCancelable(false);

        JSONObject paramJson = new JSONObject();

        paramJson.put("key1", "value1");

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest( AppConstants.url,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response) {
                        // display response
                        pDialog.hide();
                        postList = StoryPresenter.getInstance().showJSON(response);
                        sAdapter.updateItems(postList);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(AppConstants.MMLog, AppConstants.ERROR + error.getMessage());
                        pDialog.hide();
                        Toast.makeText(MainActivity.this, AppConstants.NETWORK_NOT_AVAILABLE, Toast.LENGTH_LONG).show();
                    }
                }
        );
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest();

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void openDialog(StoryData storyData){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.addToBackStack(null);
        DialogFragment newFragment = DetailDialogFragment.newInstance(storyData);
        newFragment.show(ft, "dialog");
    }

}

package com.tamu.jcabelloc.visitmanager;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ScheduleActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    LatLng latLng;
    Spinner agentSpinner;
    TextView addressTextView;
    EditText visitReasonEditText;
    ArrayList<VisitTask> visitTasks;
    CustomAdapter listViewAdapter;

    public void scheduleVisit(View view) {
        //    public VisitTask(String agent, String address, LatLng location, String reason, String status) {

        final VisitTask visitTask = new VisitTask(agentSpinner.getSelectedItem().toString(), addressTextView.getText().toString(), latLng, visitReasonEditText.getText().toString(), "created");
        ParseObject parseVisitTask = new ParseObject("VisitTask");
        parseVisitTask.put("agent", visitTask.getAgent());
        parseVisitTask.put("reason", visitTask.getReason());
        parseVisitTask.put("address", visitTask.getAddress());
        parseVisitTask.put("status", visitTask.getStatus());
        ParseGeoPoint location = new ParseGeoPoint(latLng.latitude, latLng.longitude);
        parseVisitTask.put("location", location);
        parseVisitTask.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    visitTasks.add(visitTask);
                    listViewAdapter.notifyDataSetChanged();
                    Toast.makeText(ScheduleActivity.this, "Visit Scheduled OK", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ScheduleActivity.this, "Something went wrong saving the visit task", Toast.LENGTH_SHORT).show();
                }
            }
        });
        addressTextView.setText("Location -> Address");
        visitReasonEditText.setText("");

    }

    public void pickUpLocation(View view) {
        Intent intent = new Intent(this, PickUpAddressMapsActivity.class);
        //latLng = new LatLng(51.881708, -0.418173);
        if (latLng != null) {
            intent.putExtra("latitude",  latLng.latitude);
            intent.putExtra("longitude", latLng.longitude);
        }
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        addressTextView = (TextView)findViewById(R.id.addressTextView);
        visitReasonEditText = (EditText) findViewById(R.id.visitReasonEditText);

        //
        ListView visitTaskListView = findViewById(R.id.visitTaskListView);
        visitTasks = new ArrayList<>();
        listViewAdapter = new CustomAdapter(this, visitTasks);
        visitTaskListView.setAdapter(listViewAdapter);
        ParseQuery<ParseObject> visitTaskQuery = ParseQuery.getQuery("VisitTask");
        visitTaskQuery.whereEqualTo("status", "created");
        visitTaskQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    for (ParseObject parseVisitTask : objects) {
                        VisitTask visitTask = new VisitTask(parseVisitTask.getObjectId(), parseVisitTask.getString("agent"), parseVisitTask.getString("address"),
                                new LatLng(parseVisitTask.getParseGeoPoint("location").getLatitude(), parseVisitTask.getParseGeoPoint("location").getLongitude()), parseVisitTask.getString("reason"));
                        visitTasks.add(visitTask);
                    }
                    listViewAdapter.notifyDataSetChanged();
                }
            }
        });
        //ArrayList<VisitTask> visitTasks = new ArrayList<VisitTask>(Arrays.asList(new VisitTask("jose", "Jr. Pira 429", new LatLng(0,0), "First Visit")));


        //
        Intent intent = getIntent();
        if (intent.getDoubleExtra("latitude", 0) != 0 && intent.getDoubleExtra("longitude", 0) != 0) {
            latLng = new LatLng(intent.getDoubleExtra("latitude", 0),intent.getDoubleExtra("longitude", 0));
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                List<Address> listAddresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                String address = "";
                if (listAddresses != null && listAddresses.size() > 0) {
                    Log.i("Address", listAddresses.get(0).toString());
                    if (listAddresses.get(0).getSubThoroughfare()!=null){
                        address += listAddresses.get(0).getSubThoroughfare() + " "; //feature??
                    }
                    if (listAddresses.get(0).getThoroughfare() != null){
                        address += listAddresses.get(0).getThoroughfare() + " ";
                    }
                    if (listAddresses.get(0).getLocality() != null ) {
                        address += listAddresses.get(0).getLocality() + " ";
                    }
                    if (listAddresses.get(0).getPostalCode() != null ) {
                        address += listAddresses.get(0).getPostalCode() + " ";
                    }
                    if (listAddresses.get(0).getCountryName() != null ) {
                        address += listAddresses.get(0).getCountryName();
                    }
                }else {
                    address = "N/A";
                }
                addressTextView.setText(address);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        agentSpinner = (Spinner)findViewById(R.id.agentSpinner);
        final List<String> agents = new ArrayList<>();
        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, agents);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        agentSpinner.setAdapter(spinnerAdapter);
        agentSpinner.setOnItemSelectedListener(this);
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereNotEqualTo("username", currentUser.getUsername());
            query.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {
                    if (e == null) {
                        for (ParseUser user: objects) {
                            agents.add(user.getUsername());
                        }
                        spinnerAdapter.notifyDataSetChanged();

                    }else {
                        Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                    }
                }
            });

        } else {
            Toast.makeText(this, "Log In Again", Toast.LENGTH_LONG).show();
        }

    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Log.i("Selected", (String)adapterView.getItemAtPosition(i));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Log.i("nothing", "Nothing");

    }
}

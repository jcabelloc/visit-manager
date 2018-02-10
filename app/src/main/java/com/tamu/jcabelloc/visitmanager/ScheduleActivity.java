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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ScheduleActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    LatLng latLng;
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
        TextView addressTextView = (TextView)findViewById(R.id.addressTextView);

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
                Log.i("Address", address);
                addressTextView.setText(address);

                //I/Address: Address[addressLines=[0:"630 Dunstable Road",1:"Luton",2:"LU4 8SE",3:"UK"],feature=630,admin=null,sub-admin=null,locality=Luton,thoroughfare=Dunstable Road,postalCode=LU4 8SE,countryCode=GB,countryName=United Kingdom,hasLatitude=true,latitude=51.8925319,hasLongitude=true,longitude=-0.4612792,phone=null,url=null,extras=null]

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        Spinner agentSpinner = (Spinner)findViewById(R.id.agentSpinner);
        final List<String> agents = new ArrayList<>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, agents);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        agentSpinner.setAdapter(adapter);
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
                        adapter.notifyDataSetChanged();

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

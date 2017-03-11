package com.example.dennisonkangi.hatuaapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;

import android.support.v4.app.NotificationCompat;



public class BulkSMS extends AppCompatActivity implements View.OnClickListener {
    ProgressDialog progress;
    public static final String PHONE_URL = "http://ioney.coolpage.biz/bulksms.php";
    public static final String COUNT_URL = "http://ioney.coolpage.biz/count.php";
    private EditText message;
    private Button buttonCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bulk_sms);
        buttonCount = (Button) findViewById(R.id.buttoncount);


        buttonCount.setOnClickListener(this);

        FloatingActionButton faby = (FloatingActionButton) findViewById(R.id.fabsms);
        faby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               BulkSMS();

            }
        });

    }

    private void BulkSMS()
    {
        message= (EditText) findViewById(R.id.message);

        progress = ProgressDialog.show(this, "Bulk SMS",
                "Fetching phone numbers...", true);


        StringRequest stringRequest = new StringRequest(PHONE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            showPhones(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress.dismiss();
                        Toast.makeText(BulkSMS.this, "Network Error", Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


    private void showPhones(String json) throws JSONException {
        final String tosend = message.getText().toString().trim();

        progress.dismiss();
        JSONObject jsonObj = new JSONObject(json);
        SmsManager smsManager = SmsManager.getDefault();

        // Getting JSON Array node
        JSONArray contacts = jsonObj.getJSONArray("contacts");
        for (int i = 0; i < contacts.length(); i++) {
            JSONObject c = contacts.getJSONObject(i);

            String rname = c.getString("names");
            String phone = c.getString("phone");

            smsManager.sendTextMessage(phone, null, tosend, null, null);
            Toast.makeText(BulkSMS.this, "Message sent to "+rname, Toast.LENGTH_SHORT).show();
            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.mipmap.ic_mainlogo)
                            .setContentTitle("SMS Sent to "+rname)
                            .setContentText(rname+" | Tel: "+phone);

            Intent notificationIntent = new Intent(this, MainActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(contentIntent);

            // Add as notification
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(0, builder.build());

        }


        //here



    }

    private void countRec()
    {
        progress = ProgressDialog.show(this, "SMS Needed",
                "Counting Recipients...", true);


        StringRequest stringRequest = new StringRequest(COUNT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        showJSON1(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress.dismiss();
                        Toast.makeText(BulkSMS.this, "Network Error!!", Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


    private void showJSON1(String total) {

        progress.dismiss();
        Toast.makeText(BulkSMS.this, total, Toast.LENGTH_SHORT).show();
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_mainlogo)
                        .setContentTitle("Total Recipients")
                        .setContentText("You need to have "+total+" SMS bundle to meet "+total+" recipients");

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());


    }
    @Override
    public void onClick(View v) {
        if(v == buttonCount){
            countRec();
        }

    }




}

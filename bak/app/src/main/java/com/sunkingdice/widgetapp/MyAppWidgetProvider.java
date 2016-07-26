package com.sunkingdice.widgetapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class MyAppWidgetProvider extends android.appwidget.AppWidgetProvider {

    public static final String ACTION_APPWIDGET_UPDATE = "com.sunkingdice.widgetapp.widgetwala";
    public static String number = "loading";

    @Override
    public void onUpdate(final Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        for(int i=0; i<appWidgetIds.length; i++){
            int currentWidgetId = appWidgetIds[i];

            Intent intent = new Intent(context,MainActivity.class);

            PendingIntent pending = PendingIntent.getActivity(context, 0,intent, 0);
            RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.widget_layout);

            views.setOnClickPendingIntent(R.id.textView, pending);
            appWidgetManager.updateAppWidget(currentWidgetId,views);
            requestNextNumber(context);
            Log.d("TEST","updated");
        }
    }


    @Override
    public void onReceive(Context context, Intent intent) {

        final String action = intent.getAction();

        if (ACTION_APPWIDGET_UPDATE.equals(action)) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            Random rand = new Random();
            int  n = rand.nextInt(50) + 1;
            views.setTextViewText(R.id.textView, number);
            requestNextNumber(context);
            ComponentName componentName = new ComponentName(context, MyAppWidgetProvider.class);
            AppWidgetManager.getInstance(context).updateAppWidget(componentName, views);
            Log.d("TEST","receive");
        }

    }

    void requestNextNumber(Context c){
        RequestQueue queue = Volley.newRequestQueue(c);
        String url ="https://bittrex.com/api/v1.1/public/getticker?market=BTC-STEEM";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject j = new JSONObject(response);
                            JSONObject k = j.getJSONObject("result");
                            number = k.getString("Last");
                            number = number.substring(0,9)+ " BTC";
                            Log.d("TEST","Reponse " + number);

                        } catch (JSONException e) {
                            Log.d("TEST",e.toString());
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);
    }


}

package com.example.praveen_investate.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import androidx.appcompat.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.praveen_investate.R;
import com.example.praveen_investate.database.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AnalyticsActivity extends AppCompatActivity {

    private WebView chartWebView;
    private Spinner stateSpinner, districtSpinner;
    private Button filterButton;
    private boolean isGifPlaying = false;
    private ImageButton ana;



    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);


        ToastMaker.toast("This is prototype, we will let you know after research and development..!",this);
        // Initialize views
        stateSpinner = findViewById(R.id.state_spinner);
        ana = findViewById(R.id.ana_p_id);
        districtSpinner = findViewById(R.id.district_spinner);
        filterButton = findViewById(R.id.filter_button);
        chartWebView = findViewById(R.id.chart_webview);
        chartWebView.getSettings().setJavaScriptEnabled(true);
        chartWebView.getSettings().setDomStorageEnabled(true);
        chartWebView.setWebViewClient(new WebViewClient());


        // Set up spinners for state and district
        ArrayAdapter<CharSequence> stateAdapter = ArrayAdapter.createFromResource(this,
                R.array.states, android.R.layout.simple_spinner_item);
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stateSpinner.setAdapter(stateAdapter);

        ArrayAdapter<CharSequence> districtAdapter = ArrayAdapter.createFromResource(this,
                R.array.districts, android.R.layout.simple_spinner_item);
        districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        districtSpinner.setAdapter(districtAdapter);

        // Set the button click listener to fetch data based on the selected state and district
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView tv2 = findViewById(R.id.ana_tv2);
                tv2.setVisibility(View.GONE);
                ImageView im = findViewById(R.id.imageana);
                im.setVisibility(View.GONE);
                Glide.with(AnalyticsActivity.this)
                        .asGif() // Specify that we want to load a GIF
                        .load(R.drawable.anaprogress) // Reference to the drawable resource
                        .transition(DrawableTransitionOptions.withCrossFade(500)) // 500 ms fade-in
                        .into(ana);
                ana.postDelayed(() -> {
                    // Create fade-out animation from 100% opacity to 0%
                    AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f); // Fade from visible to invisible
                    fadeOut.setDuration(500); // 1 second fade-out duration
                    ana.startAnimation(fadeOut); // Start the fade-out animation

                    // Optionally hide the ImageView after fade-out
                    ana.setVisibility(ImageButton.GONE);
                }, 2500);
                chartWebView.setVisibility(View.GONE);
                ana.setVisibility(View.VISIBLE);
                String selectedState = stateSpinner.getSelectedItem().toString();
                String selectedDistrict = districtSpinner.getSelectedItem().toString();

                fetchAnalyticsData(selectedState, selectedDistrict);
            }
        });
    }



    private void fetchAnalyticsData(String state, String district) {
        String url = "http://10.0.2.2:1010/api/posts/getall-posts";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray postsArray = new JSONArray(response);
                            JSONObject analyticsData = processPostsData(postsArray, state, district);
                            Log.e("Analytics", analyticsData.toString());

                            new Handler().postDelayed(() -> {
                                try {

                                    renderChart(analyticsData);


                                } catch (JSONException e) {
                                    ToastMaker.toast("in analytics class in fetch method 134 line",AnalyticsActivity.this);
                                }
                            }, 2500);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        Volley.newRequestQueue(this).add(stringRequest);
    }

    private JSONObject processPostsData(JSONArray postsArray, String state, String district) {
        JSONObject analyticsData = new JSONObject();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM", Locale.getDefault());

        try {
            JSONObject propertyTypeCount = new JSONObject();
            JSONArray priceData = new JSONArray();
            JSONArray priceTrendsByType = new JSONArray();

            for (int i = 0; i < postsArray.length(); i++) {
                JSONObject post = postsArray.getJSONObject(i);
                String postState = post.getString("state");
                String postDistrict = post.getString("district");

                if (postState.equals(state) && postDistrict.equals(district)) {
                    String propertyType = post.getString("propertyType");
                    double price = post.getDouble("totalPrice");
                    String dateStr = post.getString("createdAt");

                    Date postDate = null;
                    try {
                        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
                        postDate = originalFormat.parse(dateStr);
                    } catch (Exception e) {
                        e.printStackTrace();
                        continue;
                    }

                    String formattedDate = dateFormat.format(postDate);

                    if (!propertyTypeCount.has(propertyType)) {
                        propertyTypeCount.put(propertyType, 0);
                    }
                    propertyTypeCount.put(propertyType, propertyTypeCount.getInt(propertyType) + 1);

                    JSONObject pricePoint = new JSONObject();
                    pricePoint.put("date", formattedDate);
                    pricePoint.put("price", price);
                    pricePoint.put("propertyType", propertyType);
                    priceData.put(pricePoint);

                    JSONObject priceTrend = new JSONObject();
                    priceTrend.put("propertyType", propertyType);
                    priceTrend.put("price", price);
                    priceTrendsByType.put(priceTrend);
                }
            }

            analyticsData.put("propertyTypeCount", propertyTypeCount);
            analyticsData.put("priceData", priceData);
            analyticsData.put("priceTrendsByType", priceTrendsByType);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return analyticsData;
    }

    private void renderChart(JSONObject analyticsData) throws JSONException {
        String chartHtml = "<html><body>" +
                "<h3>Analytics</h3>" +

                // Bar Chart (Property Types)
                "<h4>Property Type Distribution</h4>" +
                "<canvas id='barChart'></canvas>" +
                "<p>This bar chart shows the distribution of property types in the selected region. Each bar represents a property type, and the height of the bar indicates the number of properties for that type.</p>" +

                // Line Chart (Price Trends)
                "<h4>Price Trends by Property Type</h4>" +
                "<canvas id='lineChart'></canvas>" +
                "<p>This line chart illustrates the price trends for different property types over time. Each line represents a property type, and the movement of the line indicates price fluctuations over time.</p>" +

                "<script src='https://cdn.jsdelivr.net/npm/chart.js'></script>" +
                "<script>" +

                // Bar Chart Data
                "var propertyTypeCount = " + analyticsData.getJSONObject("propertyTypeCount").toString() + ";" +
                "var barLabels = Object.keys(propertyTypeCount);" +
                "var barValues = Object.values(propertyTypeCount);" +
                "var barCtx = document.getElementById('barChart').getContext('2d');" +
                "var barChart = new Chart(barCtx, {" +
                "    type: 'bar'," +
                "    data: {" +
                "        labels: barLabels," +
                "        datasets: [{" +
                "            data: barValues," +
                "            backgroundColor: ['#FF5733', '#33FF57', '#3357FF', '#FF33A1', '#FFC300'], " +  // Colorful bars
                "            borderColor: ['#D43F00', '#228B22', '#1F4C9C', '#D32F6A', '#FFB300'], " + // Border colors for better visibility
                "            borderWidth: 1" +
                "        }]" +
                "    }," +
                "    options: {" +
                "        responsive: true," +
                "        scales: {" +
                "            y: {" +
                "                beginAtZero: true," +
                "                ticks: {" +
                "                    stepSize: 1," + // Increment Y-axis by 1
                "                    min: 1" + // Minimum Y value starts at 1
                "                }" +
                "            }" +
                "        }," +
                "        plugins: {" +
                "            legend: {" +
                "                display: false" +  // Hide legend for simplicity
                "            }" +
                "        }" +
                "    }" +
                "});" +

                // Line Chart Data
                "var priceData = " + analyticsData.getJSONArray("priceData").toString() + ";" +
                "var lineData = {};" +
                "priceData.forEach(function(point) {" +
                "    var propertyType = point.propertyType;" +
                "    if (!lineData[propertyType]) {" +
                "        lineData[propertyType] = [];" +
                "    }" +
                "    lineData[propertyType].push({" +
                "        date: point.date," +
                "        price: point.price" +
                "    });" +
                "});" +

                "var lineCtx = document.getElementById('lineChart').getContext('2d');" +
                "var lineChart = new Chart(lineCtx, {" +
                "    type: 'line'," +
                "    data: {" +
                "        labels: priceData.map(function(item) { return item.date; })," +
                "        datasets: Object.keys(lineData).map(function(propertyType) {" +
                "            return {" +
                "                label: propertyType," +
                "                data: lineData[propertyType].map(function(point) { return point.price; })," +
                "                borderColor: getRandomColor()," +
                "                fill: false," +
                "                tension: 0.4" + // Smooth the lines for a better look
                "            };" +
                "        })" +
                "    }," +
                "    options: {" +
                "        responsive: true," +
                "        scales: {" +
                "            y: {" +
                "                ticks: {" +
                "                    beginAtZero: true" +
                "                }" +
                "            }" +
                "        }" +
                "    }" +
                "});" +

                // Random color generator for line chart
                "function getRandomColor() {" +
                "    var letters = '0123456789ABCDEF';" +
                "    var color = '#';" +
                "    for (var i = 0; i < 6; i++) {" +
                "        color += letters[Math.floor(Math.random() * 16)];" +
                "    }" +
                "    return color;" +
                "}" +

                "</script>" +
                "</body></html>";

        chartWebView.loadDataWithBaseURL(null, chartHtml, "text/html", "UTF-8", null);
        ana.setVisibility(View.GONE);
        chartWebView.setVisibility(View.VISIBLE);
    }

}

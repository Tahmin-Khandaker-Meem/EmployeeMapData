package com.example.employeemapdata;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public static String textViewData = "";
    public static HashMap<String, String> employeeData;
    ArrayList<String> nameList;
    ArrayAdapter arrayAdapter;
    Intent intent;
    TextView textView;
    SQLiteDatabase mydatabase;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.lists);
        nameList = new ArrayList<String>();
        arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,nameList);
        listView.setAdapter(arrayAdapter);
        textView = (TextView) findViewById(R.id.timerDisplay);
        employeeData = new HashMap<String, String>();
        mydatabase = openOrCreateDatabase("employees.db",MODE_PRIVATE,null);
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS employee_details(name VARCHAR, location VARCHAR);");
        intent = new Intent(this,EmployeeMaps.class);
        setupListViewListeners();
    }

    public void loadJSON(View view) {
        fetchData process = new fetchData();
        process.execute();
        textView.setText(textViewData); // Perfect
    }

    public void makeHash(View view) {
        try {
            JSONArray jsonArray = new JSONArray(textViewData);
            for (int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject = (JSONObject) jsonArray.getJSONObject(i);
                String name = ""+jsonObject.get("name");
                String location ="";
                if(!jsonObject.isNull("location")) {
                    String rawLocation = "" + jsonObject.get("location");
                    JSONObject locationJSON = new JSONObject(rawLocation);
                    location = ""+locationJSON.get("latitude")+","+locationJSON.get("longitude");
                }
                else {
                    location = "23.747078,90.417118";
                }
                employeeData.put(name,location);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        textViewData = "";
        for (Map.Entry<String, String> keys : employeeData.entrySet()) {
            textViewData += "Name: " + keys.getKey()+". Location: "+keys.getValue()+"\n";
        }
        textView.setText(textViewData);   // Perfect. No need to change any code
    }

    private void setupListViewListeners() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Cursor cursor = mydatabase.rawQuery("select * from employee_details",null);
                String employeeName = nameList.get(position);

                while (cursor.moveToNext()){
                    if (cursor.getString(0).equals(employeeName)){
                        String loc = cursor.getString(1);
                        String [] locationArray = loc.split(",");
                        double [] latlng = new double [2];
                        for (int i=0; i<locationArray.length;i++){
                            latlng[i] = Double.parseDouble(locationArray[i]);
                        }
                        intent.putExtra("employeeName",employeeName);
                        intent.putExtra("latitude",latlng[0]);
                        intent.putExtra("longitude",latlng[1]);
                        startActivity(intent);
                        break;
                    }
                }

            }
        });
    }

    public void createDatabase(View view) {
        for (Map.Entry<String, String> keys : employeeData.entrySet()) {
            mydatabase.execSQL("REPLACE INTO employee_details VALUES('"+keys.getKey()+"','"+keys.getValue()+"');");
        }
        employeeData.clear();
    }

    public void retrive(View view) {
        Cursor cursor = mydatabase.rawQuery("select * from employee_details",null);
        if (cursor.getCount() == 0){
            Toast.makeText(this, "No more data",Toast.LENGTH_SHORT).show();
            return;
        }
        employeeData.clear();
        arrayAdapter.clear();
        while (cursor.moveToNext()){
            String naaaam = cursor.getString(0);
            employeeData.put(naaaam,cursor.getString(1));
            arrayAdapter.add(""+naaaam);
        }
        textViewData = "";
        for (Map.Entry<String, String> keys : employeeData.entrySet()) {
            textViewData += "Name: " + keys.getKey()+". Location: "+keys.getValue()+"\n";
        }
        textView.setText(textViewData);
    }

    public void clearTextArea(View view) {
        textView.setText("");
    }

    public void viewOnMap(View view) {
        Intent intent = new Intent(this,EmployeeMaps.class);
        startActivity(intent);
    }
}

package edu.purdue.srandhaw.foodie;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Pop extends AppCompatActivity {
    static String finale = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_popup);

        TextView tv = (TextView) findViewById(R.id.tvTitle);
        final EditText et = (EditText) findViewById(R.id.etAlternative);
        ImageButton ib = (ImageButton) findViewById(R.id.btnplus);
        final ArrayList<String> description = new ArrayList<String>();
        int descriptionCounter = 0;
        String response = getIntent().getStringExtra("Key");
        final int index = getIntent().getIntExtra("index", 0);
        final ArrayList<String> dJSONArray = getIntent().getStringArrayListExtra("Array");

        try {
            JSONObject parentObject = new JSONObject(dJSONArray.get(index));
            JSONArray parentArray = parentObject.getJSONArray("responses");
            JSONObject childObject = parentArray.getJSONObject(0);
            JSONArray childArray = childObject.getJSONArray("labelAnnotations");


            for (int i = 0; i < childArray.length(); i++) {
                JSONObject finalObject = childArray.getJSONObject(i);
                description.add(finalObject.getString("description"));
            }
        } catch (Exception e) {
            System.out.println("Description counter: " + descriptionCounter);
            e.printStackTrace();
        }
        ListView lv = (ListView) findViewById(R.id.lvAlternatives);
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, description);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                finale = description.get(i);

                MainActivity.foodItems.set(index, finale);
                System.out.println(dJSONArray);
                MainActivity.customAdapter.notifyDataSetChanged();
                Intent intent = new Intent(Pop.this, MainActivity.class);
                startActivity(intent);

            }
        });

        ib.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                finale = et.getText().toString();
                et.setText("");
                System.out.println(dJSONArray);
                MainActivity.foodItems.set(index, finale);
                MainActivity.customAdapter.notifyDataSetChanged();
                Intent intent = new Intent(Pop.this, MainActivity.class);
                startActivity(intent);
            }
        });

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * .6));
    }
}

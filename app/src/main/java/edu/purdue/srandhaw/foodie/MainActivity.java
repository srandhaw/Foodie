package edu.purdue.srandhaw.foodie;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
 EditText searchBar;

    Button searchButton;

    ListView listView;
    int searchCounter=0;
 ArrayList<String> foodItems = new ArrayList<String>();
    CustomAdapter customAdapter=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchBar = (EditText)findViewById(R.id.etsearchbar);
        searchButton = (Button)findViewById(R.id.buttonsearch);
        listView = (ListView)findViewById(R.id.listfood);
        searchButton.setOnClickListener(this);


        customAdapter = new CustomAdapter(MainActivity.this,R.layout.custom_foodlistrow,foodItems);
        listView.setAdapter(customAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {


            case R.id.buttonsearch:
            {
             if(searchBar.getText()!=null){
                 foodItems.add(searchBar.getText().toString());
             }
            }

            case R.id.btgo:
            {

            }



        }
    }


    class CustomAdapter extends ArrayAdapter<String> {
        TextView txtView;
        ArrayList<String> temp;

       public CustomAdapter(Context context,int textViewResourceId,ArrayList<String> objects){
           super(context, textViewResourceId,objects);
           this.temp = objects;
       }


        public View getView(final int position, View convertView, ViewGroup parent) {
           View view = getLayoutInflater().inflate(R.layout.custom_foodlistrow, parent, false);

            Button btn = (Button) view.findViewById(R.id.button);
            txtView = (TextView) view.findViewById(R.id.textView);
            ImageButton ib = (ImageButton)view.findViewById(R.id.imageButton);
            txtView.setText(foodItems.get(position));

            ib.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    temp.remove(position);
                    customAdapter.notifyDataSetChanged();
                }
            });


            return view;
        }




    }
}

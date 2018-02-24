package edu.purdue.srandhaw.foodie;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
 EditText searchBar;

    Button searchButton;
    ImageButton cameraButton;
    ImageView iv;

    ListView listView;
    int searchCounter=0;
 ArrayList<String> foodItems = new ArrayList<String>();
    CustomAdapter customAdapter=null;
    static final int CAM = 1111;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchBar = (EditText)findViewById(R.id.etsearchbar);
        searchButton = (Button)findViewById(R.id.buttonsearch);
        cameraButton = (ImageButton)findViewById(R.id.camera);
        listView = (ListView)findViewById(R.id.listfood);
        searchButton.setOnClickListener(this);


        customAdapter = new CustomAdapter(MainActivity.this,R.layout.custom_foodlistrow,foodItems);
        listView.setAdapter(customAdapter);

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAM);
            }
        });
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
            break;

            case R.id.btgo:
            {

            }
            break;



        }
    }



    private File getFile() {
     File folder = new File("camera_app");

     File image_file = new File(folder,"image.jpg");
     return image_file;

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAM) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            // now we can access the photo using bitmap -> photo
        }
    }
}

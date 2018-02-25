package edu.purdue.srandhaw.foodie;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.zip.CheckedInputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    static final int CAM = 1111;
    EditText searchBar;
    Button searchButton;
    ImageButton cameraButton;
    ImageView iv;
    ListView listView;
    Button btgo;
    int searchCounter = 0;
   static ArrayList<String> foodItems = new ArrayList<String>();
    static CustomAdapter customAdapter = null;

    byte[] byteArray;
    ArrayList<String> JSONArray = new ArrayList<>();

    String final_JSONString="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchBar = (EditText) findViewById(R.id.etsearchbar);
        searchButton = (Button) findViewById(R.id.buttonsearch);
        cameraButton = (ImageButton) findViewById(R.id.camera);
        listView = (ListView) findViewById(R.id.listfood);
        btgo = (Button) findViewById(R.id.btgo);
        btgo.setOnClickListener(this);
        searchButton.setOnClickListener(this);
        searchBar.setHint("ENTER OR CAPTURE INGREDIENT " + (foodItems.size() + 1));


        customAdapter = new CustomAdapter(MainActivity.this, R.layout.custom_foodlistrow, foodItems);
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
        switch (view.getId()) {


            case R.id.buttonsearch: {

                if (searchBar.getText() != null) {

                    foodItems.add(searchBar.getText().toString());
                    searchBar.setText("");
                    searchBar.setHint("ENTER OR CAPTURE INGREDIENT " + (foodItems.size() + 1));
                    customAdapter.notifyDataSetChanged();
                }
            }
            break;

            case R.id.btgo: {
                System.out.println(" go works");
Intent intent = new Intent(MainActivity.this,RecipeActivity.class);
intent.putExtra("foodArray",foodItems);
startActivity(intent);


            }
            break;


        }
    }


    private File getFile() {
        File folder = new File("camera_app");

        File image_file = new File(folder, "image.jpg");
        return image_file;

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) throws NullPointerException {
        if (requestCode == CAM) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            // now we can access the photo using bitmap -> photo

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byteArray = stream.toByteArray();

            new JSONTask().execute("http://overlyliteral.com/projects/testFoodiePOST.php");

        }
    }

    public class CustomAdapter extends ArrayAdapter<String> {
        TextView txtView;
        ArrayList<String> temp;

        public CustomAdapter(Context context, int textViewResourceId, ArrayList<String> objects) {
            super(context, textViewResourceId, objects);
            this.temp = objects;
        }


        public View getView(final int position, View convertView, ViewGroup parent) {
            final View view = getLayoutInflater().inflate(R.layout.custom_foodlistrow, parent, false);

            Button infobtn = (Button) view.findViewById(R.id.button);
            txtView = (TextView) view.findViewById(R.id.textView);
            ImageButton ib = (ImageButton) view.findViewById(R.id.imageButton);
            txtView.setText(foodItems.get(position));

            ib.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    temp.remove(position);
                    customAdapter.notifyDataSetChanged();
                }
            });
            infobtn.setOnClickListener(new View.OnClickListener() {
                Dialog myDialog;
                @Override
                public void onClick(View view) {
                     Intent intent=new Intent(MainActivity.this,Pop.class);
                     intent.putExtra("Key",final_JSONString);
                     intent.putExtra("index",foodItems.size()-1);
                     intent.putExtra("Array",JSONArray);
                     startActivity(intent);
                   //  temp.set(position,Pop.finale);
                //    customAdapter.notifyDataSetChanged();

                }


            });




            return view;
        }


    }

    public class JSONTask extends AsyncTask<String, String, String> {


        @Override


        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader bfr = null;
            try {
////                Base64.Encoder encoder = Base64.getEncoder();
//                String encoded = "somerequest=" + Base64.encodeToString(byteArray,Base64.DEFAULT);
//                // System.out.println(encoded);
              URL url = new URL(strings[0]);
       //     connection = (HttpURLConnection) url.openConnection();
//               connection.connect();
//              InputStream stream = connection.getInputStream();
//                bfr = new BufferedReader(new InputStreamReader(stream));
                try {
                    Base64.Encoder encoder = Base64.getEncoder();
                    String encoded = "somerequest=" + encoder.encodeToString(byteArray);
                    // System.out.println(encoded);
                    HttpURLConnection httpsURLConnection = (HttpURLConnection) url.openConnection();
                    httpsURLConnection.setRequestMethod("POST");

                    httpsURLConnection.setDoOutput(true);

//                    String JSON = encoded;

                    DataOutputStream dataOutputStream = new DataOutputStream(httpsURLConnection.getOutputStream());

                    dataOutputStream.writeBytes(encoded);
                    dataOutputStream.flush();

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));

                    StringBuilder response = new StringBuilder();

                    String currentResponse ;
                    while ((currentResponse = bufferedReader.readLine()) != null) {
                        response.append(currentResponse);
                    }
final_JSONString=response.toString();
                    JSONArray.add(final_JSONString);
                    //System.out.println(response);
                    return response.toString();

                } catch (Exception e) {
                    e.printStackTrace();
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                if (bfr != null) {
                    try {
                        bfr.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return "No internet connection found.";
        }


        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String JSON_String = result;
            String JSON_String1 = JSON_String.substring(JSON_String.indexOf("description"));
            String JSON_String2 = JSON_String1.substring(JSON_String1.indexOf("\":"));
            String JSON_String3 = JSON_String2.substring(JSON_String2.indexOf(" "));
            String JSON_String4 = JSON_String3.substring(JSON_String3.indexOf("\""), JSON_String3.indexOf(","));
            String JSON_String5 = JSON_String4.replace("\"", "");
            foodItems.add(JSON_String5);
            searchBar.setText("");
            searchBar.setHint("ENTER OR CAPTURE INGREDIENT " + (foodItems.size() + 1));
            customAdapter.notifyDataSetChanged();

            //Toast.makeText(getApplicationContext(),JSON_String5,Toast.LENGTH_LONG).show();
        }
    }


}

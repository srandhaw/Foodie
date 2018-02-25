package edu.purdue.srandhaw.foodie;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;

public class RecipeActivity extends AppCompatActivity {
    String[] urlArray;
    ArrayList<String> titleArray = new ArrayList<>();
    ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        TextView tv = (TextView) findViewById(R.id.tvTitle);
        lv=(ListView) findViewById(R.id.lvRecipies);

        ArrayList<String> ingredients = getIntent().getStringArrayListExtra("foodArray");
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ingredients);
        lv.setAdapter(adapter);

        StringBuilder foodItems= new StringBuilder("http://food2fork.com/api/search?key=fc99fad477a16a16e574c69eed41db44&q=");
        for(int i=0;i<ingredients.size();i++){
            foodItems.append(ingredients.get(i));
            foodItems.append(",");
        }
        foodItems.deleteCharAt(foodItems.length()-1);
        new RecipeActivity.JSONTask().execute(foodItems.toString());



    }
    public class JSONTask extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader bfr = null;
            try {

            URL urlRecipe = new URL(strings[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) urlRecipe.openConnection();

                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setDoInput(true);
                httpURLConnection.addRequestProperty("User-Agent", "Mozilla/4.76");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String currentResponse;

                while ((currentResponse = bufferedReader.readLine()) != null) {
                    response.append(currentResponse);
                }

                    return response.toString();

                }
            catch (MalformedURLException e) {
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
            try {
                JSONObject jsonObject = new JSONObject(result);
int recipeLength = jsonObject.getInt("count");
urlArray = new String[recipeLength];
//titleArray = new String[recipeLength];

                JSONArray jsonArray = jsonObject.getJSONArray("recipes");
                for(int i=0;i<jsonArray.length();i++){
                    urlArray[i] = jsonArray.getJSONObject(i).getString("f2f_url");
                    titleArray.add(jsonArray.getJSONObject(i).getString("title"));
                }
                ArrayAdapter adapter = new ArrayAdapter<String>(RecipeActivity.this, android.R.layout.simple_list_item_1,titleArray);
                lv.setAdapter(adapter);

                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(RecipeActivity.this,WebViewActivity.class);
                        intent.putExtra("url",urlArray[i]);
                        startActivity(intent);
                    }
                });


            }catch (Exception e){
                e.printStackTrace();
            }



        }
    }
}

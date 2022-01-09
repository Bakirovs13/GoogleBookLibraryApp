package kg.geektech.googlebooklibraryapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import kg.geektech.googlebooklibraryapp.adapters.BookAdapter;
import kg.geektech.googlebooklibraryapp.databinding.ActivityMainBinding;
import kg.geektech.googlebooklibraryapp.models.BookInfo;

public class MainActivity extends AppCompatActivity {


    ActivityMainBinding binding;
    // creating variables for our request queue
    // array list
    private RequestQueue mRequestQueue;
    private ArrayList<BookInfo> bookInfoArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initListeners();

    }

    private void initListeners() {
        // initializing on click listener for our button.
        binding.idBtnSearch.setOnClickListener(v -> {
            binding.idLoadingPB.setVisibility(View.VISIBLE);

            // checking if our edittext field is empty or not.
            if (binding.idEdtSearchBooks.getText().toString().isEmpty()) {
                binding.idEdtSearchBooks.setError("Please enter search query");
                return;
            }
            // if the search query is not empty then we are
            // calling get book info method to load all
            // the books from the API.
            getBooksInfo(binding.idEdtSearchBooks.getText().toString());
        });
    }

    private void initAdapter() {
        BookAdapter adapter = new BookAdapter(bookInfoArrayList, MainActivity.this);
        binding.idRVBooks.setAdapter(adapter);

    }

    private void getBooksInfo(String query) {

        // creating a new array list.
        bookInfoArrayList = new ArrayList<>();

        // below line is use to initialize
        // the variable for our request queue.
        mRequestQueue = Volley.newRequestQueue(MainActivity.this);

        // below line is use to clear cache this
        // will be use when our data is being updated.
        mRequestQueue.getCache().clear();

        // below is the url for getting data from API in json format.
        String url = "https://www.googleapis.com/books/v1/volumes?q=" + query;

        // below line we are creating a new request queue.
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);


        // below line is use to make json object request inside that we
        // are passing url, get method and getting json object. .
        JsonObjectRequest booksObjrequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            binding.idLoadingPB.setVisibility(View.GONE);
            // inside on response method we are extracting all our json data.
            try {
                JSONArray itemsArray = response.getJSONArray("items");
                for (int i = 0; i < itemsArray.length(); i++) {
                    JSONObject itemsObj = itemsArray.getJSONObject(i);
                    JSONObject volumeObj = itemsObj.getJSONObject("volumeInfo");
                    String title = volumeObj.optString("title");
                    String subtitle = volumeObj.optString("subtitle");
                    JSONArray authorsArray = volumeObj.getJSONArray("authors");
                    String publisher = volumeObj.optString("publisher");
                    String publishedDate = volumeObj.optString("publishedDate");
                    String description = volumeObj.optString("description");
                    int pageCount = volumeObj.optInt("pageCount");
                    JSONObject imageLinks = volumeObj.optJSONObject("imageLinks");
                    assert imageLinks != null;
                    String thumbnail = imageLinks.optString("thumbnail");
                    String previewLink = volumeObj.optString("previewLink");
                    String infoLink = volumeObj.optString("infoLink");
                    JSONObject saleInfoObj = itemsObj.optJSONObject("saleInfo");
                    assert saleInfoObj != null;
                    String buyLink = saleInfoObj.optString("buyLink");
                    ArrayList<String> authorsArrayList = new ArrayList<>();
                    if (authorsArray.length() != 0) {
                        for (int j = 0; j < authorsArray.length(); j++) {
                            authorsArrayList.add(authorsArray.optString(i));
                        }
                    }
                    // after extracting all the data we are
                    // saving this data in our modal class.
                    BookInfo bookInfo = new BookInfo(title, subtitle, authorsArrayList, publisher, publishedDate, description, pageCount, thumbnail, previewLink, infoLink, buyLink);
                    initAdapter();
                    // below line is use to pass our modal
                    // class in our array list.
                    bookInfoArrayList.add(bookInfo);

                }
            } catch (JSONException e) {
                e.printStackTrace();
                // displaying a toast message when we get any error from API
                Toast.makeText(MainActivity.this, "No Data Found" + e, Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            // also displaying error message in toast.
            Toast.makeText(MainActivity.this, "Error found is " + error, Toast.LENGTH_SHORT).show();
        });
        // at last we are adding our json object
        // request in our request queue.
        queue.add(booksObjrequest);
    }
}


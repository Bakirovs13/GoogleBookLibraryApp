package kg.geektech.googlebooklibraryapp.ui;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import kg.geektech.googlebooklibraryapp.R;
import kg.geektech.googlebooklibraryapp.databinding.ActivityBookDetailBinding;


public class BookDetail extends AppCompatActivity {
    //creating variables for strings,text view, image views and button.
    String title, subtitle, publisher, publishedDate, description, thumbnail, previewLink, infoLink, buyLink;
    int pageCount;
    ActivityBookDetailBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getData();
        setData();
        initListeners();
    }

    private void initListeners() {
        //adding on click listner for our preview button.
        binding.idBtnPreview.setOnClickListener(v -> {
            if(previewLink.isEmpty()){
                //below toast message is displayed when preview link is not present.
                Toast.makeText(BookDetail.this, "No preview Link present", Toast.LENGTH_SHORT).show();
                return;
            }
            //if the link is present we are opening that link via an intent.
            Uri uri = Uri.parse(previewLink);
            Intent i = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(i);
        });

        //initializing on click listner for buy button.
        binding.idBtnBuy.setOnClickListener(v -> {
            if (buyLink.isEmpty()) {
                //below toast message is displaying when buy link is empty.
                Toast.makeText(BookDetail.this, "No buy page present for this book", Toast.LENGTH_SHORT).show();
                return;
            }
            //if the link is present we are opening the link via an intent.
            Uri uri = Uri.parse(buyLink);
            Intent i = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(i);
        });

    }

    @SuppressLint("SetTextI18n")
    private void setData() {
        //after getting the data we are setting that data to our text views and image view.
        binding.idTVTitle.setText(title);
        binding.idTVSubTitle.setText(subtitle);
        binding.idTVpublisher.setText(publisher);
        binding.idTVPublishDate.setText("Published On : " + publishedDate);
        binding.idTVDescription.setText(description);
        binding.idTVNoOfPages.setText("No Of Pages : " + pageCount);
        Picasso.get().load(thumbnail).into(binding.idIVbook);


    }

    private void getData() {
        //getting the data which we have passesd from our adapter class.
        title = getIntent().getStringExtra("title");
        subtitle = getIntent().getStringExtra("subtitle");
        publisher = getIntent().getStringExtra("publisher");
        publishedDate = getIntent().getStringExtra("publishedDate");
        description = getIntent().getStringExtra("description");
        pageCount = getIntent().getIntExtra("pageCount", 0);
        thumbnail = getIntent().getStringExtra("thumbnail");
        previewLink = getIntent().getStringExtra("previewLink");
        infoLink = getIntent().getStringExtra("infoLink");
        buyLink = getIntent().getStringExtra("buyLink");
    }
}
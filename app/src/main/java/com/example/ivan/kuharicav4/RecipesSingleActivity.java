package com.example.ivan.kuharicav4;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class RecipesSingleActivity extends AppCompatActivity {

    private String mPost_key = null;

    private DatabaseReference mDatabase;

    private ImageView mRecipesSingleImage;
    private TextView mRecipesSingleTitle;
    private TextView mRecipesSingleDesc;
    private TextView mRecipesSingleIng;
    private TextView mRecipesSinglePrTime;
    private TextView mRecipesSingleCategories;

    private FirebaseAuth mAuth;

    private Button mSingleRemoveBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_single);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Recipes");
        mAuth = FirebaseAuth.getInstance();

        mPost_key = getIntent().getExtras().getString("recipes_id");

        mRecipesSingleTitle = (TextView) findViewById(R.id.singleRecipesTitle);
        mRecipesSingleDesc = (TextView) findViewById(R.id.singleRecipesDesc);
        mRecipesSingleIng = (TextView) findViewById(R.id.singleRecipesIng);
        mRecipesSingleImage = (ImageView) findViewById(R.id.singleRecipesImage);
        mRecipesSinglePrTime = (TextView) findViewById(R.id.singleRecipesPrTime);
        mRecipesSingleCategories = (TextView) findViewById(R.id.singleRecipesCategories);

        mSingleRemoveBtn = (Button) findViewById(R.id.singleRemoveBtn);


        mDatabase.child(mPost_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String post_title = (String) dataSnapshot.child("title").getValue();
                String post_desc = (String) dataSnapshot.child("desc").getValue();
                String post_ing = (String) dataSnapshot.child("ingredients").getValue();
                String post_prTime = (String) dataSnapshot.child("prTime").getValue();
                String post_categories = (String) dataSnapshot.child("categories").getValue();
                String post_image = (String) dataSnapshot.child("image").getValue();
                String post_uid = (String) dataSnapshot.child("uid").getValue();

                mRecipesSingleTitle.setText(post_title);
                mRecipesSingleDesc.setText(post_desc);
                mRecipesSingleIng.setText(post_ing);
                mRecipesSinglePrTime.setText(post_prTime);
                mRecipesSingleCategories.setText(post_categories);
                Picasso.with(RecipesSingleActivity.this).load(post_image).into(mRecipesSingleImage);

                Toast.makeText(RecipesSingleActivity.this, post_uid, Toast.LENGTH_LONG).show();

                if (mAuth.getCurrentUser().getUid().equals(post_uid)){

                    mSingleRemoveBtn.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mSingleRemoveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDatabase.child(mPost_key).removeValue();
                Intent mainIntent = new Intent(RecipesSingleActivity.this, MainActivity.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainIntent);
            }
        });
    }
}

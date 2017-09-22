package com.example.ivan.kuharicav4;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class PostActivity extends AppCompatActivity {

    private ImageButton mSelectImage;
    private EditText mrecNameTitle;
    private EditText mPostDesc;
    private Button mSubmitBtn;
    ArrayAdapter<CharSequence> adapter;
    ArrayAdapter<CharSequence> adapter1;
    Spinner mPrTimeSpinner;
    Spinner mCategoriesSpinner;

    private EditText mIng;
    private Button btn;
    private ListView list;
    private ArrayAdapter<String> adapter2;
    private ArrayList<String> arrayList;

    private Uri mImageUri = null;

    private static final int GALLERY_REQUEST = 1;

    private StorageReference mStorage;
    private DatabaseReference mDatabase;

    private ProgressDialog mProgress;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mDatabaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Recipes");

        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());

        mSelectImage = (ImageButton) findViewById(R.id.imageSelect);

        mrecNameTitle = (EditText) findViewById(R.id.recNameField);
        mPostDesc = (EditText) findViewById(R.id.descField);
        mSubmitBtn = (Button) findViewById(R.id.submitBtn);

        mIng = (EditText) findViewById(R.id.ingField);
        btn = (Button) findViewById(R.id.addBtn);
        list = (ListView) findViewById(R.id.list);
        arrayList = new ArrayList<String>();

        mProgress = new ProgressDialog(this);

        mPrTimeSpinner = (Spinner) findViewById(R.id.prTimeSpinner);
        adapter = ArrayAdapter.createFromResource(this,R.array.prTime_array,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPrTimeSpinner.setAdapter(adapter);

        adapter2 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayList);

        // Here, you set the data in your ListView
        list.setAdapter(adapter2);

        mPrTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        mCategoriesSpinner = (Spinner) findViewById(R.id.categoriesSpinner);
        adapter1 = ArrayAdapter.createFromResource(this,R.array.categories_array,android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCategoriesSpinner.setAdapter(adapter1);

        mCategoriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
            }
        });

        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPosting();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // this line adds the data of your EditText and puts in your array
                arrayList.add(mIng.getText().toString());
                // next thing you have to do is check if your adapter has changed
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void startPosting(){

        mProgress.setMessage("Posting to database...");

        final String title_val = mrecNameTitle.getText().toString().trim();
        final String desc_val = mPostDesc.getText().toString().trim();
        final String ing_val = arrayList.toString().trim();
        final String prTimeSpinner_val = mPrTimeSpinner.getSelectedItem().toString().trim();
        final String categoriesSpinner_val = mCategoriesSpinner.getSelectedItem().toString().trim();

        if (!TextUtils.isEmpty(title_val) && !TextUtils.isEmpty(desc_val)&& !TextUtils.isEmpty(ing_val) && !TextUtils.isEmpty(prTimeSpinner_val) && !TextUtils.isEmpty(categoriesSpinner_val) && mImageUri !=null){
            mProgress.show();

            StorageReference filepath = mStorage.child("Recipes_Images").child(mImageUri.getLastPathSegment());


            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    final Uri downloadUrl = taskSnapshot.getDownloadUrl();

                    final DatabaseReference newPost = mDatabase.push();



                    mDatabaseUser.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            newPost.child("title").setValue(title_val);
                            newPost.child("desc").setValue(desc_val);
                            newPost.child("ingredients").setValue(ing_val);
                            newPost.child("prTime").setValue(prTimeSpinner_val);
                            newPost.child("categories").setValue(categoriesSpinner_val);
                            newPost.child("image").setValue(downloadUrl.toString());
                            newPost.child("uid").setValue(mCurrentUser.getUid());
                            newPost.child("username").setValue(dataSnapshot.child("name").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()){
                                        startActivity(new Intent(PostActivity.this, MainActivity.class));
                                    }
                                }
                            });

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {


                        }
                    });


                    mProgress.dismiss();


                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){

            mImageUri = data.getData();

            mSelectImage.setImageURI(mImageUri);
        }
    }
}

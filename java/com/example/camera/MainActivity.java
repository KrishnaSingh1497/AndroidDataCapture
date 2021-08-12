package com.example.camera;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    Button button;
    Button btnCalculate;
    FirebaseStorage storage;
    Uri imageUri;
    TextView textView;
    EditText txtname;
    EditText txtlotno;
    EditText txtpieces;
    EditText txtsizelen;
    EditText txtsizebred;
    EditText txtsqft;
    EditText txtremark;
    EditText txtthickness;

    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_main);

        txtname = findViewById(R.id.txtname);
        txtlotno= findViewById(R.id.txtlotno);
        txtpieces = findViewById(R.id.txtpieces);
        txtsizelen = findViewById(R.id.txtsizelen);
        txtsizebred = findViewById(R.id.txtsizebred);
        txtsqft = findViewById(R.id.txtsqft);
        txtremark = findViewById(R.id.txtremark);
        txtthickness = findViewById(R.id.txtthickness);
        textView = findViewById(R.id.textView);

        myRef = FirebaseDatabase.getInstance().getReference().child("Stock");


        button= findViewById(R.id.button);
        btnCalculate = findViewById(R.id.btnCalculate);
        imageView = findViewById(R.id.imageView);
        storage = FirebaseStorage.getInstance();

        //Calculation of sqft from length x width x pieces
        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // Calculate();
                double num1 = Double.parseDouble(txtsizelen.getText().toString());
                double num2 = Double.parseDouble(txtsizebred.getText().toString());
                double num3 = Double.parseDouble(txtthickness.getText().toString());
                double mul = (num1 * num2 * num3) / 144;
                //double ans = mul / 144;
                // set it ot result textview
                txtsqft.setText(Double.toString(mul));
            }
        });


        //run the below method on image view
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetContent.launch("image/*");
                //the image is selected successsfully
                //now we need to upload the image in firebase storage

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //upload image on button click
                uploadImage();
                insertStockData();
            }
        });


    }

    private void Calculate() {
//        ProgressDialog dialog = new ProgressDialog(this);
//        dialog.setMessage("Calculating...");
//        dialog.show();
//        double num1 = Double.parseDouble(txtsizelen.getText().toString());
//        double num2 = Double.parseDouble(txtsizebred.getText().toString());
//        double num3 = Double.parseDouble(txtthickness.getText().toString());
//        double mul = (num1/2.54) * (num2/2.54) * (num3/2.54);
//        double ans = mul / 144;
//        // set it ot result textview
//        txtsqft.setText(Double.toString(ans));
    }

    private void insertStockData() {
        String name = txtname.getText().toString();
        String lotno = txtlotno.getText().toString();
        String thickness = txtthickness.getText().toString();
        String pieces = txtpieces.getText().toString();
        String sizelen = txtsizelen.getText().toString();
        String sizebred = txtsizebred.getText().toString();
        String remark = txtremark.getText().toString();
        String sqft = txtsqft.getText().toString();

        Stock stock = new Stock(name, lotno,thickness, pieces,sizelen, sizebred, remark, sqft);
        myRef.push().setValue(stock);
        Toast.makeText(MainActivity.this, "Data Inserted", Toast.LENGTH_SHORT).show();
    }

    private void uploadImage() {
        //let's create a progess dialogue to make it better
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Uploading...");
        dialog.show();

        //here we need to access the below result
        if(imageUri != null){
            String name = txtname.getText().toString();
            String lotno = txtlotno.getText().toString();
            StorageReference reference = storage.getReference().child("images/"+ name +"-"+ lotno +" - " +UUID.randomUUID().toString());
            //we are creating a reference to store the image in firebase storage

            reference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()){
                        //Image uploaded successfully
                        dialog.dismiss();
                        Toast.makeText(MainActivity.this, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                        txtname.setText("");
                        txtlotno.setText("");
                        txtthickness.setText("");
                        txtpieces.setText("");
                        txtremark.setText("");
                        txtsizebred.setText("");
                        txtsizelen.setText("");
                        txtsqft.setText("");
                    }else{
                        //Failed
                        dialog.dismiss();
                        Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

    //start an activity for new method

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
    new ActivityResultCallback<Uri>(){
        @Override
        public void onActivityResult(Uri result) {
            if(result != null){
                imageView.setImageURI(result);
                imageUri = result;
            }
        }

    });
}
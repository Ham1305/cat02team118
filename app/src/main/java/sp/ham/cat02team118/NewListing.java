package sp.ham.cat02team118;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class NewListing extends AppCompatActivity {

    public static final String TAG = "TAG";
    private EditText itemname,itemdescription,itemprice;
    private ImageView photo2;
    private Button submit;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private FirebaseStorage storage;
    StorageReference storageRef;
    private String UserID;
    String usershop,mfstorephoto;
    Uri itemUri;

    private static final int GALLERY_REQUEST = 1;
    private static final int CAMERA_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_listing);

        itemname = findViewById(R.id.listing);
        itemprice = findViewById(R.id.listing_price);
        itemdescription = findViewById(R.id.listing_description);
        submit = findViewById(R.id.listing_submit);
        photo2 = findViewById(R.id.imageView6);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        UserID = auth.getCurrentUser().getUid();

        DoesUserHaveShop(UserID);
        Log.d(TAG, "onCreate: the shop this user owns is " + usershop);

        photo2.setOnClickListener(onPicture);
        submit.setOnClickListener(onSubmit);


    }

    private void DoesUserHaveShop(String uid) {
        CollectionReference cR = firestore.collection("users");
        cR.document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().contains("shop")){
                        Log.d(TAG, "onComplete: document contains shop, proceed to new listing page");
                        cR.document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                usershop = documentSnapshot.getString("shop");
                            }
                        });

                    } else{
                        new AlertDialog.Builder(NewListing.this)
                                .setTitle("Hold on!")
                                .setMessage("Your shop had not been set up yet, set up in order to upload listings")
                                .setPositiveButton("Set up", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(NewListing.this, NewShop.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                });
                    }
                }
            }
        });
    }

    View.OnClickListener onPicture = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final CharSequence[] options = {"Open Camera","Select from Gallery","Cancel"};
            new android.app.AlertDialog.Builder(NewListing.this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Pick Shop Picture")
                    .setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (options[which].equals("Open Camera")){
                                Intent CameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                if(CameraIntent.resolveActivity(getPackageManager()) != null){
                                    startActivityForResult(CameraIntent, CAMERA_REQUEST);
                                }
                            }
                            else if (options[which].equals("Select from Gallery")){
                                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                                photoPickerIntent.setType("image/*");
                                startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
                            }
                            else if(options[which].equals("Cancel")){
                                dialog.dismiss();
                            }
                        }
                    })
                    .show();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST){
            if (resultCode == Activity.RESULT_OK){
                itemUri = data.getData();
                try {
                    InputStream imageStream = getContentResolver().openInputStream(itemUri);
                    Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    photo2.setImageBitmap(selectedImage);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        if (requestCode == CAMERA_REQUEST){
            if (resultCode == Activity.RESULT_OK){
                itemUri = data.getData();
                try {
                    InputStream imageStream = getContentResolver().openInputStream(itemUri);
                    Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    photo2.setImageBitmap(selectedImage);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    View.OnClickListener onSubmit = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (itemUri != null){
                String itemnameStr = itemname.getText().toString();

                StorageReference storageReference = storage.getReference("listings").child(usershop).child(itemnameStr + "." + getFileExtension(itemUri));

                storageReference.putFile(itemUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        mfstorephoto = uri.toString();
                                        String itemnameStr = itemname.getText().toString();
                                        String itemdescStr = itemdescription.getText().toString();
                                        String itempriceStr = itemprice.getText().toString();

                                        DocumentReference documentReference = firestore.collection("shops").document(usershop)
                                                .collection("listings").document(itemnameStr);
                                        Map<String,Object> item = new HashMap<>();
                                        item.put("name",itemnameStr);
                                        item.put("description",itemdescStr);
                                        item.put("price",itempriceStr);
                                        item.put("photo",mfstorephoto);
                                        documentReference.set(item).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Log.d(TAG, "onSuccess: "+ itemnameStr + " added to " + usershop);
                                                Toast.makeText(NewListing.this, "Item added successfully to shop!", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(NewListing.this, "something went wrong...", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(NewListing.this, e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(NewListing.this, "Please upload photo",Toast.LENGTH_SHORT).show();
            }
        }
    };
}
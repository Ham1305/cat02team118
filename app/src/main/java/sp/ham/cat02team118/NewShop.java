package sp.ham.cat02team118;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
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


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewShop extends AppCompatActivity implements CategoryDialog.CatDialogListener {

    public static final String TAG = "TAG";
    private EditText shopname,opening_hours,description,shopphoneno,shopaddress,shopunitno;
    private TextView DisplayCat;
    private ImageView photo;
    private Button Category,submit;
    private double shopLat=0, shopLon=0;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private FirebaseStorage storage;
    StorageReference storageRef;
    private String country = "SG";
    private String UserID,shopCate,fstorephoto;
    Uri shopUri;

    private static final int GALLERY_REQUEST = 1;
    private static final int CAMERA_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_shop);

        shopname = findViewById(R.id.shopname);
        opening_hours = findViewById(R.id.openinghours);
        description = findViewById(R.id.description);
        shopphoneno = findViewById(R.id.shopphone);
        shopaddress = findViewById(R.id.shopaddress);
        shopunitno = findViewById(R.id.shopunitno);
        Category = findViewById(R.id.category);
        submit = findViewById(R.id.shopconfirm);
        DisplayCat = findViewById(R.id.textView15);

        photo = findViewById(R.id.imageView5);//wip uploader and cropper

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference("shops");

        Places.initialize(getApplicationContext(),"AIzaSyATnQKwhcuyoLSZpDTIMnESETadwd0pyYU");

        shopaddress.setFocusable(false);
        shopaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //initialize place filed list
                List<Place.Field> fields = Arrays.asList(Place.Field.ADDRESS
                        , Place.Field.LAT_LNG);
                //Create intent
                Intent intent = new Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.OVERLAY, fields
                )
                        .setCountry(country)
                        .build(NewShop.this);
                //Start Activity result
                startActivityForResult(intent, 100);
            }
        });

        Category.setOnClickListener(onCategory);
        submit.setOnClickListener(onSubmit);
        photo.setOnClickListener(onPhoto);
    }

    View.OnClickListener onCategory = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            openDialog();
        }
    };

    View.OnClickListener onPhoto = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final CharSequence[] options = {"Open Camera","Select from Gallery","Cancel"};
            new AlertDialog.Builder(NewShop.this)
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

    View.OnClickListener onSubmit = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String shopnameStr = shopname.getText().toString();

            uploadFile(shopnameStr);



        }
    };

    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile(String shop){
        if (shopUri != null){
            StorageReference fileReference = storageRef.child(shop + "." + getFileExtension(shopUri));

            fileReference.putFile(shopUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    fstorephoto = uri.toString();
                                    String hourStr = opening_hours.getText().toString();
                                    String descStr = description.getText().toString();
                                    String shopphoneStr = shopphoneno.getText().toString();
                                    String shopaddStr = shopaddress.getText().toString();
                                    String shopunitStr = shopunitno.getText().toString();
                                    String shopcatStr = shopCate;

                                    String ShopLatStr = String.valueOf(shopLat);
                                    String ShopLonStr = String.valueOf(shopLon);
                                    UserID = auth.getCurrentUser().getUid();

                                    DocumentReference documentReference = firestore.collection("shops").document(shop);
                                    Map<String,Object> user = new HashMap<>();
                                    user.put("name",shop);
                                    user.put("photo",fstorephoto);
                                    user.put("category",shopcatStr);
                                    user.put("open hours",hourStr);
                                    user.put("description",descStr);
                                    user.put("phoneno",shopphoneStr);
                                    user.put("address",shopaddStr);
                                    user.put("unitno",shopunitStr);
                                    user.put("shopLat",ShopLatStr);
                                    user.put("shopLon",ShopLonStr);
                                    user.put("created by",UserID);
                                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.d(TAG, "onSuccess: shop profile for"+ shop +"created");
                                            Toast.makeText(NewShop.this, "Shop page created successfully!", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(NewShop.this, "something went wrong...", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(NewShop.this, e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    })/*.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull @org.jetbrains.annotations.NotNull UploadTask.TaskSnapshot snapshot) {
                    double progress = (100.0 * snapshot.getBytesTransferred()/snapshot.getTotalByteCount());

                }
            })*/;
        } else {
            Toast.makeText(this, "Please upload photo",Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Check condition
        if (requestCode == 100 && resultCode == RESULT_OK) {
            //When Success
            //INitialise place
            Place place = Autocomplete.getPlaceFromIntent(data);
            //check condition
            //Set adress on edittext
            shopaddress.setText(place.getAddress());
            //Get latitude and longitude
            String sSource = String.valueOf(place.getLatLng());
            sSource = sSource.replaceAll("lat/lng: ", "");
            sSource = sSource.replace("(", "");
            sSource = sSource.replace(")", "");
            String[] split = sSource.split(",");
            shopLat = Double.parseDouble(split[0]);
            shopLon = Double.parseDouble(split[1]);
            String uLatStr = String.valueOf(shopLat);
            String uLonStr = String.valueOf(shopLon);

            Toast.makeText(getApplicationContext(), uLatStr + "  ,  " + uLonStr,Toast.LENGTH_SHORT).show();


        } else if (requestCode == AutocompleteActivity.RESULT_ERROR) {
            Status status = Autocomplete.getStatusFromIntent(data);

            Toast.makeText(getApplicationContext(), status.getStatusMessage()
                    , Toast.LENGTH_SHORT).show();
        }

        if (requestCode == GALLERY_REQUEST){
            if (resultCode == Activity.RESULT_OK){
                shopUri = data.getData();
                try {
                    InputStream imageStream = getContentResolver().openInputStream(shopUri);
                    Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    photo.setImageBitmap(selectedImage);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        if (requestCode == CAMERA_REQUEST){
            if (resultCode == Activity.RESULT_OK){
                shopUri = data.getData();
                try {
                    InputStream imageStream = getContentResolver().openInputStream(shopUri);
                    Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    photo.setImageBitmap(selectedImage);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }


    }


    public void openDialog(){
        CategoryDialog categoryDialog = new CategoryDialog();
        categoryDialog.show(getSupportFragmentManager(),"cat dialog");
    }

    @Override
    public void applyTexts(String shopcat) {
        DisplayCat.setText("Category: " + shopcat);
        shopCate = shopcat;
    }
}
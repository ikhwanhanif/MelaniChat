package ikhwan.hanif.melanichat.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yalantis.ucrop.UCrop;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

import ikhwan.hanif.melanichat.R;
import ikhwan.hanif.melanichat.utilities.Constants;
import ikhwan.hanif.melanichat.utilities.PreferencesManager;

public class SignUpActivity extends AppCompatActivity {

    TextView SignInText, addImageText;
    Button registBtn;
    private String encodedImage;
    private PreferencesManager preferencesManager;
    ProgressBar progressBar;
    FrameLayout layoutImage;
    RoundedImageView imageProfile;
    TextInputEditText name, email, password, confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize PreferencesManager for managing preferences
        preferencesManager = new PreferencesManager(getApplicationContext());

        progressBar = findViewById(R.id.progressBar);
        layoutImage = findViewById(R.id.frameLayout1);
        layoutImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(intent);
        });

        addImageText = findViewById(R.id.addImage);
        imageProfile = findViewById(R.id.imageProfile);
        name = findViewById(R.id.namaET);
        email = findViewById(R.id.emailET);
        password = findViewById(R.id.passET);
        confirmPassword = findViewById(R.id.confirmPassET);

        RelativeLayout layout = findViewById(R.id.signup);
        AnimationDrawable animationDrawable = (AnimationDrawable) layout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(2000);
        animationDrawable.start();

        SignInText = findViewById(R.id.textSignIn);
        SignInText.setOnClickListener(v -> {
            startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
            finish();
        });

        registBtn = findViewById(R.id.registBtn);
        registBtn.setOnClickListener(v -> {
            if (isValidSignUpDetails()){
                signUp();
            }
        });
    }

    // Method to display toast messages
    private void showToast(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }

    // Method to sign up a new user
    private void signUp() {
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String, Object> user = new HashMap<>();
        user.put(Constants.KEY_NAME, name.getText().toString());
        user.put(Constants.KEY_EMAIL, email.getText().toString());
        user.put(Constants.KEY_PASSWORD,password.getText().toString());
        user.put(Constants.KEY_IMAGE, encodedImage);
        database.collection(Constants.KEY_COLLECTION_USERS)
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    loading(false);
                    preferencesManager.putBoolean(Constants.KEY_IS_SIGNED_IN,true);
                    preferencesManager.putString(Constants.KEY_USER_ID, documentReference.getId());
                    preferencesManager.putString(Constants.KEY_NAME, name.getText().toString());
                    preferencesManager.putString(Constants.KEY_IMAGE, encodedImage);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .addOnFailureListener(exception -> {
                    loading(false);
                    showToast(exception.getMessage());
                });
    }

    // Method to encode the image to Base64
    private String encodeImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    // Activity Result Launcher for cropping image
    private final ActivityResultLauncher<Intent> cropImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    final Uri resultUri = UCrop.getOutput(result.getData());
                    if (resultUri != null) {
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                            imageProfile.setImageBitmap(bitmap);
                            addImageText.setVisibility(View.GONE);
                            encodedImage = encodeImage(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        showToast(getString(R.string.crop_gagal));
                    }
                }
            }
    );

    // Activity Result Launcher for picking image from gallery
    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    if (imageUri != null) {
                        startCrop(imageUri);
                    }
                }
            }
    );

    // Method to start image cropping activity
    private void startCrop(@NonNull Uri uri) {
        String destinationFileName = "SampleCropImage.jpg";
        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), destinationFileName)));

        uCrop.withAspectRatio(1, 1);
        uCrop.withMaxResultSize(450, 450);

        UCrop.Options options = new UCrop.Options();
        options.setCompressionQuality(70);
        options.setCircleDimmedLayer(true);
        options.setShowCropFrame(true);
        options.setShowCropGrid(true);
        options.setHideBottomControls(true);

        uCrop.withOptions(options);
        cropImage.launch(uCrop.getIntent(this));
    }

    // Method to validate sign up details
    private boolean isValidSignUpDetails() {
        if (encodedImage == null) {
            showToast(getString(R.string.select_profile_image));
            return false;
        } else if (name.getText().toString().trim().isEmpty()) {
            showToast(getString(R.string.enter_name));
            return false;
        } else if (email.getText().toString().trim().isEmpty()) {
            showToast(getString(R.string.enter_email));
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            showToast(getString(R.string.enter_valid_email));
            return false;
        } else if (password.getText().toString().trim().isEmpty()) {
            showToast(getString(R.string.enter_password));
            return false;
        } else if (confirmPassword.getText().toString().trim().isEmpty()) {
            showToast(getString(R.string.confirm_password));
            return false;
        } else if (!password.getText().toString().equals(confirmPassword.getText().toString())) {
            showToast(getString(R.string.password_and_confirm_password_must_be_same));
            return false;
        } else {
            return true;
        }
    }

    // Method to handle loading state
    private void loading(Boolean isLoading) {
        if (isLoading) {
            registBtn.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            registBtn.setVisibility(View.VISIBLE);
        }
    }
}

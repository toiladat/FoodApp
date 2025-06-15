package com.example.foodapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foodapp.Model.UserModel;
import com.example.foodapp.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AdditionalUserInfo;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "LoginActivity";

    // Khai báo các biến UI
    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private ImageButton googleButton;
    private TextView signUpTextView;

    // Khai báo các biến Firebase và Google
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Khởi tạo Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // **CẢI TIẾN QUAN TRỌNG NHẤT**
        // Kiểm tra ngay khi mở Activity xem người dùng đã đăng nhập chưa
        if (mAuth.getCurrentUser() != null) {
            Log.d(TAG, "User is already logged in. Navigating to Userpage.");
            goToUserPage();
            // return để không chạy code phía dưới nữa, tránh việc hiển thị layout không cần thiết
            return;
        }

        // Nếu người dùng chưa đăng nhập, mới hiển thị layout
        setContentView(R.layout.activity_login);

        // Cấu hình Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Ánh xạ View và thiết lập Listener
        initViews();
        setupListeners();
    }

    private void initViews() {
        emailEditText = findViewById(R.id.editTextEmail);
        passwordEditText = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.buttonLogin);
        googleButton = findViewById(R.id.buttonGoogle);
        signUpTextView = findViewById(R.id.textViewSignUp);
    }

    private void setupListeners() {
        loginButton.setOnClickListener(v -> handleManualLogin());
        googleButton.setOnClickListener(v -> initiateGoogleSignIn());
        signUpTextView.setOnClickListener(v -> startActivity(new Intent(this, SingupActivity.class)));
    }

    private void handleManualLogin() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Vui lòng nhập email và mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithEmail:success");
                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                        goToUserPage(); // Chuyển trang tường minh
                    } else {
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(LoginActivity.this, "Sai email hoặc mật khẩu.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void initiateGoogleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Log.w(TAG, "Google sign in failed", e);
                Toast.makeText(this, "Đăng nhập Google thất bại. Mã lỗi: " + e.getStatusCode(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Hàm chính được gọi sau khi lấy được idToken từ Google
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithCredential:success");
                        AuthResult authResult = task.getResult();
                        if (authResult == null) return;

                        AdditionalUserInfo additionalUserInfo = authResult.getAdditionalUserInfo();
                        FirebaseUser firebaseUser = authResult.getUser();
                        if (additionalUserInfo == null || firebaseUser == null) {
                            Toast.makeText(this, "Lỗi xác thực, không lấy được thông tin.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (additionalUserInfo.isNewUser()) {
                            Log.d(TAG, "New Google user. Creating profile.");
                            saveNewGoogleUserAndGoToUserPage(firebaseUser);
                        } else {
                            Log.d(TAG, "Existing user in Auth. Checking Realtime Database profile.");
                            checkAndCreateProfileIfNeeded(firebaseUser);
                        }
                    } else {
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        Toast.makeText(LoginActivity.this, "Xác thực với Firebase thất bại.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Hàm mới để kiểm tra và tạo hồ sơ nếu cần
    private void checkAndCreateProfileIfNeeded(FirebaseUser firebaseUser) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot snapshot = task.getResult();
                if (snapshot.exists()) {
                    Log.d(TAG, "Profile already exists in DB. Navigating to Userpage.");
                    Toast.makeText(this, "Chào mừng quay trở lại!", Toast.LENGTH_SHORT).show();
                    goToUserPage();
                } else {
                    Log.d(TAG, "Profile does not exist in DB. Creating a new one.");
                    saveNewGoogleUserAndGoToUserPage(firebaseUser);
                }
            } else {
                Log.e(TAG, "Failed to check for user profile.", task.getException());
                Toast.makeText(this, "Lỗi khi kiểm tra dữ liệu.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveNewGoogleUserAndGoToUserPage(FirebaseUser firebaseUser) {
        String uid = firebaseUser.getUid();
        String name = firebaseUser.getDisplayName();
        String email = firebaseUser.getEmail();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        UserModel newUser = new UserModel(name, email, "", "");

        databaseReference.child(uid).setValue(newUser)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Successfully saved new Google user profile.");
                        Toast.makeText(LoginActivity.this, "Tạo tài khoản thành công!", Toast.LENGTH_SHORT).show();
                        goToUserPage();
                    } else {
                        Log.e(TAG, "Failed to save Google user profile.", task.getException());
                        Toast.makeText(LoginActivity.this, "Lỗi khi lưu hồ sơ. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void goToUserPage() {
        Intent intent = new Intent(LoginActivity.this, UserpageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
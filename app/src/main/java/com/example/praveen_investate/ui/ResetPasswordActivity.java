package com.example.praveen_investate.ui;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.praveen_investate.R;
import com.example.praveen_investate.encryption_decryption.PsychoCipher;

public class ResetPasswordActivity extends AppCompatActivity {
    private EditText newPasswordEditText;
    private Button resetPasswordButton;
    private TextView tv ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        newPasswordEditText = findViewById(R.id.newPasswordEditText);
        resetPasswordButton = findViewById(R.id.resetPasswordButton);
        tv = findViewById(R.id.text);
        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPassword = new PsychoCipher(newPasswordEditText.getText().toString()).getEncryptedString();
                String username = tv.getText().toString(); // Assuming you pass the username from the previous activity
                resetPassword(username, newPassword);
            }
        });
    }

    private void resetPassword(String username, String newPassword) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "http://10.0.2.2:1010/api/auth/profile/" + username + "/" + newPassword;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null,
                response -> {
                    Toast.makeText(ResetPasswordActivity.this, "Password reset successfully.", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(() -> {

                        finish();
                    }, 2000);
                     // Close the activity after successful reset

                },
                error -> {
                    String errorMessage = "";

                    if (error.networkResponse != null && error.networkResponse.data != null) {
                        errorMessage += " " + new String(error.networkResponse.data);
                    }
                    Log.e("",errorMessage);
                    Toast.makeText(ResetPasswordActivity.this, errorMessage, Toast.LENGTH_SHORT).show();

        });

        requestQueue.add(jsonObjectRequest);
    }
}

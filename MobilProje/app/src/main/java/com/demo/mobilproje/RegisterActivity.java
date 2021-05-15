package com.demo.mobilproje;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    //region --> Widgets
        private EditText userET,passET,emailET;
        Button registerBtn;
    //endregion

    //region --> Firebase
        FirebaseAuth mAuth;
        DatabaseReference myRef;

    //endregion
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //region --> Initialize Widgets
            userET = findViewById(R.id.editTextUserName);
            passET = findViewById(R.id.editTextTextPassword);
            emailET = findViewById(R.id.editTextEmail);
            registerBtn = findViewById(R.id.regBtn);
        //endregion

        mAuth = FirebaseAuth.getInstance();

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName_Text = userET.getText().toString();
                String email_Text = emailET.getText().toString();
                String password_Text = passET.getText().toString();

                if (TextUtils.isEmpty(userName_Text) || TextUtils.isEmpty(email_Text) || TextUtils.isEmpty(password_Text)){
                    Toast.makeText(RegisterActivity.this, "Please fill all fields.", Toast.LENGTH_SHORT).show();
                }
                else{
                    registerNow(userName_Text,email_Text,password_Text);
                }
            }
        });
    }

    private void registerNow(final String username,String email,String password ){

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    String userId = firebaseUser.getUid();

                    myRef = FirebaseDatabase.getInstance()
                            .getReference("Users")
                            .child(userId);

                    //region --> Hashmap
                    HashMap<String,String> hashMap = new HashMap<>();
                    hashMap.put("id",userId);
                    hashMap.put("username",username);
                    hashMap.put("imageURL","default");
                    //endregion

                    // if success open MainActivity

                    myRef.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){
                                Intent i = new Intent(RegisterActivity.this,MainActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                                finish();
                            }

                        }
                    });
                }
                else{
                    Toast.makeText(RegisterActivity.this, "Invalid Email or Password.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
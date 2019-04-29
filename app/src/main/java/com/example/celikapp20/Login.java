package com.example.celikapp20;

import android.animation.TimeAnimator;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity implements GestureDetector.OnGestureListener {
    private FirebaseAuth mAuth;
    private EditText mail;
    private EditText psw;
    private Button btnlogin;
    private Button btnlogout;
    private Button btnIscr;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth=FirebaseAuth.getInstance();

        mail=(EditText) findViewById(R.id.mail);
        psw=(EditText) findViewById(R.id.psw);
        btnlogin=(Button) findViewById(R.id.login);
        btnlogout=(Button) findViewById(R.id.logout);
        btnIscr=(Button) findViewById(R.id.iscriviti);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email=mail.getText().toString();
                final String password=psw.getText().toString();
                user=mAuth.getCurrentUser();
                if(!email.isEmpty() && !password.isEmpty()) {
                    if (user == null) {
                        mAuth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            Toast.makeText(Login.this, "Login effettuato correttamente", Toast.LENGTH_SHORT).show();
                                            user = mAuth.getCurrentUser();

                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Log.w("TAG", "signInWithEmail:failure", task.getException());
                                            Toast.makeText(Login.this, "Authentication failed.",
                                                    Toast.LENGTH_SHORT).show();

                                        }

                                        // ...
                                    }
                                });
                    } else {
                        Toast.makeText(Login.this, "Sei già loggato con " + user.getEmail(), Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(Login.this, "Inserire email e password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(Login.this, "Logout effettuato correttamente", Toast.LENGTH_SHORT).show();
            }
        });

        btnIscr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email=mail.getText().toString();
                final String password=psw.getText().toString();
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Log.d("TAG", "onComplete: create user succesfully");
                                    Toast.makeText(Login.this, "Utente creato correttamente", Toast.LENGTH_SHORT).show();
                                    user=mAuth.getCurrentUser();
                                }else{
                                    Toast.makeText(Login.this, "Impossibile creare un nuovo utente", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if(e1.getX()<e2.getX()){
            NavUtils.navigateUpFromSameTask(this);
        }
        return true;
    }
}

package com.example.signinsignup;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.signinsignup.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    EditText newusername,newpassword,newemail;
    EditText username,password;
    Button btnsignup, btnsignin;

    FirebaseDatabase database;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Firebase
        database = FirebaseDatabase.getInstance();
        users = database.getReference("Users");

        username = (EditText) findViewById(R.id.usernameid);
        password = (EditText) findViewById(R.id.passwordid);
        newusername = (EditText) findViewById(R.id.newusernameid);
        newpassword = (EditText) findViewById(R.id.newpasswordid);
        newemail = (EditText) findViewById(R.id.newemailid);

        btnsignup = (Button) findViewById(R.id.signupid);
        btnsignin = (Button) findViewById(R.id.signinid);

        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignUpDialog();
            }


        });

        btnsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(username.getText().toString(), password.getText().toString());
            }

        });

    }

    private void signIn(final String user, final String pwd) {

        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(user).exists())
                {
                    if (!user.isEmpty())
                    {
                        User login = dataSnapshot.child(user).getValue(User.class);
                        if (login.getPassword().equals(pwd))
                            Toast.makeText(MainActivity.this, "Login Ok", Toast.LENGTH_SHORT).show();
//                        {
//                            Intent homeActivity = new Intent(MainActivity.this, Home.class);
//                            startActivity(homeActivity);
//                            finish();
//                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                        }

                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "Please enter your username", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(MainActivity.this, "User is nor exit", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    private void showSignUpDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Sign Up");
        alertDialog.setMessage("Pease fill up Information");

        LayoutInflater inflater = this.getLayoutInflater();
        View sign_up_layout = inflater.inflate(R.layout.sign_up_layout,null);

        newusername =(EditText)sign_up_layout.findViewById(R.id.newusernameid);
        newpassword =(EditText)sign_up_layout.findViewById(R.id.newpasswordid);
        newemail =(EditText)sign_up_layout.findViewById(R.id.newemailid);

        alertDialog.setView(sign_up_layout);
        alertDialog.setIcon(R.drawable.ic_account_circle_black_24dp);

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                 final User user = new User(newusername.getText().toString(),
                        newpassword.getText().toString(),
                        newemail.getText().toString());


               users.addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                       if (dataSnapshot.child(user.getUserName()).exists())
                           Toast.makeText(MainActivity.this, "User Already Exit", Toast.LENGTH_SHORT).show();
                       else
                       {
                           users.child(user.getUserName())
                                   .setValue(user);

                           Toast.makeText(MainActivity.this, "User registration success", Toast.LENGTH_SHORT).show();
                       }
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError databaseError) {

                   }
               });

               dialog.dismiss();


            }
    });
        alertDialog.show();



    }
}

package com.example.saldoseguro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //button create account
    public void create_account(View view){
        Intent create_account_intent = new Intent(this, create_account.class);
        startActivity(create_account_intent);
    }

    //metod to go to the main screen
    public  void  mainScreen(View view){
        Intent createMainScreen = new Intent(this, pantalla_principal.class);
        startActivity(createMainScreen);
    }
}
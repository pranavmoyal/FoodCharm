package com.pranav.foodcharm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.pranav.foodcharm.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button bt_signin,bt_signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inIt();
        setListener();
    }


    private void inIt(){
        bt_signin=(Button) findViewById(R.id.bt_signin);
        bt_signup=(Button) findViewById(R.id.bt_signup);
    }

    private void setListener(){
        bt_signin.setOnClickListener(this);
        bt_signup.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_signin:

                Intent bt_signin=new Intent(MainActivity.this, LoginActivity.class);
                startActivity(bt_signin);

                break;

            case R.id.bt_signup:

                Intent bt_signup=new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(bt_signup);

                break;
        }
    }
}

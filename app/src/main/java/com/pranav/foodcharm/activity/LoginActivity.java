package com.pranav.foodcharm.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.pranav.foodcharm.CustomToast;
import com.pranav.foodcharm.R;
import com.pranav.foodcharm.view.CustomEditText;
import com.pranav.foodcharm.view.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    ImageView img_love;
    private Animation myAnim;
    TextView tv_forgot_password,tv_lets_start;
    CustomEditText et_food_id,et_password;

    private static final String PREFER_NAME = "Reg";
    private DatabaseHelper databaseHelper;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        inIt();
        setListener();
        initObjects();
    }


    private void inIt(){
        img_love=(ImageView) findViewById(R.id.img_love);
        tv_forgot_password=(TextView) findViewById(R.id.tv_forgot_password);
        et_food_id=(CustomEditText) findViewById(R.id.et_food_id);
        et_password=(CustomEditText) findViewById(R.id.et_password);
        tv_lets_start=(TextView) findViewById(R.id.tv_lets_start);

        myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        myAnim.setInterpolator(interpolator);
        img_love.startAnimation(myAnim);


        //et_password.setTransformationMethod(new PasswordTransformationMethod());
    }

    private void setListener(){
        tv_forgot_password.setOnClickListener(this);
        tv_lets_start.setOnClickListener(this);
    }

    private void initObjects() {
        databaseHelper = new DatabaseHelper(this);
        //inputValidation = new InputValidation(activity);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_forgot_password:

                Intent tv_forgot_password=new Intent(LoginActivity.this,ResetPasswordActivity.class);
                startActivity(tv_forgot_password);

                break;

            case R.id.tv_lets_start:
                checkValidation();
                break;

        }
    }

    private void checkValidation() {
        String getEmailId = et_food_id.getText().toString();
        String getPassword = et_password.getText().toString();

        Pattern p = Pattern.compile(Utils.regEx);

        Matcher m = p.matcher(getEmailId);

        if (getEmailId.equals("") || getEmailId.length() == 0
                || getPassword.equals("") || getPassword.length() == 0) {
            new CustomToast().Show_Toast(this, et_food_id,
                    "Enter both credentials.");

        }
        else if (!m.find())
            new CustomToast().Show_Toast(this, et_food_id,
                    "Your Email Id is Invalid.");
        else
            /*Toast.makeText(getActivity(), "Do Login.", Toast.LENGTH_SHORT)
                    .show();*/

            if (databaseHelper.checkUser(getEmailId
                    , getPassword)) {

                emptyInputEditText();
                Intent intent=new Intent(LoginActivity.this,FoodDashBoardActivity.class);
                startActivity(intent);



            } else {
                new CustomToast().Show_Toast(this, et_password,
                        "Error Valid Email or Password.");
            }
    }

    private void emptyInputEditText() {
        et_food_id.setText(null);
        et_password.setText(null);
    }
}

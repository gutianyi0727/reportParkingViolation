package com.example.elvis.reportparkingviolation;
/**
 * Elvis Gu, May 2018
 * The following code contain some from https://github.com/liaopen123/LoginAnimation
 */
import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class AnimatLogin extends Activity implements OnClickListener {

    private TextView mBtnLogin;

    private View progress;

    private View mInputLayout;

    private float mWidth, mHeight;

    private LinearLayout mName, mPsw;

    private Button mLoginButton;
    private Button mToRegisterButton;
    private EditText mEditTextUsername;
    private EditText mEditTextPassword;
    private TextView textSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_animat_login);
        initView();

        mEditTextUsername = (EditText) findViewById(R.id.username_animation_login);
        mEditTextPassword = (EditText) findViewById(R.id.password_animation_login);
        textSignUp = (TextView) findViewById(R.id.sign_up_text_button);
        textSignUp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSign = new Intent(AnimatLogin.this, SignUp.class);
                startActivity(intentSign);
            }
        });
    }


    //The following code is from https://github.com/liaopen123/LoginAnimation
    private void initView() {
        mBtnLogin = (TextView) findViewById(R.id.main_btn_login);
        progress = findViewById(R.id.layout_progress);
        mInputLayout = findViewById(R.id.input_layout);
        mName = (LinearLayout) findViewById(R.id.input_layout_name);
        mPsw = (LinearLayout) findViewById(R.id.input_layout_psw);

        mBtnLogin.setOnClickListener(this);
    }
   //End of the code
    @Override
    public void onClick(View v) {

        mWidth = mBtnLogin.getMeasuredWidth();
        mHeight = mBtnLogin.getMeasuredHeight();

        mName.setVisibility(View.INVISIBLE);
        mPsw.setVisibility(View.INVISIBLE);

        inputAnimator(mInputLayout, mWidth, mHeight);

        //check users' login informatiton
        appUser userlogin = new appUser();
        userlogin.setUsername(mEditTextUsername.getText().toString());
        userlogin.setPassword(mEditTextPassword.getText().toString());
        userlogin.login(new SaveListener<appUser>() {
            @Override
            public void done(appUser bmobUser, BmobException e) {
                if (e == null) {
                    if (bmobUser.getPolice()){
                        final Intent intentLoginP = new Intent(AnimatLogin.this, TransferData.class);
                        intentLoginP.putExtra("username", bmobUser.getUsername());
                        Timer timer = new Timer();
                        Toast.makeText(AnimatLogin.this, bmobUser.getUsername() + "登录成功", Toast.LENGTH_LONG).show();
                        TimerTask timerTask = new TimerTask() {
                            @Override
                            public void run() {
                                startActivity(intentLoginP);
                                finish();
                            }
                        };
                        timer.schedule(timerTask,2000);

                    }else{
                        final Intent intentLoginS = new Intent(AnimatLogin.this, userMapActivity.class);
                        intentLoginS.putExtra("useID", bmobUser.getObjectId());

                        Timer timer = new Timer();
                        Toast.makeText(AnimatLogin.this, bmobUser.getUsername() + "登录成功", Toast.LENGTH_LONG).show();
                        TimerTask timerTask = new TimerTask() {
                            @Override
                            public void run() {
                                startActivity(intentLoginS);
                                finish();
                            }
                        };
                        timer.schedule(timerTask,2000);
                    }
                }else{
                    Toast.makeText(AnimatLogin.this,"Wrong username and password or null",Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    //The following code is from https://github.com/liaopen123/LoginAnimation
    private void inputAnimator(final View view, float w, float h) {



        ValueAnimator animator = ValueAnimator.ofFloat(0, w);
        animator.addUpdateListener(new AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                MarginLayoutParams params = (MarginLayoutParams) view.getLayoutParams();
                params.leftMargin = (int) value;
                params.rightMargin = (int) value;
                view.setLayoutParams(params);
            }
        });
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mInputLayout, "scaleX", 1f, 0.5f);
        set.setDuration(1000);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.playTogether(animator, animator2);
        set.start();
        set.addListener(new AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                progress.setVisibility(View.VISIBLE);
                progressAnimator(progress);
                mInputLayout.setVisibility(View.INVISIBLE);

                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        recovery();
                    }
                }, 2000);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // TODO Auto-generated method stub

            }
        });

    }

    private void progressAnimator(final View view) {
        PropertyValuesHolder animator = PropertyValuesHolder.ofFloat("scaleX", 0.5f, 1f);
        PropertyValuesHolder animator2 = PropertyValuesHolder.ofFloat("scaleY", 0.5f, 1f);
        ObjectAnimator animator3 = ObjectAnimator.ofPropertyValuesHolder(view, animator, animator2);
        animator3.setDuration(1000);
        animator3.setInterpolator(new JellyInterpolator());
        animator3.start();

    }

    /**
     * �ָ���ʼ״̬
     */
    private void recovery() {
        progress.setVisibility(View.GONE);
        mInputLayout.setVisibility(View.VISIBLE);
        mName.setVisibility(View.VISIBLE);
        mPsw.setVisibility(View.VISIBLE);

        MarginLayoutParams params = (MarginLayoutParams) mInputLayout.getLayoutParams();
        params.leftMargin = 0;
        params.rightMargin = 0;
        mInputLayout.setLayoutParams(params);


        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mInputLayout, "scaleX", 0.5f,1f );
        animator2.setDuration(500);
        animator2.setInterpolator(new AccelerateDecelerateInterpolator());
        animator2.start();
    }
    //End of the code
}

package tech.misfit.ifarmer.view.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import tech.misfit.ifarmer.R;

public class SplashScreenActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
    }

    @Override
    protected void viewRelatedTask() {
        Intent intent =new Intent(this,LoginActivity.class);
        startActivity(intent);
    }
}

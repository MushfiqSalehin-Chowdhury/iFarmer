package tech.misfit.ifarmer.view.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import tech.misfit.ifarmer.R;
import tech.misfit.ifarmer.databinding.ActivityLoginBinding;

public class LoginActivity extends BaseActivity {

    ActivityLoginBinding loginBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginBinding= DataBindingUtil.setContentView(this,R.layout.activity_login);
    }

    @Override
    protected void viewRelatedTask() {

        setToolbar("", true, loginBinding.toolbarBinding);

        loginBinding.logIn.setOnClickListener(view -> {
            Intent intent= new Intent(this,DashboardActivity.class);
            startActivity(intent);
        });

    }
}

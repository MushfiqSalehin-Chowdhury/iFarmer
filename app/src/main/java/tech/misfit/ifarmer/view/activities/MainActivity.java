package tech.misfit.ifarmer.view.activities;


import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import tech.misfit.ifarmer.R;
import tech.misfit.ifarmer.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity {


    ActivityMainBinding mainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);
    }

    @Override
    protected void viewRelatedTask() {

    }
}

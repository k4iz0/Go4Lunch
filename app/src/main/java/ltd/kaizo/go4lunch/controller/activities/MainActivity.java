package ltd.kaizo.go4lunch.controller.activities;

import android.os.Bundle;

import ltd.kaizo.go4lunch.R;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public int getFragmentLayout() {
        return 0;
    }
}

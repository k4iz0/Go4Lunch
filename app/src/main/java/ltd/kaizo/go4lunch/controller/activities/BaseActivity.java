package ltd.kaizo.go4lunch.controller.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.ButterKnife;
import ltd.kaizo.go4lunch.R;
import ltd.kaizo.go4lunch.models.utils.DataRecordHelper;
import timber.log.Timber;

/**
 * The type Base activity.
 */
public abstract class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(this.getFragmentLayout());
        ButterKnife.bind(this);
        DataRecordHelper.init(getApplicationContext());
        Timber.plant(new Timber.DebugTree());
        this.configureDesign();
    }

    /**
     * Gets fragment layout.
     *
     * @return the fragment layout
     */
    public abstract int getFragmentLayout();

    /**
     * Configure design.
     */
    public abstract void configureDesign();
    /**
     * Configure toolbar.
     */

    /**
     * Gets current user.
     *
     * @return the current user
     */
    @Nullable
    protected FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    /**
     * Is current user logged boolean.
     *
     * @return the boolean
     */
    protected Boolean isCurrentUserLogged() {
        return (this.getCurrentUser() != null);
    }

    /**
     * On failure listener on failure listener.
     *
     * @return the on failure listener
     */
// --------------------
    //ERROR HANDLER
    // --------------------
    protected OnFailureListener onFailureListener() {
        return new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), getString(R.string.error_unknown_error) + e, Toast.LENGTH_SHORT).show();
            }
        };

    }

}

package ltd.kaizo.go4lunch.controller.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextThemeWrapper;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.squareup.haha.perflib.Main;

import butterknife.BindView;
import butterknife.OnClick;
import ltd.kaizo.go4lunch.R;
import ltd.kaizo.go4lunch.models.API.UserHelper;

public class SettingsActivity extends BaseActivity {

    private static final int DELETE_USER_TASK = 20;
    private static final int UPDATE_USERNAME = 30;
    @BindView(R.id.setting_activity_toolbar)
    Toolbar toolbar;
    private String TAG = getClass().getSimpleName();

    @Override
    public int getFragmentLayout() {
        return R.layout.activity_settings;
    }

    @Override
    public void configureDesign() {
        this.configureToolbar();
    }

    @OnClick(R.id.activity_setting_delete_account)
    public void configureOnClickDeleteButton() {

        new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom))
                .setMessage(R.string.popup_message_confirmation_delete_account)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteUserFromFirebase();
                        Log.i(TAG, "onClick: user deleted");
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();

    }


    private void deleteUserFromFirebase() {
        if (this.getCurrentUser() != null) {
            AuthUI.getInstance()
                    .delete(this)
                    .addOnSuccessListener(this, this.updateUIAfterRESTRequestsCompleted(DELETE_USER_TASK));
            UserHelper.deleteUser(this.getCurrentUser().getUid()).addOnFailureListener(this.onFailureListener()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    AuthUI.getInstance()
                            .signOut(getApplicationContext()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(mainActivity);
                                }
                            });
                }
            });

        }

    }

    private OnSuccessListener<Void> updateUIAfterRESTRequestsCompleted(final int origin) {
        return new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                switch (origin) {
                    case DELETE_USER_TASK:
                        finish();
                        startActivity(getIntent());
                        break;
                    case UPDATE_USERNAME:
//                        progressBar.setVisibility(View.INVISIBLE);
                        break;
                    default:
                        break;
                }
            }
        };
    }

    private void configureToolbar() {
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
        setSupportActionBar(toolbar);

    }
}

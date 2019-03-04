package ltd.kaizo.go4lunch.controller.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;
import ltd.kaizo.go4lunch.R;
import ltd.kaizo.go4lunch.models.API.UserHelper;
import timber.log.Timber;

import static ltd.kaizo.go4lunch.models.utils.DataRecordHelper.NOTIFICATION_ENABLE;
import static ltd.kaizo.go4lunch.models.utils.DataRecordHelper.read;
import static ltd.kaizo.go4lunch.models.utils.DataRecordHelper.write;

/**
 * The type Settings activity.
 */
public class SettingsActivity extends BaseActivity {

    /**
     * The constant VALID_EMAIL_ADDRESS_REGEX.
     */
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    /**
     * The constant DELETE_USER_TASK.
     */
    private static final int DELETE_USER_TASK = 20;
    /**
     * The Toolbar.
     */
    @BindView(R.id.setting_activity_toolbar)
    Toolbar toolbar;
    /**
     * The Notification switch.
     */
    @BindView(R.id.activity_setting_notification_switch)
    SwitchCompat notificationSwitch;
    /**
     * The Username edit text.
     */
    @BindView(R.id.activity_setting_username_edit_text)
    EditText usernameEditText;
    /**
     * The Email edit text.
     */
    @BindView(R.id.activity_setting_email_edittext)
    EditText emailEditText;

    /**
     * Validate email boolean.
     *
     * @param emailStr the email str
     * @return the boolean
     */
    public static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.activity_settings;
    }

    @Override
    public void configureDesign() {
        this.configureToolbar();
        this.configureNotificationSwitch();
    }

    /**
     * Configure on click delete button.
     */
    @OnClick(R.id.activity_setting_delete_account)
    public void configureOnClickDeleteButton() {

        new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom))
                .setMessage(R.string.popup_message_confirmation_delete_account)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteUserFromFirebase();
                        Timber.i("onClick: user deleted");
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();

    }

    /**
     * Configure on click update username button.
     */
    @OnClick(R.id.activity_setting_update_username_btn)
    public void configureOnClickUpdateUsernameButton() {
        if (isUsernameValid()) {
            UserHelper.updateUsername(usernameEditText.getText().toString(), getCurrentUser().getUid()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), getString(R.string.update_usermane_success) + " : " + usernameEditText.getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), getString(R.string.error_unknown_error), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, getString(R.string.updateUsernameError), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * configure on click update email button
     */
    @OnClick(R.id.activity_setting_update_email_btn)
    public void configureOnClickUpdateEmailButton() {
        if (isEmailValid()) {
            UserHelper.updateEmail(emailEditText.getText().toString(), getCurrentUser().getUid()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), getString(R.string.update_email_success) + " : " + emailEditText.getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), getString(R.string.error_unknown_error), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.updateEmailError), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Is email valid boolean.
     *
     * @return the boolean
     */
    private boolean isEmailValid() {
        return emailEditText.getText().length() > 0 && validateEmail(emailEditText.getText().toString());
    }

    /**
     * return true if the username is bigger than 2 characters
     * and different from previous username
     *
     * @return Boolean boolean
     */
    private Boolean isUsernameValid() {
        return usernameEditText.getText().length() > 2 && !usernameEditText.getText().equals(getCurrentUser().getDisplayName());
    }

    /**
     * Configure notification switch.
     */
    public void configureNotificationSwitch() {
        if (read(NOTIFICATION_ENABLE, true)) {
            notificationSwitch.setChecked(true);
        }
        notificationSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (read(NOTIFICATION_ENABLE, true)) {
                    notificationSwitch.setChecked(false);
                    write(NOTIFICATION_ENABLE, false);
                } else {
                    notificationSwitch.setChecked(true);
                    write(NOTIFICATION_ENABLE, true);
                }
            }
        });

    }

    /**
     * Delete user from firebase.
     */
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

    /**
     * Update ui after rest requests completed on success listener.
     *
     * @param origin the origin
     * @return the on success listener
     */
    private OnSuccessListener<Void> updateUIAfterRESTRequestsCompleted(final int origin) {
        return new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                switch (origin) {
                    case DELETE_USER_TASK:
                        finish();
                        startActivity(getIntent());
                        break;
                    default:
                        break;
                }
            }
        };
    }

    /**
     * Configure toolbar.
     */
    private void configureToolbar() {
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

    }
}

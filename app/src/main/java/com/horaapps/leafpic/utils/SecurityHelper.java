package com.horaapps.leafpic.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.horaapps.leafpic.R;
import com.horaapps.leafpic.Views.ThemedActivity;

/**
 * Created by Jibo on 06/05/2016.
 */
public class SecurityHelper {
    SharedPreferences SP;
    boolean activeSecurity;
    boolean passwordOnDelete;
    boolean passwordOnHidden;
    String passwordValue;

    Context context;
    public SecurityHelper(Context c){
        this.context = c;
        updateSecuritySetting();
    }

    public boolean isActiveSecurity(){return activeSecurity;}
    public boolean isPasswordOnHidden(){return passwordOnHidden;}
    public boolean isPasswordOnDelete(){return passwordOnDelete;}

    public boolean checkPassword(String pass){
        return (isActiveSecurity() && pass.equals(passwordValue));
    }

    public void updateSecuritySetting(){

        this.SP = PreferenceManager.getDefaultSharedPreferences(context);
        this.activeSecurity = SP.getBoolean("active_security", false);
        this.passwordOnDelete = SP.getBoolean("password_on_delete", false);
        this.passwordOnHidden = SP.getBoolean("password_on_hidden", true);
        this.passwordValue = SP.getString("password_value", "");
    }

    public AlertDialog getInsertPasswordDialog(final ThemedActivity activity, AlertDialog
            .Builder dialogBuilder, EditText editText){
        editText.setGravity(Gravity.CENTER_HORIZONTAL);
        editText.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType
                .TYPE_NUMBER_VARIATION_PASSWORD);
        editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        return AlertDialogsHelper.getInsertTextDialog(activity,dialogBuilder,
                editText, activity.getString(R.string.type_password));
    }
}

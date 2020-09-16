package com.example.bloodbank.fireStoreDataBase.posts;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

/**
 * Created by Mohamed Nabil Mohamed on 9/13/2019.
 * m.nabil.fci2015@gmail.com
 */
public class BaseActivityA extends AppCompatActivity {

    public AlertDialog showMessage(String message, String posActionName , DialogInterface.OnClickListener onClickListener, Context context){
        AlertDialog.Builder builder =new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setPositiveButton(posActionName, (dialog, which) -> dialog.dismiss());
        return builder.show();
    }


    public AlertDialog showMessage(String message, String posActionName , Context context){
        AlertDialog.Builder builder =new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setPositiveButton(posActionName, (dialog, which) -> dialog.dismiss());
        return builder.show();
    }
    public AlertDialog showMessage(int message, int posActionName){
        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setPositiveButton(posActionName, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return builder.show();
    }

    public AlertDialog showMessage(String message, String posActionName,
                                   DialogInterface.OnClickListener onClickListener,
                                   boolean isCancelable){
        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setPositiveButton(posActionName,onClickListener );
        builder.setCancelable(isCancelable);
        return builder.show();
    }
    public AlertDialog showMessage(String message, String posActionName,
                                   DialogInterface.OnClickListener onPosClick,
                                   String negativeText,
                                   DialogInterface.OnClickListener onNegativeClick,
                                   boolean isCancelable){
        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setPositiveButton(posActionName,onPosClick );
        builder.setNegativeButton(negativeText,onNegativeClick );
        builder.setCancelable(isCancelable);
        return builder.show();
    }
public AlertDialog showMessage(int message, int posActionName,
                               DialogInterface.OnClickListener onClickListener,
                               boolean isCancelable
){
        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setPositiveButton(posActionName,onClickListener);
        builder.setCancelable(isCancelable);
        return builder.show();
    }
    public AlertDialog showMessage(int message, int posActionName,
                                   DialogInterface.OnClickListener onPosClick,
                                   int negativeText,
                                   DialogInterface.OnClickListener onNegativeClick,
                                   boolean isCancelable
    ){
        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setPositiveButton(posActionName,onPosClick );
        builder.setNegativeButton(negativeText,onNegativeClick );
        builder.setCancelable(isCancelable);
        return builder.show();
    }
    ProgressDialog dialog;
    public void showProgressDialog(String message , Context context ){
         dialog =new ProgressDialog(context);
        dialog.setMessage(message);
        dialog.setCancelable(false);
        dialog.show();
    }
    public void hideProgressDialog(){
        if(dialog!=null&&dialog.isShowing())
            dialog.dismiss();
    }
}

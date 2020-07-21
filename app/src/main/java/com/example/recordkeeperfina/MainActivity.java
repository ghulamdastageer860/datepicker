package com.example.recordkeeperfina;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.recordkeeperfina.activities.PersonListActivity;
import com.example.recordkeeperfina.database.MyDbHandler;
import com.example.recordkeeperfina.model.PersonsBean;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity
{

    private static final String TAG = "myLogCheck";
    MyDbHandler db = new MyDbHandler(this);
    FloatingActionButton floatingActionButtonAdd;
    private Context context =MainActivity.this;
    /// popup Layout items below;
    private EditText editTextName, editTextNumber;
    private Button buttonSave;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private int selectedIndex = -1;
    private int personId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setElevation(0);
            byPassActivity();

        getSupportActionBar().setTitle("Record Keeper");
        floatingActionButtonAdd = findViewById(R.id.floatingActionButtonMainActivity);

        floatingActionButtonAdd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                builder = new AlertDialog.Builder(MainActivity.this);
                View view = getLayoutInflater().inflate(R.layout.popup_add_person, null);
                editTextName = view.findViewById(R.id.editTextPopupAddPersonName);
                editTextNumber = view.findViewById(R.id.editTextPopupAddPersonNumber);
                buttonSave = view.findViewById(R.id.buttonPopuAddPersonSave);

                builder.setView(view);
                dialog = builder.create();
                dialog.show();
                buttonSave.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        String name = editTextName.getText().toString();
                        String number = editTextNumber.getText().toString().trim();
                        if (name.isEmpty() || number.isEmpty())
                        {
                            Toasty.warning(context, "Enter All Fields", Toasty.LENGTH_SHORT).show();
                        } // if closed
                        else
                        {
                            PersonsBean bean = new PersonsBean();
                            bean.setPersonName(name);
                            bean.setPersonNumber(number);
                            db.addPerson(bean); //
                            new Handler().postDelayed(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    startActivity(new Intent(context, PersonListActivity.class));
                                    finish();
                                }
                            }, 500);
                        }


                    } // onClick closed
                }); // buttonSave onClick Listener closed
            }

        });


    }

    private void byPassActivity()
    {
        if(db.getPersonsCount()>0)
        {
            Intent intent = new Intent(MainActivity.this, PersonListActivity.class);
            startActivity(intent);
            finish();
        }
    } //
    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Double Tap to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    } //



}
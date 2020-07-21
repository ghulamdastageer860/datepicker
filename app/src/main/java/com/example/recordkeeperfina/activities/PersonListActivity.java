package com.example.recordkeeperfina.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Person;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.recordkeeperfina.MainActivity;
import com.example.recordkeeperfina.R;
import com.example.recordkeeperfina.adapters.CustomRecyclerAdapterForPersons;
import com.example.recordkeeperfina.database.MyDbHandler;
import com.example.recordkeeperfina.model.ItemsBean;
import com.example.recordkeeperfina.model.PersonsBean;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class PersonListActivity extends AppCompatActivity implements CustomRecyclerAdapterForPersons.OnItemSelected
{
    private static final String TAG ="check" ;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    private Context context = PersonListActivity.this;
    private MyDbHandler db;
    private List<PersonsBean>personsBeanList;
    private FloatingActionButton floatingActionButton;
    /// popup Layout items below;
    private EditText editTextName,editTextNumber;
    private Button buttonSave;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private int selectedIndex =-1;
    private int personId = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_list);
        getSupportActionBar().setTitle("Persons List");
        getSupportActionBar().setElevation(0);
        db = new MyDbHandler(this);
        personsBeanList = db.getPersonsList();

        floatingActionButton = findViewById(R.id.floatingActionButtonPersonListActivity);
        recyclerView = findViewById(R.id.recyclerViewPersonListActivity);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        floatingActionButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                popupLayout();
            }
        });



        showPersonList();
    } ///

    private void popupLayout()
    {
        builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup_add_person,null);

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
                if(name.isEmpty()||number.isEmpty())
                {
                    Toasty.warning(context,"Enter All Fields",Toasty.LENGTH_SHORT).show();
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
                           startActivity(new Intent(context,PersonListActivity.class));
                       }
                   },200);
                }


            } // onClick closed
        }); // buttonSave onClick Listener closed


    } // method Popup layout closed

    private void showPersonList()
    {
        adapter = new CustomRecyclerAdapterForPersons(PersonListActivity.this,personsBeanList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    @Override
    public void onListItemClicked(int index)
    {
       selectedIndex = index;
       CardView cardView  = findViewById(R.id.cardViewCustomRecyclerViewPersons);
       personId = personsBeanList.get(selectedIndex).getPersonId();
       Intent intent = new Intent(context,ItemsListActivity.class);
       intent.putExtra("personId",personId);
       intent.putExtra("personName",personsBeanList.get(selectedIndex).getPersonName());
       startActivity(intent);
    }

    @Override
    public void onDeleteClicked(int index)
    {
        selectedIndex = index;
        deleteDialog(selectedIndex);

    } // onDeleteClicked
    private void deleteDialog(final int selectedIndex)
    {

        builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert !");
        builder.setMessage("Do you want to delete this record");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(final DialogInterface dialog, int which)
            {
                PersonsBean bean = personsBeanList.get(selectedIndex);
                db.deletePerson(bean.getPersonId());
                personsBeanList.remove(selectedIndex);
                adapter.notifyItemRemoved(selectedIndex);
                Toasty.success(context, "Deleted", Toasty.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if(db.getPersonsCount()==00)
                        {
                            Intent intent = new Intent(context, MainActivity.class);
                            startActivity(intent);
                            finish();                        }
                        else
                        {

                            dialog.dismiss();
                        }


                    }
                }, 200); // Handler closed
            }
        }); // positive button closed
        builder.setNegativeButton("No", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });
        builder.show();

    } // deleteDialog closed
        @Override
    public void onEditClicked(int index)
    {
        selectedIndex = index;
        builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup_add_person,null);

        editTextName = view.findViewById(R.id.editTextPopupAddPersonName);
        editTextNumber = view.findViewById(R.id.editTextPopupAddPersonNumber);
        buttonSave = view.findViewById(R.id.buttonPopuAddPersonSave);
        builder.setView(view);
        dialog = builder.create();
        dialog.show();

        PersonsBean bean = personsBeanList.get(selectedIndex);
        editTextName.setText(bean.getPersonName());
        editTextNumber.setText(bean.getPersonNumber());

        buttonSave.setText("Update");
        buttonSave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String name = editTextName.getText().toString();
                String number = editTextNumber.getText().toString().trim();
                if(name.isEmpty()||number.isEmpty())
                {
                    Toasty.warning(context,"Enter All Fields",Toasty.LENGTH_SHORT).show();
                } // if closed
                else
                {

                    PersonsBean personsBean = db.getPerson(personsBeanList.get(selectedIndex).getPersonId());
                    personsBean.setPersonName(name);
                    personsBean.setPersonNumber(number);
                    db.updatePerson(personsBean);
                    Toasty.success(context, "Person Updated SuccessFully", Toasty.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            startActivity(new Intent(PersonListActivity.this,PersonListActivity.class));
                            finish();
                        }
                    },200);

                }


            } // onClick closed
        }); // buttonSave onClick Listener closed

    } // onEditClicked closed

    @Override
    public void onCallClicked(int index)
    {
        PersonsBean bean = personsBeanList.get(index);
        String number = bean.getPersonNumber();
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+number));
        startActivity(intent);
        //finish();
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
    }

}
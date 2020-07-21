package com.example.recordkeeperfina.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recordkeeperfina.R;
import com.example.recordkeeperfina.adapters.CustomAdapterForItems;
import com.example.recordkeeperfina.adapters.CustomRecyclerAdapterForPersons;
import com.example.recordkeeperfina.database.MyDbHandler;
import com.example.recordkeeperfina.model.ItemsBean;
import com.example.recordkeeperfina.model.PersonsBean;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class ItemsListActivity extends AppCompatActivity implements CustomAdapterForItems.OnItemClicked
{
    private static final String TAG ="check" ;
    private Context context =ItemsListActivity.this;
    int personId;
    String personName;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    private TextView textViewNoData ,textViewTotalPrice,textViewItemsCount;
    private MyDbHandler db;
    private FloatingActionButton floatingActionButton;
    // Popup layout items below
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private EditText editTextItemName  ,editTextPrice;
    String date = "";
    String itemName ="";
    String itemPrice = "";
    private Button buttonSave, buttonAddDate;
    private DatePicker datePicker;
    private LinearLayout linearLayout;
    private List<ItemsBean>itemsBeanList;
    int selectedIndex = -1;
    int price;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_list);
        Bundle bundle = getIntent().getExtras();

        personId = bundle.getInt("personId");
        personName = bundle.getString("personName");


        Log.d(TAG, "onCreate: "+personName);
        getSupportActionBar().setTitle(personName+" - Items List");
        getSupportActionBar().setElevation(0);
        db = new MyDbHandler(this);


        textViewTotalPrice = findViewById(R.id.textViewTotalPrice);
        textViewItemsCount = findViewById(R.id.texViewActivityIemsListItemCount);
        itemsBeanList = db.getItemList(personId);
        floatingActionButton = findViewById(R.id.floatingActionButtonItemsListActivity);

        for(ItemsBean priceCount:db.getItemList(personId))
        {
             price =price+priceCount.getItemPrice();

        }
        textViewTotalPrice.setText("Total Amount :"+price);
        textViewItemsCount.setText("Total Items : "+db.getItemsCountAtPersonId(personId));

        Log.d(TAG, "onCreate: "+db.getTotalItems());
        Log.d(TAG, "onCreate: "+personId);


        recyclerView = findViewById(R.id.recyclerViewItemsListActivity);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        textViewNoData = findViewById(R.id.textViewNoDataItemsActivity);



        floatingActionButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                popupForItems();
            }
        });

        showItemsList();
    } //

    private void popupForItems()
    {
        builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup_add_item,null);
        editTextItemName = view.findViewById(R.id.editTextPopupAddItemsItemName);
        editTextPrice = view.findViewById(R.id.editTextPopupAddItemsItemPrice);
        buttonSave = view.findViewById(R.id.buttonPopupAddItemSave);
        buttonAddDate = view.findViewById(R.id.buttonPopupAddItemAddDate);
        datePicker = view.findViewById(R.id.popupAddItemDatePicker);
        linearLayout = view.findViewById(R.id.linearLayoutPopupAddItem);
        builder.setView(view);
        dialog = builder.create();
        dialog.show();


        buttonAddDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                itemName = editTextItemName.getText().toString().trim();
                itemPrice = editTextPrice.getText().toString().trim();
                if (itemName.isEmpty()||itemPrice.isEmpty())
                {
                    Toasty.warning(context,"Enter Item & Price ",Toasty.LENGTH_SHORT).show();
                }
                else
                {
                    linearLayout.setVisibility(View.GONE);
                    datePicker.setVisibility(View.VISIBLE);
                    buttonSave.setVisibility(View.VISIBLE);

                    Calendar calendar = Calendar.getInstance();
                    datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                            new DatePicker.OnDateChangedListener()
                            {
                                @Override
                                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                                {
                                    date = dayOfMonth+"/"+monthOfYear+"/"+year;
                                } // onDateChanged closed
                            } // new DatePicker closed
                    );// date init closed
                }  // else closed
            }//onClick closed
        }); // buttonAdd Date OnClickListener closed
        buttonSave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ItemsBean bean = new ItemsBean();
                bean.setItemName(itemName);
                bean.setItemPrice(Integer.parseInt(itemPrice));
                bean.setPersonIdItem(personId);
                bean.setDate(date);
                if(!(date.isEmpty()||itemName.isEmpty()||itemPrice.isEmpty()))
                {
                    db.addItem(bean);
                    new Handler().postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Intent intent = new Intent(context,ItemsListActivity.class);
                            intent.putExtra("personId",personId);
                            intent.putExtra("personName",personName);
                            startActivity(intent);
                            finish();

                        }},200); // Handler closed
                    } // if closed
                else
                {
                    Toasty.warning(ItemsListActivity.this,"Please Pick Date",Toasty.LENGTH_SHORT).show();
                }

            } // oClick closed
        });
    } // popupForItems closed

    private void showItemsList()
    {
        if(db.getItemsCountAtPersonId(personId)<1)
        {
            textViewNoData.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        else
        {
            adapter = new CustomAdapterForItems(db.getItemList(personId),ItemsListActivity.this);
            recyclerView.setAdapter(adapter);
        }
    } // showItems ListClosed


    @Override
    public void onItemDeleteClicked(int index)
    {
       selectedIndex = index;
        deleteDialog(selectedIndex);
    }// onDeleteITemClicked

    private void deleteDialog(final int selectedIndex)
    {

        builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert !");
        builder.setMessage("Do you want to delete this item");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                List<ItemsBean>list = db.getTotalItems();
                ItemsBean bean = db.getItem(list.get(selectedIndex).getItemId());
                db.deleteItem(bean);
                adapter.notifyItemRemoved(selectedIndex);



                Toasty.success(context, "Deleted", Toasty.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Intent intent = new Intent(context,ItemsListActivity.class);
                        intent.putExtra("personId",personId);
                        intent.putExtra("personName",personName);
                        startActivity(intent);
                        finish();

                           // showItemsList();
                    }},400); // Handler closed
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

    } // deleteSelectedItemClosed


    @Override
    public void onItemEditClicked(int index)
    {

        final List<ItemsBean>itemsList  = db.getTotalItems();


        selectedIndex = index;
        builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup_add_item,null);
        editTextItemName = view.findViewById(R.id.editTextPopupAddItemsItemName);
        editTextPrice = view.findViewById(R.id.editTextPopupAddItemsItemPrice);
        buttonSave = view.findViewById(R.id.buttonPopupAddItemSave);
        buttonAddDate = view.findViewById(R.id.buttonPopupAddItemAddDate);
        datePicker = view.findViewById(R.id.popupAddItemDatePicker);
        linearLayout = view.findViewById(R.id.linearLayoutPopupAddItem);
        builder.setView(view);
        dialog = builder.create();
        dialog.show();
      Log.d(TAG, "onItemEditClicked: "+selectedIndex);

       ItemsBean itemsBean = itemsList.get(selectedIndex);
        editTextItemName.setText(itemsBean.getItemName());
        editTextPrice.setText(""+itemsBean.getItemPrice());

        buttonSave.setText("Update");
        buttonAddDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                itemName = editTextItemName.getText().toString().trim();
                itemPrice = editTextPrice.getText().toString().trim();
                if (itemName.isEmpty()||itemPrice.isEmpty())
                {
                    Toasty.warning(context,"Enter Item & Price ",Toasty.LENGTH_SHORT).show();
                }
                else
                {
                    linearLayout.setVisibility(View.GONE);
                    datePicker.setVisibility(View.VISIBLE);
                    buttonSave.setVisibility(View.VISIBLE);

                    Calendar calendar = Calendar.getInstance();
                    datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                            new DatePicker.OnDateChangedListener()
                            {
                                @Override
                                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                                {
                                    date = dayOfMonth+"/"+monthOfYear+"/"+year;
                                } // onDateChanged closed
                            } // new DatePicker closed
                    );// date init closed
                }  // else closed
            }//onClick closed
        }); // buttonAdd Date OnClickListener closed
        buttonSave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ItemsBean bean = db.getItem(itemsList.get(selectedIndex).getItemId());
                bean.setItemName(itemName);
                bean.setItemPrice(Integer.parseInt(itemPrice));
                bean.setPersonIdItem(personId);
                bean.setDate(date);
                if(!(date.isEmpty()||itemName.isEmpty()||itemPrice.isEmpty()))
                {
                    db.updateItem(bean);
                    Toasty.success(context, "Item Updated SuccessFully", Toasty.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Intent intent = new Intent(context,ItemsListActivity.class);
                            intent.putExtra("personId",personId);
                            intent.putExtra("personName",personName);
                            startActivity(intent);
                            finish();

                        }},200); // Handler closed
                } // if closed
                else
                {
                    Toasty.warning(ItemsListActivity.this,"Please Pick Date",Toasty.LENGTH_SHORT).show();
                }

            } // oClick closed
        });
    } //




}
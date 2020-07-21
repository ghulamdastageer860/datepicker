package com.example.recordkeeperfina.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.recordkeeperfina.utils.Utils;
import com.example.recordkeeperfina.model.ItemsBean;
import com.example.recordkeeperfina.model.PersonsBean;

import java.util.ArrayList;
import java.util.List;

public class MyDbHandler extends SQLiteOpenHelper
{
    public MyDbHandler(@Nullable Context context)
    {
        super(context, Utils.DATABASE_NAME, null, Utils.DATABASE_VERSION);
    }//

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(" CREATE TABLE IF NOT EXISTS "+Utils.TABLE_PERSONS+" ( "+
                Utils.PERSON_ID+" INTEGER PRIMARY KEY AUTOINCREMENT ,"+
                Utils.PERSON_NAME+" TEXT ,"+
                Utils.PERSON_NUMBER+" TEXT"+ ") " );
//        public static final String ITEM_ID = "itemId";
//        public static final String ITEM_NAME ="itemName";
//        public static final String ITEM_PRICE = "itemPrice";
//        public static final String PERSON_ID_Items = "personId";
//        public static final String DATE  ="date";

        db.execSQL("Create TABLE IF NOT EXISTS "+Utils.TABLE_ITEMS+" ( "+
                Utils.ITEM_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                Utils.ITEM_NAME+" TEXT , "+
                Utils.ITEM_PRICE+" INTEGER , "+
                Utils.PERSON_ID_Items+" TEXT ,"+
                Utils.DATE+" TEXT )"
        );
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS "+Utils.TABLE_PERSONS);
        db.execSQL("DROP TABLE IF EXISTS "+Utils.TABLE_ITEMS);
        onCreate(db);
    } // onUpgrade

    public void addPerson(PersonsBean personsBean)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Utils.PERSON_NAME,personsBean.getPersonName());
        cv.put(Utils.PERSON_NUMBER,personsBean.getPersonNumber());
        db.insert(Utils.TABLE_PERSONS,null,cv);
    } //
    public List<PersonsBean> getPersonsList()
    {

        List<PersonsBean>personsBeanList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[]columns = new String[]{Utils.PERSON_ID,Utils.PERSON_NAME, Utils.PERSON_NUMBER};
        Cursor cursor = db.query(Utils.TABLE_PERSONS,columns,null,null,null,
                null,null);
        if(cursor!=null) {
            for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext())
            {
                PersonsBean bean = new PersonsBean();
                bean.setPersonId(Integer.parseInt(cursor.getString(0)));
                bean.setPersonName(cursor.getString(1));
                bean.setPersonNumber(cursor.getString(2));
                personsBeanList.add(bean);
            } // for loop closed
        } // if closed
        return personsBeanList;
    }
    public PersonsBean getPerson(int id)
    {

        SQLiteDatabase db = this.getReadableDatabase();
        String[]columns = new String[]{Utils.PERSON_ID,Utils.PERSON_NAME, Utils.PERSON_NUMBER};
        Cursor cursor = db.query(Utils.TABLE_PERSONS,columns,Utils.PERSON_ID+"=?",new String[]{""+id},null,
                null,null);
        if(cursor!=null)
        {
            cursor.moveToFirst();
        }

        PersonsBean bean = new PersonsBean();
        bean.setPersonId(Integer.parseInt(cursor.getString(0)));
        bean.setPersonName(cursor.getString(1));
        bean.setPersonNumber(cursor.getString(2));
        return bean;
    }
    public void deletePerson(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        //String[]columns = new String[]{Utils.PERSON_ID,Utils.PERSON_NAME, Utils.PERSON_NUMBER};
        deleteItemsWherePersonId(id);
        db.delete(Utils.TABLE_PERSONS,Utils.PERSON_ID+"=?",new String[]{""+id});

    }
    public void updatePerson(PersonsBean personsBean)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Utils.PERSON_NAME,personsBean.getPersonName());
        cv.put(Utils.PERSON_NUMBER,personsBean.getPersonNumber());
        db.update(Utils.TABLE_PERSONS,cv,Utils.PERSON_ID+"=?",new String[]{""+personsBean.getPersonId()});
    } //
    public  int getPersonsCount()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM "+Utils.TABLE_PERSONS;
        Cursor cursor = db.rawQuery(query,null);
        return cursor.getCount();
    }

    ///  Table Items Queries

    public boolean addItem(ItemsBean itemsBean)
    {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Utils.ITEM_NAME,itemsBean.getItemName());
        cv.put(Utils.ITEM_PRICE,itemsBean.getItemPrice());
        cv.put(Utils.PERSON_ID_Items,itemsBean.getPersonIdItem());
        cv.put(Utils.DATE,itemsBean.getDate());
        long result =  db.insert(Utils.TABLE_ITEMS,null,cv);
        if(result ==-1)
        {
            return false;
        }
        else
        {
            return true;
        }
    } //
    public List<ItemsBean>getItemList(int id)
    {
        List<ItemsBean> itemsBeansList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String[]columns = new String[]{Utils.ITEM_ID,Utils.ITEM_NAME,Utils.ITEM_PRICE,Utils.PERSON_ID_Items,Utils.DATE};
        Cursor cursor = db.query(Utils.TABLE_ITEMS,columns,Utils.PERSON_ID_Items+"=?",new String[]{""+id}
                ,null,null,Utils.DATE);
        int iKeyId = cursor.getColumnIndex(Utils.ITEM_ID);
        int iKeyName = cursor.getColumnIndex(Utils.ITEM_NAME);
        int iKeyPrice = cursor.getColumnIndex(Utils.ITEM_PRICE);
        int iKeyPersonId = cursor.getColumnIndex(Utils.PERSON_ID_Items);
        int iKeyDate = cursor.getColumnIndex(Utils.DATE);
        for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext())
        {
            ItemsBean bean = new ItemsBean();
            bean.setItemId(Integer.parseInt(cursor.getString(iKeyId)));
            bean.setItemName(cursor.getString(iKeyName));
            bean.setItemPrice(Integer.parseInt(cursor.getString(iKeyPrice)));
            bean.setPersonIdItem(Integer.parseInt(cursor.getString(iKeyPersonId)));
            bean.setDate(cursor.getString(iKeyDate));
            itemsBeansList.add(bean);
        }
        return itemsBeansList;
    } //
    public void deleteItem(ItemsBean itemsBean)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Utils.TABLE_ITEMS,Utils.ITEM_ID+"=?",new String[]{""+itemsBean.getItemId()});
    } //
    private void deleteItemsWherePersonId(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Utils.TABLE_ITEMS,Utils.PERSON_ID_Items+"=?",new String[]{""+id});
    }
    public ItemsBean getItem(int id)
    {
        SQLiteDatabase db = getReadableDatabase();
        String[]columns = new String[]{Utils.ITEM_ID,Utils.ITEM_NAME,Utils.ITEM_PRICE,Utils.PERSON_ID_Items,Utils.DATE};
        Cursor cursor = db.query(Utils.TABLE_ITEMS,columns,Utils.ITEM_ID+"=?",new String[]{""+id}
                ,null,null,null);
        int iKeyId = cursor.getColumnIndex(Utils.ITEM_ID);
        int iKeyName = cursor.getColumnIndex(Utils.ITEM_NAME);
        int iKeyPrice = cursor.getColumnIndex(Utils.ITEM_PRICE);
        int iKeyPersonId = cursor.getColumnIndex(Utils.PERSON_ID_Items);
        int iKeyDate = cursor.getColumnIndex(Utils.DATE);
        if(cursor!=null)
        {
            cursor.moveToFirst();
        }
            ItemsBean bean = new ItemsBean();
            bean.setItemId(Integer.parseInt(cursor.getString(iKeyId)));
            bean.setItemName(cursor.getString(iKeyName));
            bean.setItemPrice(Integer.parseInt(cursor.getString(iKeyPrice)));
            bean.setPersonIdItem(Integer.parseInt(cursor.getString(iKeyPersonId)));
            bean.setDate(cursor.getString(iKeyDate));
        return bean;

    }
    public void updateItem(ItemsBean itemsBean)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Utils.ITEM_NAME,itemsBean.getItemName());
        cv.put(Utils.ITEM_PRICE,itemsBean.getItemPrice());
        cv.put(Utils.PERSON_ID_Items,itemsBean.getPersonIdItem());
        cv.put(Utils.DATE,itemsBean.getDate());
        db.update(Utils.TABLE_ITEMS,cv,Utils.ITEM_ID+"=?",new String[]{""+itemsBean.getItemId()});
    }

    public  int getItemsCountAtPersonId(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM "+Utils.TABLE_ITEMS+" WHERE "+Utils.PERSON_ID_Items+" = "+id;
        Cursor cursor = db.rawQuery(query,null);
        return cursor.getCount();
    } // closed


    public List<ItemsBean>getTotalItems()
    {
        List<ItemsBean> itemsBeansList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String[]columns = new String[]{Utils.ITEM_ID,Utils.ITEM_NAME,Utils.ITEM_PRICE,Utils.PERSON_ID_Items,Utils.DATE};
        Cursor cursor = db.query(Utils.TABLE_ITEMS,columns,null,null
                ,null,null,null);
        int iKeyId = cursor.getColumnIndex(Utils.ITEM_ID);
        int iKeyName = cursor.getColumnIndex(Utils.ITEM_NAME);
        int iKeyPrice = cursor.getColumnIndex(Utils.ITEM_PRICE);
        int iKeyPersonId = cursor.getColumnIndex(Utils.PERSON_ID_Items);
        int iKeyDate = cursor.getColumnIndex(Utils.DATE);
        for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext())
        {
            ItemsBean bean = new ItemsBean();
            bean.setItemId(Integer.parseInt(cursor.getString(iKeyId)));
            bean.setItemName(cursor.getString(iKeyName));
            bean.setItemPrice(Integer.parseInt(cursor.getString(iKeyPrice)));
            bean.setPersonIdItem(Integer.parseInt(cursor.getString(iKeyPersonId)));
            bean.setDate(cursor.getString(iKeyDate));
            itemsBeansList.add(bean);
        }
        return itemsBeansList;
    } //



}

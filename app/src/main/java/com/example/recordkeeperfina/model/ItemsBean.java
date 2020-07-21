package com.example.recordkeeperfina.model;

public class ItemsBean
{
    private int itemId;
    private String itemName;
    private int itemPrice;
    private int personIdItem;
    private String Date;

    public ItemsBean()
    {
    }

    public ItemsBean(int itemId, String itemName, int itemPrice, int personIdItem, String date)
    {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.personIdItem = personIdItem;
        Date = date;
    }

    public int getItemId()
    {
        return itemId;
    }

    public void setItemId(int itemId)
    {
        this.itemId = itemId;
    }

    public String getItemName()
    {
        return itemName;
    }

    public void setItemName(String itemName)
    {
        this.itemName = itemName;
    }

    public int getItemPrice()
    {
        return itemPrice;
    }

    public void setItemPrice(int itemPrice)
    {
        this.itemPrice = itemPrice;
    }

    public int getPersonIdItem()
    {
        return personIdItem;
    }

    public void setPersonIdItem(int personIdItem)
    {
        this.personIdItem = personIdItem;
    }

    public String getDate()
    {
        return Date;
    }

    public void setDate(String date)
    {
        Date = date;
    } //

    @Override
    public String toString()
    {
        return "ItemsBean{" +
                "itemId=" + itemId +
                ", itemName='" + itemName + '\'' +
                ", itemPrice=" + itemPrice +
                ", personIdItem=" + personIdItem +
                ", Date='" + Date + '\'' +
                '}';
    }
}

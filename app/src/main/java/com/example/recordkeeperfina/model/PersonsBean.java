package com.example.recordkeeperfina.model;

public class PersonsBean
{
    private int personId;
    private String personName;
    private String personNumber;

    public PersonsBean(int personId, String personName, String personNumber)
    {
        this.personId = personId;
        this.personName = personName;
        this.personNumber = personNumber;
    }

    public PersonsBean()
    {

    }

    public int getPersonId()
    {
        return personId;
    }

    public void setPersonId(int personId)
    {
        this.personId = personId;
    }

    public String getPersonName()
    {
        return personName;
    }

    public void setPersonName(String personName)
    {
        this.personName = personName;
    }

    public String getPersonNumber()
    {
        return personNumber;
    }

    public void setPersonNumber(String personNumber)
    {
        this.personNumber = personNumber;
    } //

    @Override
    public String toString()
    {
        return "PersonsBean{" +
                "personId=" + personId +
                ", personName='" + personName + '\'' +
                ", personNumber='" + personNumber + '\'' +
                '}';
    }
}

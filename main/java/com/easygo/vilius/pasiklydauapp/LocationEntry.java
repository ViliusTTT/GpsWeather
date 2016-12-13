package com.easygo.vilius.pasiklydauapp;

/**
 * Vietoves duomenis sauganti klase
 */
public class LocationEntry {

    private int mID;            //Indeksas
    private String mAdress;     //Adresas
    private String mDate;

    /**
     * Tuscias konstruktorius
     */
    public LocationEntry()
    {
        mID = 0;
        mAdress = "";
        mDate = "";
    }

    /**
     * Konstruktorius su parametrais
     * @param id - indeksas
     * @param address - adresas
     * @param date - data
     */
    public LocationEntry(int id, String address, String date)
    {
        mID = id;
        mAdress = address;
        mDate = date;
    }

    /**
     * Nustato id reiksme
     * @param val - nustatoma reiksme
     */
    public void setID(int val)
    {
        mID = val;
    }

    /**
     * Grazina id reiksme
     * @return id - indeksas
     */
    public int getID()
    {
        return mID;
    }

    /**
     * Nustatyti adresa
     * @param val - nustatoma reiksme
     */
    public void setAddress(String val)
    {
        mAdress = val;
    }

    /**
     * Grazinti adresa
     * @return mAdress-adresas
     */
    public String getAddress()
    {
        return mAdress;
    }

    /**
     * Nustato data
     * @param val- datos reiksme
     */
    public void setDate(String val)
    {
        mDate = val;
    }

    /**
     * Grazina data
     * @return mDate - grazinama data
     */
    public String getDate()
    {
        return mDate;
    }
}
package com.example.fulltaskc4i.ModelsPackage;

public class PersonModel implements java.io.Serializable {
    private int id;
    private String personName, personSaying, personInfo;
    private byte[] personImage;

    public PersonModel(int id, String personName, String personSaying, String personInfo, byte[] personImage) {
        this.id = id;
        this.personName = personName;
        this.personSaying = personSaying;
        this.personInfo = personInfo;
        this.personImage = personImage;
    }

    public int getId() {
        return id;
    }

    public String getPersonName() {
        return personName;
    }

    public String getPersonSaying() {
        return personSaying;
    }

    public String getPersonInfo() {
        return personInfo;
    }


    public byte[] getPersonImage() {
        return personImage;
    }

}

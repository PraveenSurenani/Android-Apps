package com.sam.contactsdirectory;

/**
 * HW 2
 * Contact.java
 * Samuel Painter and Praveen Surenani
 */

import android.app.Application;
import android.content.ContentResolver;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class Contact implements Parcelable, Comparable<Contact> {
    private String name;
    private String number;
    private String email;
    private Uri photo;

    public Contact(String name, String number, String email) {
        this.name = name;
        this.number = number;
        this.email = email;
        this.photo = null;
//        this.photo = Uri.parse("android.resource://com.sam.contactsdirectory/" + R.drawable.default_photo);
    }

    public Contact(String name, String number, String email, Uri photo) {

        this.name = name;
        this.number = number;
        this.email = email;
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Uri getPhoto() {return photo;}

    public void setPhoto(Uri photo) { this.photo = photo; }

    @Override
    public String toString() {
        return "Contact{" +
                "name='" + name + '\'' +
                ", number='" + number + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    @Override
    public int compareTo(Contact another) {
        int lastCmp = this.name.compareTo(another.name);
        return (lastCmp != 0 ? lastCmp : this.name.compareTo(another.name));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Contact contact = (Contact) o;

        if (!getName().equals(contact.getName())) return false;
        if (!getNumber().equals(contact.getNumber())) return false;
        return getEmail().equals(contact.getEmail());

    }

    @Override
    public int hashCode() {
        int result = getName().hashCode();
        result = 31 * result + getNumber().hashCode();
        result = 31 * result + getEmail().hashCode();
        return result;
    }

    public Contact(Parcel in) {
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.number);
        dest.writeString(this.email);
        dest.writeParcelable(this.photo, flags);
    }

    private void readFromParcel(Parcel in) {
        this.name = in.readString();
        this.number = in.readString();
        this.email = in.readString();
        this.photo = in.readParcelable(Uri.class.getClassLoader());
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };
}

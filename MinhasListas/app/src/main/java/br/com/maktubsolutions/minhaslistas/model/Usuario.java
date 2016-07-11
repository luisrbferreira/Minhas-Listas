package br.com.maktubsolutions.minhaslistas.model;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import br.com.maktubsolutions.minhaslistas.util.LibraryClass;

/**
 * Created by luis.ferreira on 09/06/2016.
 */
public class Usuario {
    public static String PROVIDER = "br.com.maktubsolutions.minhaslistas.model.Usuario.PROVIDER";

    private String id;
    private String name;
    private String email;
    private String password;
    private String celular;
    private String newPassword;

    public Usuario() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private void setNameInMap(Map<String, Object> map) {
        if (getName() != null) {
            map.put("name", getName());
        }
    }

    public void setNameIfNull(String name) {
        if (this.name == null) {
            this.name = name;
        }
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private void setEmailInMap(Map<String, Object> map) {
        if (getEmail() != null) {
            map.put("email", getEmail());
        }
    }

    public void setEmailIfNull(String email) {
        if (this.email == null) {
            this.email = email;
        }
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }


    @Exclude
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @Exclude
    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }


    public void saveProviderSP(Context context, String token) {
        LibraryClass.saveSP(context, PROVIDER, token);
    }

    public String getProviderSP(Context context) {
        return (LibraryClass.getSP(context, PROVIDER));
    }


    public void saveDB(DatabaseReference.CompletionListener... completionListener) {
        DatabaseReference firebase = LibraryClass.getFirebase().child("users").child(getId());

        if (completionListener.length == 0) {
            firebase.setValue(this);
        } else {
            firebase.setValue(this, completionListener[0]);
        }
    }

    public void updateDB(DatabaseReference.CompletionListener... completionListener) {

        DatabaseReference firebase = LibraryClass.getFirebase().child("users").child(getId());

        Map<String, Object> map = new HashMap<>();
        setNameInMap(map);
        setEmailInMap(map);

        if (map.isEmpty()) {
            return;
        }

        if (completionListener.length > 0) {
            firebase.updateChildren(map, completionListener[0]);
        } else {
            firebase.updateChildren(map);
        }
    }

    public void removeDB(DatabaseReference.CompletionListener completionListener) {

        DatabaseReference firebase = LibraryClass.getFirebase().child("users").child(getId());
        firebase.setValue(null, completionListener);
    }

    public void contextDataDB(Context context) {
        DatabaseReference firebase = LibraryClass.getFirebase().child("users").child(getId());

        firebase.addListenerForSingleValueEvent((ValueEventListener) context);
    }
}

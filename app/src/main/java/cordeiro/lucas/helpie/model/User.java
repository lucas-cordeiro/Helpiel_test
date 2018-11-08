package cordeiro.lucas.helpie.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private int id;
    private String name;
    private String email;
    private Company company;
    private String city;

    public User() {
        this.id = 0;
        this.name = "Null";
        this.email = "Null";
        this.city = "Null";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompanyName(){
        return company.getName();
    }

    public void setCompanyName(String name){
        company.setName(name);
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}

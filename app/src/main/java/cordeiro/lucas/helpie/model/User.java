package cordeiro.lucas.helpie.model;

public class User {
    private int id;
    private String name;
    private String email;
    private String companyName;
    private String city;

    public User() {
        this.id = 0;
        this.name = "Null";
        this.email = "Null";
        this.companyName = "Null";
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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}

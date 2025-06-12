package models.pojo;

public class RegisterResponseBodyModel {
    //"{\"email\": \"eve.holt@reqres.in\",\"password\": \"pistol\"}"
    String token;
    Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String password) {
        this.token = password;
    }


}

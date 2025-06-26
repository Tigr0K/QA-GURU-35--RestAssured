package models;
import lombok.Data;

@Data
public class GetUserResponseLombokModel {
    Data data;
    Support support;
    @lombok.Data
    public static class Data {
        private Integer id;
        private String email;
        private String first_name;
        private String last_name;
        private String avatar;
    }

    @lombok.Data
    public static class Support {
        private String url;
        private String text;
    }

}

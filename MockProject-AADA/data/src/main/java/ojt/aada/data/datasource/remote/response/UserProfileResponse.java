package ojt.aada.data.datasource.remote.response;

public class UserProfileResponse {
    private String id;
    private String name;
    private String email;
    private String avatar;
    private String gender;
    private String dob;

    public UserProfileResponse() {
    }

    public UserProfileResponse(String id, String name, String email, String avatar, String gender, String dob) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.avatar = avatar;
        this.gender = gender;
        this.dob = dob;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }
}

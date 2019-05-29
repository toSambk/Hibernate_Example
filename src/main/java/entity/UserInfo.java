package entity;

import javax.persistence.*;

@Entity
@Table(name = "users_info")
public class UserInfo {

    @OneToOne (cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private User user;

    @Id
    @Column(name = "user_id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer userId;

    private String name;

    @Column(name = "last_login")
    private String lastLogin;

    @Column(name = "email", unique = true)
    private String email;

    private String phone;


    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

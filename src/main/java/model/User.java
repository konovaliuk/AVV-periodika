package model;

import java.util.Objects;
import java.util.StringJoiner;

public class User implements Entity<Long> {
    private Long id;
    private Long userTypeId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String address;
    private String phone;
    private String login;
    private String password;

    public User() {
    }

    public User(String firstName,
                String middleName,
                String lastName,
                String email,
                String address,
                String phone,
                String login,
                String password) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.login = login;
        this.password = password;
    }

    public User(Long id,
                Long userTypeId,
                String firstName,
                String middleName,
                String lastName,
                String email,
                String address,
                String phone,
                String login,
                String password) {
        this.id = id;
        this.userTypeId = userTypeId;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.login = login;
        this.password = password;
    }

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserTypeId() {
        return userTypeId;
    }

    public void setUserTypeId(Long userTypeId) {
        this.userTypeId = userTypeId;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }


    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(userTypeId, user.userTypeId) &&
               Objects.equals(firstName, user.firstName) && Objects.equals(middleName, user.middleName) &&
               Objects.equals(lastName, user.lastName) && Objects.equals(email, user.email) &&
               Objects.equals(address, user.address) && Objects.equals(phone, user.phone) &&
               Objects.equals(login, user.login) && Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userTypeId, firstName, middleName, lastName, email, address, phone, login, password);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", User.class.getSimpleName() + "[", "]").add("id=" + id)
                .add("userTypeId=" + userTypeId)
                .add("firstName='" + firstName + "'")
                .add("middleName='" + middleName + "'")
                .add("lastName='" + lastName + "'")
                .add("email='" + email + "'")
                .add("address='" + address + "'")
                .add("phone='" + phone + "'")
                .add("login='" + login + "'")
                .add("password='" + password + "'")
                .toString();
    }

}

package org.aethos;

public class User {
    private String cpf;
    private String birthDate;
    private String email;
    private String name;
    private String phone;
    private String password;
    private String confirmationPassword;

    public User(
            String cpf,
            String birthDate,
            String email,
            String name,
            String phone,
            String password,
            String confirmationPassword)
    {
        this.cpf = cpf;
        this.birthDate = birthDate;
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.password = password;
        this.confirmationPassword = confirmationPassword;
    }

    public String getCpf()
    {
        return cpf;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirmationPassword() {
        return confirmationPassword;
    }
}

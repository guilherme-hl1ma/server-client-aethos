package org.aethos;

public class UserValidator extends Message {
    private String messageCpf;
    private String messageBirthDate;
    private String messageEmail;
    private String messageName;
    private String messagePhone;
    private String messagePassword;
    private String messageConfirmationPassword;

    public UserValidator()
    {
        this.messageCpf = "";
        this.messageBirthDate = "";
        this.messageEmail = "";
        this.messageName = "";
        this.messagePhone = "";
        this.messagePassword = "";
        this.messageConfirmationPassword = "";
    }

    public void validateUser(
            String cpf,
            String birthDate,
            String email,
            String name,
            String phone,
            String password,
            String confirmationPassword)
    {
        this.messageCpf = isCpfValid(cpf);
        this.messageBirthDate = isBirthDateValid(birthDate);
        this.messageEmail = isEmailValid(email);
        this.messageName = isNameValid(name);
        this.messagePhone = isPhoneValid(phone);
        this.messagePassword = isPasswordValid(password);
        this.messageConfirmationPassword = isConfirmationPasswordValid(confirmationPassword, password);
    }

    private static String isCpfValid(String cpf)
    {
        if (cpf.length() != 14) return "CPF inválido. Deve ter 11 números!";
        return "";
    }

    private static String isBirthDateValid(String birthDate)
    {
        if (birthDate.length() != 10) return "Data de nascimento inválida. DD/MM/AAAA";

        byte day = Byte.parseByte(birthDate.substring(0, 2));
        byte month = Byte.parseByte(birthDate.substring(3, 5));
        short year = Short.parseShort(birthDate.substring(6));

        if (day == 30 && (month != 4 && month != 6 && month != 9 && month != 11))
            return "Data de nascimento inválida";

        if (day == 31 && (month != 1 && month != 3 && month != 5 && month != 7 &&
                month != 8 && month != 10 && month != 12))
            return "Data de nascimento inválida";

        if (month == 2)
        {
            if (year % 4 == 0 && year % 100 != 0 && day != 29) return "Data de nascimento inválida";
            if (year % 400 == 0 && day != 29) return "Data de nascimento inválida";
            if (day > 29) return "Data de nascimento inválida";
        }

        if (day > 31) return "Data de nascimento inválida";

        return "";
    }

    private static String isEmailValid(String email)
    {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        if (email.matches(emailRegex)) return "";
        return "Email inválido";
    }

    private static String isNameValid(String name) {
        String nameRegex = "^[^0-9]*$";
        if (name.matches(nameRegex)) return "";
        return "Nome inválido";
    }

    private static String isPhoneValid(String phone)
    {
        String telefoneRegex = "^\\(\\d{2}\\) ?1? ?\\d{4,5}-\\d{4}$";
        if (phone.matches(telefoneRegex)) return "";
        return "Telefone inválido. (XX) X XXXX-XXXX";
    }

    private static String isPasswordValid(String passsword)
    {
        if (passsword.length() < 6) return "Senha inválida. Deve ter no mínimo 6 caracteres";
        return "";
    }

    private static String isConfirmationPasswordValid(String confirmationPassword, String password)
    {
        if (confirmationPassword.length() < 6)   return "Senha de confirmação inválida. Deve ter no mínimo 6 " +
                "caracteres";

        if (!confirmationPassword.equals(password))   return "As senhas devem coincidir";

        return "";
    }
}


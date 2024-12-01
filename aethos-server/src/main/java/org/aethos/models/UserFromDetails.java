package org.aethos.models;

/**
 * Classe que os detalhes do usuário que realizou a ação que gerou a notificação.
 */
public class UserFromDetails {
    private String uid;
    private String name;

    public UserFromDetails(String uid, String name) {
        this.uid = uid;
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return String.format(
                "Source User Name: %s%n",
                this.getName()
        );
    }

    @Override
    public int hashCode()
    {
        int aux = 11;

        aux = aux * 11 + String.valueOf(uid).hashCode();
        aux = aux * 11 + String.valueOf(name).hashCode();

        if (aux < 0)    aux = -aux;

        return aux;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)    return true;
        if (obj == null)    return false;
        if (this.getClass() != obj.getClass())  return false;

        UserFromDetails userFromDetails = (UserFromDetails) obj;

        return true;
    }
}

package com.booxware.test;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * The encryption can be very simple, we don't put much emphasis on the
 * encryption algorithm.
 * Modified into Entity bean with data validation support.
 */
@Entity
@Table(name = "ACCOUNT")
public class Account implements Serializable {

    private static final long serialVersionUID = -7984003838755412618L;

    @Id
    @NotNull
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO,
                    generator = "ACCOUNT_SEQ_GEN")
    @SequenceGenerator(name = "ACCOUNT_SEQ_GEN", sequenceName = "ACCOUNT_SEQ")
    private Long id;

    @NotNull
    @Size(min = 3, max = 10)
    @Column(name = "USERNAME", unique = true, updatable = false)
    private String username;

    @Lob
    @NotNull
    @Column(name = "ENCRYPTED_PASSWORD")
    private byte[] encryptedPassword;

    @NotNull
    @Column(name = "SALT")
    private String salt;

    @NotNull
    @Column(name = "EMAIL")
    @Email
    private String email;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name = "LAST_LOGIN")
    private Date lastLogin;

    public Account() {
    }

    public Account(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public byte[] getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(byte[] encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Account{");
        sb.append("id=").append(id)
                .append(", username=").append(username)
                .append(", encryptedPassword=")
                .append(Arrays.toString(encryptedPassword))
                .append(", salt=").append(salt)
                .append(", email=").append(email)
                .append(", lastLogin=").append(lastLogin)
                .append('}');
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.id);
        hash = 89 * hash + Objects.hashCode(this.username);
        hash = 89 * hash + Arrays.hashCode(this.encryptedPassword);
        hash = 89 * hash + Objects.hashCode(this.salt);
        hash = 89 * hash + Objects.hashCode(this.email);
        hash = 89 * hash + Objects.hashCode(this.lastLogin);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Account other = (Account) obj;
        if (!Objects.equals(this.username, other.username)) {
            return false;
        }
        if (!Objects.equals(this.salt, other.salt)) {
            return false;
        }
        if (!Objects.equals(this.email, other.email)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Arrays.equals(this.encryptedPassword, other.encryptedPassword)) {
            return false;
        }
        return Objects.equals(this.lastLogin, other.lastLogin);
    }

}

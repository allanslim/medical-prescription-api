package com.codewarrior.csc686.project.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table( name = "MRX_USER")
@SequenceGenerator( name = "PK", sequenceName = "MRX_USER_UID_SEQ" )
public class MrxUser {

    @Id
    @GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "PK" )
    public long user_id;

    @Column( name = "TOKEN" )
    public String token;

    @Column( name = "EMAIL", nullable = false )
    public String email;

    @Column( name = "ENCRYPTED_PASSWORD", nullable = false )
    public String encryptedPassword;

    @Column( name = "PASSWORD_SALT", nullable = false )
    public String passwordSalt;

    @Type( type = "yes_no" )
   	@Column( name = " IS_VALID" )
    public boolean isValid;

    @Column( name = "LAST_CHANGED_BY", nullable = false )
    public String lastChangedBy;

    @Column( name = "LAST_CHANGED_ON", nullable = false )
    public Date lastChangedOn;


    @Override
    public boolean equals(Object obj) {

        if (obj == null) { return false; }
        if (obj == this) { return true; }
        if (obj.getClass() != getClass()) {
            return false;
        }
        MrxUser rhs = (MrxUser) obj;
        return new EqualsBuilder().append(this.user_id, rhs.user_id).append(this.token, rhs.token).append(this.email, rhs.email).append(this.encryptedPassword, rhs.encryptedPassword).append(this.passwordSalt, rhs.passwordSalt).append(this.isValid, rhs.isValid).append(this.lastChangedBy, rhs.lastChangedBy).append(this.lastChangedOn, rhs.lastChangedOn).isEquals();
    }

    @Override
    public int hashCode() {

        return new HashCodeBuilder().append(user_id).append(token).append(email).append(encryptedPassword).append(passwordSalt).append(isValid).append(lastChangedBy).append(lastChangedOn).toHashCode();
    }


    @Override
    public String toString() {

        return new ToStringBuilder(this).append("user_id", user_id).append("token", token).append("email", email).append("encryptedPassword", encryptedPassword).append("passwordSalt", passwordSalt).append("isValid", isValid).append("lastChangedBy", lastChangedBy).append("lastChangedOn", lastChangedOn).toString();
    }
}

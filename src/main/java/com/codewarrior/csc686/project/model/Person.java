package com.codewarrior.csc686.project.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("person")
public class Person {

    @JsonProperty
    private String firstName;

    @JsonProperty
    private String lastName;

    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }


    @Override
    public boolean equals(Object obj) {

        if (obj == null) { return false; }
        if (obj == this) { return true; }
        if (obj.getClass() != getClass()) {
            return false;
        }
        Person rhs = (Person) obj;
        return new org.apache.commons.lang3.builder.EqualsBuilder().append(this.firstName, rhs.firstName).append(this.lastName, rhs.lastName).isEquals();
    }

    @Override
    public int hashCode() {

        return new org.apache.commons.lang3.builder.HashCodeBuilder().append(firstName).append(lastName).toHashCode();
    }
}

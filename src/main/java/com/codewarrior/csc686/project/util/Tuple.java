package com.codewarrior.csc686.project.util;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Tuple<X, Y> {

    public final X x;
    public final Y y;


    public Tuple(X x, Y y) {
        this.x = x;
        this.y = y;
    }


    @Override
    public boolean equals(Object obj) {

        if (obj == null) { return false; }
        if (obj == this) { return true; }
        if (obj.getClass() != getClass()) {
            return false;
        }
        Tuple rhs = (Tuple) obj;
        return new EqualsBuilder().append(this.x, rhs.x).append(this.y, rhs.y).isEquals();
    }

    @Override
    public int hashCode() {

        return new HashCodeBuilder().append(x).append(y).toHashCode();
    }

    @Override
    public String toString() {

        return new ToStringBuilder(this).append("x", x).append("y", y).toString();
    }
}

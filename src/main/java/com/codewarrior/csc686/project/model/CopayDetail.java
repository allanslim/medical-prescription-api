package com.codewarrior.csc686.project.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class CopayDetail {

    public String serviceType;
    public String formulaGenericCopay;
    public String nonFormulaGenericCopay;
    public String formulaBrandCopay;
    public String nonFormulaBrandCopay;


    public CopayDetail(String serviceType, String formulaGenericCopay, String nonFormulaGenericCopay, String formulaBrandCopay, String nonFormulaBrandCopay) {


        this.serviceType = serviceType;
        this.formulaGenericCopay = formulaGenericCopay;
        this.nonFormulaGenericCopay = nonFormulaGenericCopay;
        this.formulaBrandCopay = formulaBrandCopay;
        this.nonFormulaBrandCopay = nonFormulaBrandCopay;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null) { return false; }
        if (obj == this) { return true; }
        if (obj.getClass() != getClass()) {
            return false;
        }
        CopayDetail rhs = (CopayDetail) obj;
        return new EqualsBuilder().append(this.serviceType, rhs.serviceType).append(this.formulaGenericCopay, rhs.formulaGenericCopay).append(this.nonFormulaGenericCopay, rhs.nonFormulaGenericCopay).append(this.formulaBrandCopay, rhs.formulaBrandCopay).append(this.nonFormulaBrandCopay, rhs.nonFormulaBrandCopay).isEquals();
    }

    @Override
    public int hashCode() {

        return new HashCodeBuilder().append(serviceType).append(formulaGenericCopay).append(nonFormulaGenericCopay).append(formulaBrandCopay).append(nonFormulaBrandCopay).toHashCode();
    }
}

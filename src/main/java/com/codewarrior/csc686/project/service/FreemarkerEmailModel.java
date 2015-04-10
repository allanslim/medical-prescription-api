package com.codewarrior.csc686.project.service;


import org.apache.commons.lang3.builder.HashCodeBuilder;

public final class FreemarkerEmailModel {
    public static final String VERIFICATION_TEMPLATE = "verification.ftl";


    private String name;
    private String toEmail;
    private String fromEmail;
    private String url;
    private String subject;
    private String templateName;

    private int hashCode;

    public String getName() { return name; }

    public String getToEmail() { return toEmail; }

    public String getFromEmail() { return fromEmail; }

    public String getUrl() { return url; }

    public String getSubject() { return subject; }

    public String getTemplateName() { return templateName; }



    private FreemarkerEmailModel() {}

    private FreemarkerEmailModel(Builder builder) {

        this.name = builder.name;
        this.toEmail = builder.toEmail;
        this.fromEmail = builder.fromEmail;
        this.url = builder.url;
        this.subject = builder.subject;
        this.templateName = builder.templateName;
    }


    public int hashCode() {

        if (hashCode == 0) {
            hashCode = new HashCodeBuilder(17, 37).
                    append(name).
                    append(toEmail).
                    append(fromEmail).
                    append(url).
                    append(templateName).
                    append(subject).
                    toHashCode();
        }
        return hashCode;
    }


    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FreemarkerEmailModel freemarkerEmailModel = (FreemarkerEmailModel) o;

        if (name != null ? !name.equals(freemarkerEmailModel.name) : freemarkerEmailModel.name != null) return false;
        if (toEmail != null ? !toEmail.equals(freemarkerEmailModel.toEmail) : freemarkerEmailModel.toEmail != null)
            return false;
        if (fromEmail != null ? !fromEmail.equals(freemarkerEmailModel.fromEmail) : freemarkerEmailModel.fromEmail != null)
            return false;
        if (url != null ? !url.equals(freemarkerEmailModel.url) : freemarkerEmailModel.url != null) return false;
        if (subject != null ? !subject.equals(freemarkerEmailModel.subject) : freemarkerEmailModel.subject != null)
            return false;
        if (templateName != null ? !templateName.equals(freemarkerEmailModel.templateName) : freemarkerEmailModel.templateName != null)
            return false;

        return true;
    }

    public static class Builder {
        private String name;
        private String toEmail;
        private String fromEmail;
        private String url;
        private String subject;
        private String templateName;

        public Builder setName(String name) {

            this.name = name;
            return this;
        }

        public Builder setToEmail(String toEmail) {

            this.toEmail = toEmail;
            return this;
        }

        public Builder setFromEmail(String fromEmail) {

            this.fromEmail = fromEmail;
            return this;
        }

        public Builder setUrl(String url) {

            this.url = url;
            return this;
        }

        public Builder setSubject(String subject) {

            this.subject = subject;
            return this;
        }

        public Builder setTemplateName(String templateName) {

            this.templateName = templateName;
            return this;
        }


        public Builder() {}

        public Builder(FreemarkerEmailModel freemarkerEmailModel) {

            this.name = freemarkerEmailModel.name;
            this.toEmail = freemarkerEmailModel.toEmail;
            this.fromEmail = freemarkerEmailModel.fromEmail;
            this.url = freemarkerEmailModel.url;
            this.subject = freemarkerEmailModel.subject;
            this.templateName = freemarkerEmailModel.templateName;
        }

        public FreemarkerEmailModel build() { return new FreemarkerEmailModel(this); }

    }
}
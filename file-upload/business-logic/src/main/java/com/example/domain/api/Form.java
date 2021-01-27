package com.example.domain.api;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Form {
    @NotEmpty(message = "name is not allowed to be empty")
    @Size(min=2, message="name should have at least 2 characters")
    private String name;

    @NotNull(message = "attachment is missing")
    private byte[] attachment;

    public Form() {
        // required for (de)serialization
    }

    public Form(String name, byte[] attachment) {
        this.name = name;
        this.attachment = attachment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getAttachment() {
        return attachment;
    }

    public void setAttachment(byte[] attachment) {
        this.attachment = attachment;
    }
}

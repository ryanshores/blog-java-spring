package com.ryanshores.blog.payloads;

import lombok.Data;

@Data
public class LoginDto {
    private String email;
    private String password;
}

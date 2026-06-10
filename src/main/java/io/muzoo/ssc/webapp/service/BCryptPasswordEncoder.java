package io.muzoo.ssc.webapp.service;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class BCryptPasswordEncoder implements PasswordEncoder {

    private final int cost;

    public BCryptPasswordEncoder(int cost) {
        this.cost = cost;
    }


    @Override
    public String encode(String rawPassword) {
        return BCrypt.withDefaults().hashToString(cost, rawPassword.toCharArray());
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return BCrypt.verifyer().verify(rawPassword.toCharArray(), encodedPassword).verified;
    }
}

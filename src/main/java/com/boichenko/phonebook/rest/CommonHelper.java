package com.boichenko.phonebook.rest;

import java.util.Collections;
import java.util.Map;

class CommonHelper {

    private CommonHelper() {
    }

    static final Map<String, Object> SUCCESS_RESPONSE = Collections.singletonMap("success", true);

    static final Map<String, Object> UNSUCCESSFUL_RESPONSE = Collections.singletonMap("success", false);

}

package com.nordstrom.ds.autoerroretry.service;

import java.util.List;

public interface Publisher {

    public void publish(final String endpoint, final List<String> messages);

}

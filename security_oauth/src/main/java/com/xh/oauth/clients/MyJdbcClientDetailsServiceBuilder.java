package com.xh.oauth.clients;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.builders.ClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.builders.JdbcClientDetailsServiceBuilder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.util.HashSet;
import java.util.Set;

/**
 * author  Xiao Hong
 * date  2021/7/14 22:59
 * description
 */
public class MyJdbcClientDetailsServiceBuilder extends ClientDetailsServiceBuilder<MyJdbcClientDetailsServiceBuilder> {

    private Set<ClientDetails> clientDetails = new HashSet<>();

    private DataSource dataSource;

    private PasswordEncoder passwordEncoder; // for writing client secrets

    private MyClientDetailsService myClientDetailsService;

    @Override
    protected void addClient(String clientId, ClientDetails value) {
        clientDetails.add(value);
    }

    @Override
    protected ClientDetailsService performBuild() {
        Assert.state(dataSource != null, "You need to provide a DataSource");
        if (passwordEncoder != null) {
            // This is used to encode secrets as they are added to the database (if it isn't set then the user has top
            // pass in pre-encoded secrets)
            myClientDetailsService.setPasswordEncoder(passwordEncoder);
        }
        for (ClientDetails client : clientDetails) {
            myClientDetailsService.addClientDetails(client);
        }
        return myClientDetailsService;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public void setMyClientDetailsService(MyClientDetailsService myClientDetailsService) {
        this.myClientDetailsService = myClientDetailsService;
    }
}

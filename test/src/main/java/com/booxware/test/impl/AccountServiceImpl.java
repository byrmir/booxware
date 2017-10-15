/*
 * Copyright 2017 Andrey Alexeenko <Andrey.Alexeenko at t-systems.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.booxware.test.impl;

import com.booxware.test.Account;
import com.booxware.test.AccountServiceException;
import com.booxware.test.AccountServiceInterface;
import com.booxware.test.PersistenceInterface;
import com.booxware.test.WrapExceptions;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Optional;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import static org.springframework.util.Assert.noNullElements;
import static org.springframework.util.Assert.notNull;
import static org.springframework.util.Assert.isTrue;
import static org.springframework.util.Assert.isNull;

/**
 * Account Service implementation, do all necessary jobs, provided by interface
 * contract.
 * #hasLoggedInSince method has been modified, because actual service cannot
 * hold user state.
 * <p>
 *
 * @author Andrey Alexeenko <Andrey.Alexeenko at t-systems.com>
 */
@Service
public class AccountServiceImpl implements AccountServiceInterface {

    /**
     * Logger, shows some debug info.
     */
    protected static final org.slf4j.Logger LOGGER = LoggerFactory
            .getLogger(AccountServiceImpl.class);
    /**
     * Repository service.
     */
    @Autowired
    private PersistenceInterface persistence;
    /**
     * String constants for message templates.
     */
    private static final String NOT_REGISTERED_MSG
                                        = "Account with name {0} is "
                                                  + "not registered within system.",
            ALREADY_REGISTERED_MSG = "Account with name {0} is "
                                             + "already registered.",
            EMPTY_PARAMETERS_MSG = "Service is not accepting an "
                                           + "empty parameters.",
            WRONG_PASSWORD_MSG = "Wrong password.";

    /**
     * {@inheritDoc }
     *
     * @param username
     * @param password
     *
     * @return
     */
    @Override
    @WrapExceptions(type = AccountServiceException.class)
    public Account login(String username, String password) {
        noNullElements(new Object[]{username, password},
                EMPTY_PARAMETERS_MSG);
        Account account = persistence.findByName(username);
        notNull(account, MessageFormat
                .format(NOT_REGISTERED_MSG, username));
        isTrue(checkPwd(password, account), WRONG_PASSWORD_MSG);
        Date now = new Date(System.currentTimeMillis());
        LOGGER.debug("User {} logged in at {}.", username, now);
        account.setLastLogin(now);
        return persistence.save(account);
    }

    /**
     * {@inheritDoc }
     *
     * @param username
     * @param email
     * @param password
     *
     * @return
     */
    @Override
    @WrapExceptions(type = AccountServiceException.class)
    public Account register(String username, String email, String password) {
        noNullElements(new Object[]{username, email, password},
                EMPTY_PARAMETERS_MSG);
        isNull(persistence.findByName(username), MessageFormat
                .format(ALREADY_REGISTERED_MSG, username));
        Account account = new Account(username, email);
        genPwdHash(password, account);
        account = persistence.save(account);
        LOGGER.debug("Registered new Account entity {}.", account);
        return account;
    }

    /**
     * {@inheritDoc }
     *
     * @param username
     */
    @Override
    @WrapExceptions(type = AccountServiceException.class)
    public void deleteAccount(String username) {
        notNull(username, EMPTY_PARAMETERS_MSG);
        Account account = persistence.findByName(username);
        notNull(account, MessageFormat
                .format(NOT_REGISTERED_MSG, username));
        persistence.delete(account);
        LOGGER.debug("Deleted Account enetity {}.", account);
    }

    /**
     * {@inheritDoc
     *
     * @param username
     * @param date
     *
     * @return
     */
    @Override
    @WrapExceptions(type = AccountServiceException.class)
    public boolean hasLoggedInSince(String username, Date date) {
        noNullElements(new Object[]{username, date},
                EMPTY_PARAMETERS_MSG);
        Account account = persistence.findByName(username);
        notNull(account, MessageFormat
                .format(NOT_REGISTERED_MSG, username));
        return Optional.ofNullable(account.getLastLogin())
                .map(d -> !d.before(date))
                .orElse(Boolean.FALSE);
    }

    /**
     * Generates new password hash and store in Account entity.
     *
     * @param passwd
     * @param account
     */
    private void genPwdHash(String passwd, Account account) {
        String salt = BCrypt.gensalt();
        String pwdHash = BCrypt.hashpw(passwd, salt);
        account.setSalt(salt);
        account.setEncryptedPassword(pwdHash.getBytes(Charset.forName("UTF8")));
    }

    /**
     * Check if received password could be compared to Account's hashed
     * password.
     *
     * @param passwd
     * @param account
     *
     * @return
     */
    private boolean checkPwd(String passwd, Account account) {
        String checkHash = BCrypt.hashpw(passwd, account.getSalt());
        String accountHash = new String(account.getEncryptedPassword(),
                Charset.forName("UTF8"));
        return accountHash.equals(checkHash);
    }

}

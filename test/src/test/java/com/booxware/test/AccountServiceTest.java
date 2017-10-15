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
package com.booxware.test;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import java.util.Date;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Andrey Alexeenko <Andrey.Alexeenko at t-systems.com>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@TestExecutionListeners({
    TransactionalTestExecutionListener.class,
    DependencyInjectionTestExecutionListener.class,
    DbUnitTestExecutionListener.class
})
@Transactional
@DatabaseSetup("classpath:data/DataSet.xml")
public class AccountServiceTest {

    @Autowired
    AccountServiceInterface accountService;

    String checkUsername;
    String checkEmail;
    String checkPassword;
    Date checkDate;

    @BeforeClass
    public static void setLogger() {
        System.setProperty("log4j.configurationFile", "log4j.xml");
    }

    @Before
    public void setUp() {
        checkUsername = "test_0";
        checkEmail = "test_0@booxware.test";
        checkPassword = "test000";
        checkDate = new Date();
    }

    @After
    public void tearDown() {

    }

    @Test
    public void accountValidLoginTest() {
        String uname = "test_1";
        String password = "test123";
        Account account = accountService.login(uname, password);
        assertNotNull(account);
        assertNotNull(account.getLastLogin());
    }

    @Test
    public void accountNotValidLoginTest() {
        String uname = "test_1";
        String password = "test1231";
        Exception ex = null;
        try {
            accountService.login(uname, password);
        } catch (Exception e) {
            ex = e;
        }
        assertNotNull(ex);
        assertEquals(AccountServiceException.class, ex.getClass());
        assertEquals("Wrong password.", ex.getMessage());
    }

    @Test
    public void registerExistentAccount() {
        String uname = "test_2";
        String password = "test123";
        Exception ex = null;
        try {
            accountService.register(uname, "e@mai.il", password);
        } catch (Exception e) {
            ex = e;
        }
        assertNotNull(ex);
        assertEquals(AccountServiceException.class, ex.getClass());
        assertEquals("Account with name test_2 is already registered.",
                ex.getMessage());
    }

    @Test
    public void checkLoggedInSince() {
        String uname = "test_3";
        String password = "test123";
        boolean loggedIn = accountService
                .hasLoggedInSince(uname, checkDate);
        assertFalse(loggedIn);
        accountService.login(uname, password);
        loggedIn = accountService
                .hasLoggedInSince(uname, checkDate);
        assertTrue(loggedIn);

    }

    @Test
    public void registerAccountWithWrongEmail() {
        String uname = "test_4";
        String password = "test1234";
        Exception ex = null;
        try {
            accountService.register(uname, "dfsdf234ma432", password);
        } catch (Exception e) {
            ex = e;
        }
        assertNotNull(ex);
        assertEquals(AccountServiceException.class, ex.getClass());
        assertEquals("[must be a well-formed email address]",
                ex.getMessage());
    }

    @Test
    public void createCheckDeleteAccount() {
        Account checkAccount = accountService
                .register(checkUsername, checkEmail, checkPassword);
        assertNotNull(checkAccount);
        assertNotNull(checkAccount.getId());
        assertNotNull(checkAccount.getUsername());
        assertNotNull(checkAccount.getEmail());
        assertNotNull(checkAccount.getSalt());
        assertNotNull(checkAccount.getEncryptedPassword());
        assertNull(checkAccount.getLastLogin());
        checkAccount = accountService.login(checkUsername, checkPassword);
        assertNotNull(checkAccount);
        assertNotNull(checkAccount.getLastLogin());
        accountService.deleteAccount(checkUsername);
        Exception ex = null;
        try {
            accountService.login(checkUsername, checkPassword);
        } catch (Exception e) {
            ex = e;
        }
        assertNotNull(ex);
        assertEquals(AccountServiceException.class, ex.getClass());
        assertEquals("Account with name " + checkUsername
                             + " is not registered within system.",
                ex.getMessage());
    }

}

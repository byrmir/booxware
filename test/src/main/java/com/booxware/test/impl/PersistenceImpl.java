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
import com.booxware.test.PersistenceInterface;
import com.booxware.test.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Still simple, because it just wraps actual JPA repository.
 * #save method has been modified, to avoid unnecessary additional entity
 * fetching, which is required updated state representation.
 * #findById method is redundant in case of requirements.
 *
 * @author Andrey Alexeenko <Andrey.Alexeenko at t-systems.com>
 */
@Repository
@Transactional(readOnly = true)
public class PersistenceImpl implements PersistenceInterface {

    /**
     * JPA repository, which provides an access to Account entity.
     */
    @Autowired
    private AccountRepository repository;

    /**
     * {@inheritDoc }
     *
     * @param account
     *
     * @return
     */
    @Override
    @Transactional
    public Account save(Account account) {
        return repository.saveAndFlush(account);
    }

    /**
     * {@inheritDoc }
     *
     * @param id
     *
     * @return
     */
    @Override
    public Account findById(Long id) {
        return repository.getOne(id);
    }

    /**
     * {@inheritDoc }
     *
     * @param name
     *
     * @return
     */
    @Override
    public Account findByName(String name) {
        return repository.findByUsername(name);
    }

    /**
     * {@inheritDoc }
     *
     * @param account
     */
    @Override
    @Transactional
    public void delete(Account account) {
        repository.delete(account);
    }

}

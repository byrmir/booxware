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
package com.booxware.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.booxware.test.Account;

/**
 * Account repository template.
 *
 * @author Andrey Alexeenko <Andrey.Alexeenko at t-systems.com>
 */
public interface AccountRepository extends JpaRepository<Account, Long> {

    /**
     * Used to find Account entity object by USERNAME column value.
     *
     * @param username
     *
     * @return
     */
    Account findByUsername(String username);

}

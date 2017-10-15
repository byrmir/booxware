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
package com.booxware.test.util;

import com.booxware.test.AccountServiceException;
import java.util.Optional;
import javax.annotation.PostConstruct;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * Exception bean factory, used to define Exception beans for business methods.
 *
 * @author Andrey Alexeenko <Andrey.Alexeenko at t-systems.com>
 */
@Configuration
public class ExceptionFactory {

    private static ExceptionFactory INSTANCE;

    @PostConstruct
    private void init() {
        INSTANCE = this;
    }

    public static final ExceptionFactory get() {
        return INSTANCE;
    }

    /**
     * Searches for Exception bean of particular type within Spring context, and
     * makes of its copy with defined message.
     *
     * @param <T>
     * @param type
     * @param message
     *
     * @return
     */
    public <T extends Throwable> T build(Class<T> type, String message) {
        T ex = ContextLookupUtil.get()
                .getBeanOfTypeAndArgs(type, message);
        return Optional.ofNullable(ex).orElse(null);
    }

    @Bean
    @Scope("prototype")
    AccountServiceException AccountServiceException(String message) {
        return new AccountServiceException(message);
    }

}

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

import java.util.Optional;
import javax.annotation.PostConstruct;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

/**
 * Utility class used to lookup Spring context.
 *
 * @author Andrey Alexeenko <Andrey.Alexeenko at t-systems.com>
 */
@Configuration
public class ContextLookupUtil {

    protected static final org.slf4j.Logger LOGGER = LoggerFactory
            .getLogger(ContextLookupUtil.class);

    @Autowired
    private ApplicationContext applicationContext;

    private static ContextLookupUtil INSTANCE;

    @PostConstruct
    private void init() {
        INSTANCE = this;
    }

    public static final ContextLookupUtil get() {
        return INSTANCE;
    }

    /**
     * Check if bean of required type presents within system. Lookup by name to
     * avoid context exceptions.
     *
     * @param <T>
     * @param beanType
     *
     * @return
     */
    public <T> String lookupBeanName(Class<T> beanType) {
        String beanName = null;
        for (String object : applicationContext.
                getBeanNamesForType(beanType)) {
            beanName = object;
            break;
        }
        return beanName;
    }

    /**
     * Required bean, will be created with requested arguments.
     *
     * @param <T>
     * @param beanType
     * @param args
     *
     * @return
     */
    public <T> T getBeanOfTypeAndArgs(Class<T> beanType, Object... args) {
        String beanName = lookupBeanName(beanType);
        return Optional.ofNullable(beanName)
                .map(n -> beanType
                .cast(applicationContext.getBean(n, args)))
                .orElse(null);
    }

}

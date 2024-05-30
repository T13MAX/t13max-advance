package com.t13max.game.run;

import com.t13max.game.config.BaseConfig;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApplicationConfig {

    Class<? extends BaseConfig> configClazz();
}

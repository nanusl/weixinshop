package com.github.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author nanusl
 * @version V1.0
 * @Description 初始化操作
 * @date 2021-02-03 20:21
 */
@Slf4j
@Component
public class SystemInitConfiguration implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        System.out.println("SystemInitConfiguration.run......................");
    }
}

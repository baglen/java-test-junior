package com.java.test.junior.load.service;

/**
 * @author artiom.spac
 * @version java-test-junior
 * @apiNote 09.03.2023
 */
public interface EntityLoader {

    /**
     * @param path file path to be imported
     * @param userId id of the entities owner
     */
    void load(String path, Long userId);
}

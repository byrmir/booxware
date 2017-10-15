package com.booxware.test;

/**
 * Persistence can be very simple, for example an in memory hash map.
 * <p>
 */
public interface PersistenceInterface {

    /**
     * Self explained method.
     *
     * @param account
     *
     * @return
     */
    public Account save(Account account);

    /**
     * Self explained method.
     *
     * @param id
     *
     * @return
     */
    public Account findById(Long id);

    /**
     * Self explained method.
     *
     * @param name
     *
     * @return
     */
    public Account findByName(String name);

    /**
     * Self explained method.
     *
     * @param account
     */
    public void delete(Account account);

}

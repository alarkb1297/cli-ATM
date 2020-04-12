package com.company;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ATMAccountImplTest {

    ATMAccountImpl account;

    @BeforeEach
    void setup() {
        this.account = new ATMAccountImpl(1, 1);
    }

    @Test
    void authorize() {
        // Make sure auth works
        assertTrue(account.authorize(1, 1));
        // Make sure auth fails
        assertFalse(account.authorize(1, 2));
        assertFalse(account.authorize(2, 2));
        assertFalse(account.authorize(2, 1));
    }

    @Test
    void withdraw() {
        assertEquals(account.history.size(), 0);
        // Withdrawal works even when balance is not enough, even though it'll be overdrawn
        assertEquals(account.withdraw(100), -105);
        assertEquals(account.history.size(), 1);
        // Deposit 105
        assertEquals(account.deposit(205), 100);

        // Withdraw 20
        assertEquals(account.withdraw(20), 80);
        assertEquals(account.history.size(), 3);
    }

    @Test
    void deposit() {
        assertEquals(account.history.size(), 0);
        // Make sure deposits work
        assertEquals(account.deposit(100), 100);
        assertEquals(account.history.size(), 1);
    }

    @Test
    void history() {
        assertEquals(account.history.size(), 0);
        // Make sure histories update appropriately
        account.deposit(100);
        assertEquals(account.history.size(), 1);
        account.withdraw(100);
        assertEquals(account.history.size(), 2);
    }
}
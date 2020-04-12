package com.company;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ATMImplTest {

    ATMImpl atm;

    @BeforeEach
    void setup() {
        atm = new ATMImpl();
    }

    @Test
    void openAccount() {
        // Making sure the account gets open
        assertEquals(this.atm.accounts.size(), 0);
        ATMAccount atmAccount = atm.openAccount(1, 1);
        assertEquals(this.atm.accounts.size(), 1);
        assertNotNull(atmAccount);
        assertEquals(atmAccount.getBalance(), 0);
        assertEquals(atmAccount.history().size(), 0);

        // Making sure account with the same id doesnt get opened twice
        atm.openAccount(1, 2);
        assertEquals(this.atm.accounts.size(), 1);

        // Making sure multiple accounts can be open
        atm.openAccount(2, 2);
        assertEquals(this.atm.accounts.size(), 2);
    }

    @Test
    void authorize() {
        assertNull(this.atm.authorizedAccount);
        // Making sure account that's not open doesn't get authorized
        this.atm.authorize(1, 1);

        // Open the account, authorize it with wrong password, make sure it's not the authorized
        ATMAccount atmAccount = atm.openAccount(1, 1);
        this.atm.authorize(1, 2);
        assertNull(this.atm.authorizedAccount);

        // Authorize with correct password, make sure it's authorized
        this.atm.authorize(1, 1);
        assertEquals(this.atm.authorizedAccount, atmAccount);

        // Try to authorize another account while this one is still logged in, make sure it doesn't
        ATMAccount atmAccount1 = atm.openAccount(2, 2);
        ATMAccount authorizeAccount = this.atm.authorize(2, 2);
        assertEquals(authorizeAccount, atmAccount);
        assertNotEquals(authorizeAccount, atmAccount1);
    }

    @Test
    void withdraw() {
        // Make sure can't withdraw without authorization
        ATMAccount atmAccount = atm.openAccount(1, 1);
        assertEquals(atm.withdraw(100), 0);

        // Auth the account withdraw 5, fails because not a multiple of 20
        this.atm.authorize(1, 1);
        assertEquals(atm.withdraw(5), 0);

        // Withdraw $100, will over draw the account and balance will be -$105 because of $5 charge
        assertEquals(atm.withdraw(100), -105);
        assertEquals(atm.amountInAtm, 9900);

        // Cant withdraw more because account is overdrawn
        assertEquals(atm.withdraw(100), -105);
        assertEquals(atm.amountInAtm, 9900);

        // Deposit money so we can test for more withdrawal logic
        atm.deposit(105);
        assertEquals(atm.amountInAtm, 10005);

        // Withdraw more than what's in the ATM, only will process what's in the ATM
        assertEquals(atm.withdraw(20000), -10010);
        assertEquals(atm.amountInAtm, 0);

        // Normal case
        atm.deposit(10210);
        assertEquals(atm.withdraw(100), 100);
    }

    @Test
    void deposit() {
        // Make sure can't deposit without authorization
        ATMAccount atmAccount = atm.openAccount(1, 1);
        assertEquals(atm.deposit(100), 0);

        // Auth the account and make sure we can deposit, and that the money in the atm udpates
        this.atm.authorize(1, 1);
        assertEquals(this.atm.deposit(100), 100);
        assertEquals(this.atm.amountInAtm, 10100);
    }

    @Test
    void balance() {
        // Make sure can't check balance without authorization
        ATMAccount atmAccount = atm.openAccount(1, 1);
        atmAccount.deposit(100);
        assertEquals(atm.balance(), 0);

        // Auth the account and make sure we can deposit, and that the money in the atm udpates
        this.atm.authorize(1, 1);
        assertEquals(this.atm.deposit(100), this.atm.balance());
        assertEquals(this.atm.amountInAtm, 10100);
    }

    @Test
    void history() {
        // Make sure can't get history without authorization
        ATMAccount atmAccount = atm.openAccount(1, 1);
        atmAccount.deposit(200);
        atmAccount.withdraw(100);
        assertEquals(atm.history().size(), 0);

        this.atm.authorize(1, 1);
        assertEquals(this.atm.history(), atmAccount.history());
    }

    @Test
    void logout() {
        // Can't log out without being logged in.
        ATMAccount atmAccount = atm.openAccount(1, 1);
        assertFalse(this.atm.logout());

        // Log in
        assertEquals(this.atm.authorize(1, 1), this.atm.authorizedAccount);

        // Logout
        assertTrue(this.atm.logout());
        assertNull(this.atm.authorizedAccount);
    }
}
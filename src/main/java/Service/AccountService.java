package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    public AccountDAO accountDAO;

    // no-args constructor for accountService that creates an AccountDAO
    public AccountService() {
        accountDAO = new AccountDAO();
    }

    // constructor if an AccountDAO is provided
    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    // use accountDAO.registerAccount() to add a new account
    public Account addAccount(Account account) {
        if (account.getUsername().trim().isEmpty() || account.getUsername() == null || account.getPassword().length() < 4 || account.getPassword() == null) {
            return null;
        }

        Account existingAccount = accountDAO.getAccountByUsername(account.getUsername());
        if (existingAccount != null) {
            return null;
        }

        return accountDAO.registerAccount(account);
    }

    // use accountDAO.getAccountByUsername to compare user/pass, login function
    public Account loginAccount(Account account) {
        Account existingAccount = accountDAO.getAccountByUsername(account.getUsername());

        if (existingAccount != null) {
            if (existingAccount.getUsername().equals(account.getUsername()) && existingAccount.getPassword().equals(account.getPassword())) {
                return existingAccount;
            }
        }

        return null;
    }
}

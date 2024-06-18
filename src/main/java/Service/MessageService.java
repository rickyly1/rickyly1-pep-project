package Service;

import DAO.MessageDAO;
import DAO.AccountDAO;
import Model.Message;
import Model.Account;

import java.util.List;

public class MessageService {
    public MessageDAO messageDAO;
    public AccountDAO accountDAO;

    // no-args constructor
    public MessageService() {
        messageDAO = new MessageDAO();
        accountDAO = new AccountDAO();
    }

    // constructor if MessageDAO is provided
    public MessageService(MessageDAO messageDAO, AccountDAO accountDAO) {
        this.messageDAO = messageDAO;
        this.accountDAO = accountDAO;
    }

    // use messageDAO.createMessage to add a new message
    public Message addMessage(Message message) {
        Account actualAccount = accountDAO.getAccountByID(message.getPosted_by());

        if (message.getMessage_text() != null && !message.getMessage_text().trim().isEmpty() && message.getMessage_text().length() <= 255 && actualAccount != null) {
            return messageDAO.createMessage(message);
        }

        return null;
    }

    // use messageDAO.getAllMessages to get a List<Messages> of all table entries
    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    // use messageDAO.getMessageByID()
    public Message getMessageByID(int message_id) {
        return messageDAO.getMessageByID(message_id);
    }

    // use messageDAO.deleteMessageByID() and receive the deleted message
    public Message deleteMessageByID(int message_id) {
        return messageDAO.deleteMessageByID(message_id);
    }

    // use messageDAO.updateMessageText w/ target message_id and new message_text
    public Message updateMessageText(int message_id, String message_text) {
        Message originalMessage = messageDAO.getMessageByID(message_id);

        if (originalMessage != null && !message_text.trim().isEmpty() && message_text != null && message_text.length() <= 255) {
            return messageDAO.updateMessageText(message_id, message_text);
        }
        return null;
    }

    // call messageDAO.getAllMessagesByUser w/ a posted_by id
    public List<Message> getAllMessagesByUser(int posted_by) {
        return messageDAO.getAllMessagesByUser(posted_by);
    }
}

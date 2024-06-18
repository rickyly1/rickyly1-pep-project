package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }


    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        //app.get("example-endpoint", this::exampleHandler);

        // MY IMPLEMENTATIONS HERE
        app.post("/register", this::registerUserHandler); // done
        app.post("/login", this::userLoginHandler); // done
        app.post("/messages", this::createMessageHandler); // done
        app.get("/messages", this::getAllMessagesHandler); // done
        app.get("/messages/{message_id}", this::getMessageByIDHandler); // done
        app.delete("/messages/{message_id}", this::deleteMessageHandler); // done
        app.patch("/messages/{message_id}", this::updateMessageHandler); // in progress
        app.get("/accounts/{account_id}/messages", this::getUserMessagesHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     
    private void exampleHandler(Context context) {
        context.json("sample text");
    }*/

    private void registerUserHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account addedAccount = accountService.addAccount(account);

        if(addedAccount != null) {
            context.status(200);
            context.json(mapper.writeValueAsString(addedAccount));
        } else {
            context.status(400);
        }
    }

    private void userLoginHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account loggedInAccount = accountService.loginAccount(account);

        if(loggedInAccount != null) {
            context.status(200);
            context.json(mapper.writeValueAsString(loggedInAccount));
        } else {
            context.status(401);
        }
    }

    private void createMessageHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);

        Message createdMessage = messageService.addMessage(message);

        if (createdMessage != null) {
            context.status(200);
            context.json(mapper.writeValueAsString(createdMessage));
        } else {
            context.status(400);
        }
    }

    private void getAllMessagesHandler(Context context) {
        List<Message> allMessages = messageService.getAllMessages();
        context.status(200);
        context.json(allMessages);
    }

    private void getMessageByIDHandler(Context context) {
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message message = messageService.getMessageByID(message_id);

        if (message != null) {
            context.json(message);
        } else {
            context.json("");
        }
        context.status(200);
    }

    private void deleteMessageHandler(Context context) {
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message deletedMessage = messageService.deleteMessageByID(message_id);

        if (deletedMessage != null) {
            context.json(deletedMessage);
        } else {
            context.json("");
        }

        context.status(200);
    }

    private void updateMessageHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        JsonNode jsonNode = mapper.readTree(context.body());
        String message_text = jsonNode.get("message_text").asText();

        Message updatedMessage = messageService.updateMessageText(message_id, message_text);
        if (updatedMessage != null) {
            context.status(200);
            context.json(updatedMessage);
        } else {
            context.status(400);
        }
    }

    private void getUserMessagesHandler(Context context) {
        int posted_by = Integer.parseInt(context.pathParam("account_id"));
        List<Message> allMessagesByUser = messageService.getAllMessagesByUser(posted_by);
        context.status(200);
        context.json(allMessagesByUser);
    }
}
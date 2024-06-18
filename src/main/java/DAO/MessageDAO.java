package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {
    // NAME OF MESSAGE TABLE: "message"

    // @return ArrayList of all messages in table, List is empty if failed;
    public List<Message> getAllMessages() {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "SELECT * FROM message;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return messages;
    }

    // @returns Message back if successful, null returned on fail;
    public Message createMessage(Message mes) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, mes.getPosted_by());
            preparedStatement.setString(2, mes.getMessage_text());
            preparedStatement.setLong(3, mes.getTime_posted_epoch());
            
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                ResultSet rs = preparedStatement.getGeneratedKeys();
                if (rs.next()) {
                    int generated_message_id = rs.getInt(1);
                    return new Message(generated_message_id, mes.getPosted_by(), mes.getMessage_text(), mes.getTime_posted_epoch());
                }
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    // @returns all messages by a user, using the poster_id;
    public List<Message> getAllMessagesByUser(int poster_id) {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "SELECT * FROM message WHERE posted_by=?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, poster_id);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                Message mes = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));

                messages.add(mes);
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return messages;
    }

    // @returns single Message using message_id
    public Message getMessageByID(int message_id) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM message WHERE message_id=?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, message_id);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                Message mes = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));

                return mes;
            } 
            
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    // delete a Message using a message_id
    public Message deleteMessageByID(int message_id) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String selectMessageToDelete = "SELECT * FROM message WHERE message_id=?;";
            PreparedStatement selectPrepared = connection.prepareStatement(selectMessageToDelete);
            selectPrepared.setInt(1, message_id);
            ResultSet rs = selectPrepared.executeQuery();

            if (rs.next()) {
                Message mes = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));

                String deleteSql = "DELETE FROM message WHERE message_id=?;";
                PreparedStatement preparedStatement = connection.prepareStatement(deleteSql);
                preparedStatement.setInt(1, message_id);
                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows > 0) {
                    return mes;
                }
            }

        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    // update a Message's message_text using its message_id
    public Message updateMessageText(int mes_id, String mes_text) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "UPDATE message SET message_text=? WHERE message_id=?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, mes_text);
            preparedStatement.setInt(2, mes_id);

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                String selectSql = "SELECT * FROM message WHERE message_id=?;";
                PreparedStatement selectStatement = connection.prepareStatement(selectSql);
                selectStatement.setInt(1, mes_id);
                ResultSet rs = selectStatement.executeQuery();

                if (rs.next()) {
                    Message updatedMessage = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                    return updatedMessage;
                }
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }
}

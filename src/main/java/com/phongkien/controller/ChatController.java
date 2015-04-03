package com.phongkien.controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.phongkien.model.ActiveUser;
import com.phongkien.model.MessageModel;
import com.phongkien.model.StatusModel;
import com.phongkien.utils.UtilsFunctions;

@RestController
public class ChatController {
	private static final String SQL_GET_MESSAGES = "select from_user, to_user, message from pgpchat.messages where to_user = ?";
	private static final String SQL_DEL_MESSAGES = "delete from pgpchat.messages where to_user = ?";
	private static final String SQL_INS_MESSAGE = "insert into pgpchat.messages(from_user, to_user, message) values (?,?,?)";
	@Autowired
	private SessionRegistry sessionRegistry;

	@Autowired
	private DataSource dataSource;

	private Logger logger = Logger.getLogger(ChatController.class);

	private void closeStatement(PreparedStatement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (Exception meh) {
			}
		}
	}

	@RequestMapping(value = "/user/activeUsers", method = { RequestMethod.GET })
	public @ResponseBody ArrayList<ActiveUser> getActiveUsers(
			HttpServletRequest request) {
		String name = request.getUserPrincipal().getName();
		ArrayList<ActiveUser> activeUsers = new ArrayList<ActiveUser>();

		List<Object> principals = sessionRegistry.getAllPrincipals();

		for (Object p : principals) {
			if (p instanceof User) {
				String userName = ((User) p).getUsername();
				if (!userName.equals(name)) {
					activeUsers.add(new ActiveUser(userName));
				}
			}
		}

		return activeUsers;
	}

	@RequestMapping(value = "/message/get", method = { RequestMethod.GET })
	public @ResponseBody ArrayList<MessageModel> getMessages(
			HttpServletRequest request) {
		ArrayList<MessageModel> messages = new ArrayList<MessageModel>();
		PreparedStatement stmt = null;

		try {
			String userName = request.getUserPrincipal().getName();
			stmt = dataSource.getConnection()
					.prepareStatement(SQL_GET_MESSAGES);
			stmt.setString(1, userName);

			if (stmt.execute()) {
				ResultSet rs = stmt.getResultSet();

				while (rs.next()) {
					MessageModel messageModel = new MessageModel();
					messageModel.setFrom(rs.getString(1));
					messageModel.setTo(rs.getString(2));
					messageModel.setMessage(rs.getString(3));
					messages.add(messageModel);
				}

				rs.close();
			}
		} catch (Exception ex) {
			logger.debug("Unable to get message", ex);
		} finally {
			closeStatement(stmt);
			stmt = null;
		}

		try {
			String userName = request.getUserPrincipal().getName();
			stmt = dataSource.getConnection()
					.prepareStatement(SQL_DEL_MESSAGES);
			stmt.setString(1, userName);
			stmt.execute();
		} catch (Exception ex) {
			logger.debug("Unable to get message", ex);
		} finally {
			closeStatement(stmt);
		}

		return messages;
	}

	@RequestMapping(value = "/message/send", method = { RequestMethod.POST })
	public @ResponseBody StatusModel sendMessages(
			@RequestBody MessageModel messageModel, HttpServletRequest request) {
		StatusModel status = new StatusModel();
		PreparedStatement stmt = null;

		try {
			String fromUser = request.getUserPrincipal().getName();
			String toUser = messageModel.getTo();
			String message = messageModel.getMessage();
			
			if (!UtilsFunctions.isNull(fromUser) && !UtilsFunctions.isNull(toUser) && !UtilsFunctions.isNull(message)) {
				stmt = dataSource.getConnection().prepareStatement(SQL_INS_MESSAGE);
				stmt.setString(1, fromUser);
				stmt.setString(2, toUser);
				stmt.setString(3, message);
				stmt.execute();
	
				if (stmt.getUpdateCount() > 0) {
					status.setStatusText("success");
				}
			}
		} catch (Exception ex) {
			logger.debug("Unable to send message", ex);
		} finally {
			closeStatement(stmt);
		}

		return status;
	}
}

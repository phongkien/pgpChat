package com.phongkien.controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.phongkien.model.StatusModel;
import com.phongkien.model.UserModel;
import com.phongkien.utils.Debug;
import com.phongkien.utils.UtilsFunctions;

@RestController
public class RegistrationController {
	@Autowired
	private DataSource dataSource;
	private static String SQL_CHECK_USERNAME = "select count(*) from pgpchat.users where username = ?";
	private static String SQL_CHECK_EMAIL = "select count(*) from pgpchat.users where email = ?";
	private static String SQL_INS_USER = "insert into pgpchat.users(user_id, username, password, email, first_name, last_name, confirmed, verification_code) values(?,?,?,?,?,?,?,?)";
	private static String SQL_NEXT_USER = "select max(user_id) + 1 from pgpchat.users";
	private static String SQL_WHITE_LIST = "select count(*) from pgpchat.white_list where email = ?";
	private static String SQL_INS_USER_ROLE = "insert into pgpchat.user_role(user_id, role) values(?,?)";
	private static String SQL_DEL_USER = "delete * from pgpchat.users where user_id = ?";
	private static final Logger logger = Logger
			.getLogger(RegistrationController.class);

	// TODO: for now, default to Y, when email validation is implemented, change
	// to N
	private static String CONFIRMED_DEFAULT = "Y";

	@RequestMapping(value = "/register", method = { RequestMethod.POST,
			RequestMethod.GET })
	public @ResponseBody StatusModel register(@RequestBody UserModel user,
			HttpServletRequest request) {
		user.setVerificationStringURL(request.getRequestURL().toString()
				+ "/confirm?");
		StatusModel model = new StatusModel();
		String username = user.getUsername();
		String password = user.getPassword();
		String verifyPassord = user.getVerifyPassword();
		String email = user.getEmail();

		if (UtilsFunctions.isNull(username) || UtilsFunctions.isNull(password)
				|| UtilsFunctions.isNull(verifyPassord)
				|| UtilsFunctions.isNull(email)) {
			model.setStatusText("Missing one or more required fields.");
		} else if (!password.equals(verifyPassord)) {
			model.setStatusText("Password mis-matched.");
		}

		// check to see if username is already in used.
		if (checkUser(username, model) && checkEmail(email, model)) {
			model.setStatusText(createUser(user));
		}

		return model;
	}

	private boolean checkUser(String username, StatusModel model) {
		boolean isValid = false;
		boolean hasDbError = false;
		PreparedStatement stmt = null;

		try {
			stmt = dataSource.getConnection().prepareStatement(
					SQL_CHECK_USERNAME);
			stmt.setString(1, username);

			if (!stmt.execute()) {
				isValid = true;
			} else {
				ResultSet rs = stmt.getResultSet();
				rs.next();
				int i = rs.getInt(1);

				if (i == 0) {
					isValid = true;
				}

				rs.close();
			}
		} catch (SQLException e) {
			logger.debug("Error occurred checking user", e);
			model.setStatusText("Database error");
			hasDbError = true;
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception ex) {
				}
			}
		}

		if (!hasDbError && !isValid) {
			model.setStatusText("Username has already been used.");
		}

		return isValid;
	}

	private boolean checkEmail(String email, StatusModel model) {
		boolean isValid = false;
		boolean hasDbError = false;
		PreparedStatement stmt = null;

		try {
			stmt = dataSource.getConnection().prepareStatement(SQL_CHECK_EMAIL);
			stmt.setString(1, email);

			if (!stmt.execute()) {
				isValid = true;
			} else {
				ResultSet rs = stmt.getResultSet();
				rs.next();
				int i = rs.getInt(1);

				if (i == 0) {
					isValid = true;
				}

				rs.close();
			}
		} catch (SQLException e) {
			logger.debug("Error occurred checking email", e);
			model.setStatusText("Database error");
			hasDbError = true;
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception ex) {
				}
			}
		}

		if (!hasDbError && !isValid) {
			model.setStatusText("Email has already been used.");
		}

		return isValid;
	}

	private int getNextUserId() {
		int nextId = -1;
		PreparedStatement stmt = null;

		try {
			stmt = dataSource.getConnection().prepareStatement(SQL_NEXT_USER);

			if (stmt.execute()) {
				ResultSet rs = stmt.getResultSet();
				if (rs != null) {
					rs.next();
					nextId = rs.getInt(1);
					rs.close();
				}
			}

			// no users, so start with 1
			if (nextId < 1) {
				nextId = 1;
			}
		} catch (SQLException e) {
			logger.debug("Error occurred while trying to obtain next user id",
					e);
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception ex) {
				}
			}
		}

		return nextId;
	}

	private String createUser(UserModel user) {
		boolean success = false;
		String retVal = "Unable to create user";
		PreparedStatement stmt = null;
		int nextId = -1;

		if (!isWhiteList(user.getEmail())) {
			retVal = "Email not authorized.";
		} else {
			try {
				nextId = getNextUserId();
				if (Debug.ON) {
					logger.debug("User id: " + nextId);
				}
				if (nextId > 0) {
					user.setVerificationCode(UtilsFunctions.generateRandomKey(32));
					stmt = dataSource.getConnection().prepareStatement(
							SQL_INS_USER);
					stmt.setInt(1, nextId);
					stmt.setString(2, user.getUsername().trim());
					stmt.setString(3, user.getPassword());
					stmt.setString(4, user.getEmail());
					stmt.setString(5, user.getFirstName());
					stmt.setString(6, user.getLastName());
					stmt.setString(7, CONFIRMED_DEFAULT);
					stmt.setString(8, user.getVerificationCode());

					stmt.execute();
					if (stmt.getUpdateCount() > 0) {
						retVal = "User is successfully created";
						success = true;
					}

					stmt.close();

					if (Debug.ON) {
						logger.debug("User is created.");
					}
				}
			} catch (SQLException ex) {
				logger.debug("Error occurred creating user", ex);
				retVal = "Database error";
			} finally {
				if (stmt != null) {
					try {
						stmt.close();
					} catch (Exception ex) {
					}
				}
			}

			if (success) {
				success = false;
				try {
					stmt = dataSource.getConnection().prepareStatement(
							SQL_INS_USER_ROLE);
					stmt.setInt(1, nextId);
					stmt.setString(2, "ROLE_USER");

					stmt.execute();
					if (stmt.getUpdateCount() > 0) {
						success = true;
					}

					stmt.close();
					if (Debug.ON) {
						logger.debug("Role is created");
						user.setVerificationStringURL(user
								.getVerificationStringURL()
								+ "id="
								+ nextId
								+ "&key="
								+ user.getVerificationCode());
						
						sendMail(user);
					}
				} catch (SQLException ex) {
					logger.debug("Error occurred while creating role", ex);
					retVal = "Database error";
					deleteUser(String.valueOf(nextId));
					success= false;
				} finally {
					if (stmt != null) {
						try {
							stmt.close();
						} catch (Exception ex) {
						}
					}
				}
			}
		}

		if (!success) {
			retVal = "Unable to create user";
		}

		return retVal;
	}
	
	private void deleteUser(String userId) {
		PreparedStatement stmt = null;
		try {
			stmt = dataSource.getConnection().prepareStatement(SQL_DEL_USER);
			stmt.setString(1, userId);
			if (!stmt.execute()) {
				int u = stmt.getUpdateCount();
				if (u > 0) {
					logger.debug("Successfully deleted user id " + userId);
				}
			}
		} catch (Exception ex) {
			logger.debug("Unabl to deleete user id " + userId, ex);
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception ex){
					//whatevver
				}
			}
		}
	}

	private boolean isWhiteList(String email) {
		boolean retVal = false;
		PreparedStatement stmt = null;

		try {
			stmt = dataSource.getConnection().prepareStatement(SQL_WHITE_LIST);
			stmt.setString(1, email);

			if (stmt.execute()) {
				ResultSet rs = stmt.getResultSet();
				if (rs.next()) {
					int i = rs.getInt(1);
					if (i > 0) {
						retVal = true;
					}
				}

				rs.close();
			} else {
				retVal = true;
			}
		} catch (Exception ex) {
			logger.debug("Error occurred while checking whitelist", ex);
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception ex) {
				}
			}
		}

		return retVal;
	}

	private boolean sendMail(UserModel model) {
		boolean isSuccess = false;

		try {
			String firstName = model.getFirstName();
			String lastName = model.getLastName();
			Properties prop = new Properties();
			prop.setProperty("mail.smtp.host", "localhost"); // TODO

			StringBuilder body = new StringBuilder(
					String.format(
							"Greeting %s %s,\n\nThank you for your registration. Please click <a href=\"%s\">here</a> to confirm your email.\n\n",
							firstName, lastName));

			body.append("If that doesn't work, copy and paste the link below into your browser search bar.\n");
			body.append(model.getVerificationStringURL());

			Session session = Session.getDefaultInstance(prop);
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress("phongkien@gmail.com"));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					model.getEmail()));
			message.setSubject("pgpChat registration confirmation");
			message.setText(body.toString());

			Transport.send(message);
			isSuccess = true;
		} catch (Exception ex) {
			logger.debug("Fail to send mail", ex);
		}
		return isSuccess;
	}

}

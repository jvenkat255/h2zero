package synapticloop.sample.h2zero.mysql.inserter;

// - - - - thoughtfully generated by synapticloop h2zero - - - - 
//    with the use of synapticloop templar templating language
//              (java-create-inserter.templar)

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import synapticloop.h2zero.base.manager.mysql.ConnectionManager;
import synapticloop.sample.h2zero.mysql.model.util.Constants;

/**
 * This class contains the methods to insert new rows into the: 
 *   <code>sample.user</code>
 * table.  The fields that are available are as follows:
 * 
 * <ul>
 *  <li><code>id_user</code> (bigint)  <strong>NOT</strong> nullable  (PRIMARY)</li>
 *  <li><code>id_user_type</code> (bigint)  <strong>NOT</strong> nullable </li>
 *  <li><code>fl_is_alive</code> (boolean)  nullable </li>
 *  <li><code>num_age</code> (int)  <strong>NOT</strong> nullable </li>
 *  <li><code>nm_username</code> (varchar)  <strong>NOT</strong> nullable </li>
 *  <li><code>txt_address_email</code> (varchar)  <strong>NOT</strong> nullable </li>
 *  <li><code>txt_password</code> (varchar)  <strong>NOT</strong> nullable </li>
 *  <li><code>dtm_signup</code> (datetime)  nullable </li>
 * </ul>
 * 
 * @author synapticloop h2zero
 */

public class UserInserter {
	// the binder is unused in code, but will generate compile problems if this 
	// class is no longer referenced in the h2zero file. Just a nicety for
	// removing dead code
	@SuppressWarnings("unused")
	private static final String BINDER = Constants.USER_BINDER;

		private static final Logger LOGGER = LoggerFactory.getLogger(UserInserter.class);

	// static fields generated by synapticloop h2zero
	private static final String SQL_BUILTIN_INSERT_ALL = "insert into user(id_user, id_user_type, fl_is_alive, num_age, nm_username, txt_address_email, txt_password, dtm_signup)";
	private static final String SQL_BUILTIN_INSERT_VALUES = SQL_BUILTIN_INSERT_ALL + " values (?, ?, ?, ?, ?, ?, ?, ?)";
	// static inserter SQL generated from the user input

	private UserInserter() {}

	/**
	 * Insert a new User into the database utilising the passed in connection.
	 * 
	 * @param connection the connection to use for the database, this __MUST__ be 
	 *   closed by the calling function.
	 * @param idUser  maps to id_user
	 * @param idUserType  maps to id_user_type
	 * @param flIsAlive  maps to fl_is_alive
	 * @param numAge  maps to num_age
	 * @param nmUsername  maps to nm_username
	 * @param txtAddressEmail  maps to txt_address_email
	 * @param txtPassword  maps to txt_password
	 * @param dtmSignup  maps to dtm_signup
	 * 
	 * @return the number of rows that were inserted, or -1 if an error occurred
	 * 
	 * @throws SQLException if there was an error in the SQL insert statement
	 */
	public static int insert(Connection connection, Long idUser, Long idUserType, Boolean flIsAlive, Integer numAge, String nmUsername, String txtAddressEmail, String txtPassword, Timestamp dtmSignup) throws SQLException {
		int numResults = -1;
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement(SQL_BUILTIN_INSERT_VALUES);
			ConnectionManager.setBigint(preparedStatement, 1, idUser);
			ConnectionManager.setBigint(preparedStatement, 2, idUserType);
			ConnectionManager.setBoolean(preparedStatement, 3, flIsAlive);
			ConnectionManager.setInt(preparedStatement, 4, numAge);
			ConnectionManager.setVarchar(preparedStatement, 5, nmUsername);
			ConnectionManager.setVarchar(preparedStatement, 6, txtAddressEmail);
			ConnectionManager.setVarchar(preparedStatement, 7, txtPassword);
			ConnectionManager.setDatetime(preparedStatement, 8, dtmSignup);
			numResults = preparedStatement.executeUpdate();
		} catch (SQLException sqlex) {
			throw sqlex;
		} finally {
			ConnectionManager.closeAll(preparedStatement);
		}
		return(numResults);
	}

	/**
	 * Insert a new User into the database utilising the passed in connection 
	 * with only the fields that are allowed to be not null.
	 * 
	 * @param connection the connection to use for the database, this __MUST__ be 
	 *   closed by the calling function.
	 * @param idUser  maps to id_user
	 * @param idUserType  maps to id_user_type
	 * @param numAge  maps to num_age
	 * @param nmUsername  maps to nm_username
	 * @param txtAddressEmail  maps to txt_address_email
	 * @param txtPassword  maps to txt_password
	 * 
	 * @return the number of rows that were inserted, or -1 if an error occurred
	 * 
	 * @throws SQLException if there was an error in the SQL insert statement
	 */
	public static int insert(Connection connection, Long idUser, Long idUserType, Integer numAge, String nmUsername, String txtAddressEmail, String txtPassword) throws SQLException {
		int numResults = -1;
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement(SQL_BUILTIN_INSERT_VALUES);
			ConnectionManager.setBigint(preparedStatement, 1, idUser);
			ConnectionManager.setBigint(preparedStatement, 2, idUserType);
			ConnectionManager.setBoolean(preparedStatement, 3, null);
			ConnectionManager.setInt(preparedStatement, 4, numAge);
			ConnectionManager.setVarchar(preparedStatement, 5, nmUsername);
			ConnectionManager.setVarchar(preparedStatement, 6, txtAddressEmail);
			ConnectionManager.setVarchar(preparedStatement, 7, txtPassword);
			ConnectionManager.setDatetime(preparedStatement, 8, null);
			numResults = preparedStatement.executeUpdate();
		} catch (SQLException sqlex) {
			throw sqlex;
		} finally {
			ConnectionManager.closeAll(preparedStatement);
		}
		return(numResults);
	}

	/**
	 * Insert a new User into the database a new connection will be retrieved 
	 * from the pool, used and then closed.
	 * 
	 * @param idUser  maps to id_user
	 * @param idUserType  maps to id_user_type
	 * @param flIsAlive  maps to fl_is_alive
	 * @param numAge  maps to num_age
	 * @param nmUsername  maps to nm_username
	 * @param txtAddressEmail  maps to txt_address_email
	 * @param txtPassword  maps to txt_password
	 * @param dtmSignup  maps to dtm_signup
	 * 
	 * @return the number of rows that were inserted, or -1 if an error occurred
	 * 
	 * @throws SQLException if there was an error in the SQL insert statement
	 */
	public static int insert(Long idUser, Long idUserType, Boolean flIsAlive, Integer numAge, String nmUsername, String txtAddressEmail, String txtPassword, Timestamp dtmSignup) throws SQLException {
		int numResults = -1;
		Connection connection = null;
		try {
			connection = ConnectionManager.getConnection();
			numResults = insert(connection, idUser, idUserType, flIsAlive, numAge, nmUsername, txtAddressEmail, txtPassword, dtmSignup);
		} catch (SQLException sqlex) {
			throw sqlex;
		} finally {
			ConnectionManager.closeAll(connection);
		}
		return(numResults);
	}

	/**
	 * Insert a new User into the database a new connection will be retrieved 
	 * from the pool, used and then closed. This is for fields which have a nullable allowed default
	 * 
	 * @param idUser  maps to id_user
	 * @param idUserType  maps to id_user_type
	 * @param numAge  maps to num_age
	 * @param nmUsername  maps to nm_username
	 * @param txtAddressEmail  maps to txt_address_email
	 * @param txtPassword  maps to txt_password
	 * 
	 * @return the number of rows that were inserted, or -1 if an error occurred
	 * 
	 * @throws SQLException if there was an error in the SQL insert statement
	 */
	public static int insert(Long idUser, Long idUserType, Integer numAge, String nmUsername, String txtAddressEmail, String txtPassword) throws SQLException {
		int numResults = -1;
		Connection connection = null;
		try {
			connection = ConnectionManager.getConnection();
			numResults = insert(connection, idUser, idUserType, numAge, nmUsername, txtAddressEmail, txtPassword);
		} catch (SQLException sqlex) {
			throw sqlex;
		} finally {
			ConnectionManager.closeAll(connection);
		}
		return(numResults);
	}

	/**
	 * Silently (i.e. swallow any exceptions) Insert a new User into the 
	 * database utilising the passed in connection. If an exception is thrown by the
	 * method, the exception message will be logged as an 'error', if 'trace' logging
	 * is enabled, the stack trace will be printed to the output stream.
	 * 
	 * @param connection the connection to use for the database, this __MUST__ be 
	 *   closed by the calling function.
	 * @param idUser  maps to id_user
	 * @param idUserType  maps to id_user_type
	 * @param flIsAlive  maps to fl_is_alive
	 * @param numAge  maps to num_age
	 * @param nmUsername  maps to nm_username
	 * @param txtAddressEmail  maps to txt_address_email
	 * @param txtPassword  maps to txt_password
	 * @param dtmSignup  maps to dtm_signup
	 * 
	 * @return the number of rows that were inserted, or -1 if an error occurred
	 */
	public static int insertSilent(Connection connection, Long idUser, Long idUserType, Boolean flIsAlive, Integer numAge, String nmUsername, String txtAddressEmail, String txtPassword, Timestamp dtmSignup) {
		int numResults = -1;
		try {
			numResults = insert(connection, idUser, idUserType, flIsAlive, numAge, nmUsername, txtAddressEmail, txtPassword, dtmSignup);
		} catch (SQLException sqlex) {
			LOGGER.error("SQLException caught, message was: {}", sqlex.getMessage());
			if(LOGGER.isTraceEnabled()){
				sqlex.printStackTrace();
			}
		}
		return(numResults);
	}

	/**
	 * Silently (i.e. swallow any exceptions) Insert a new User into the 
	 * database utilising the passed in connection. If an exception is thrown by the
	 * method, the exception message will be logged as an 'error', if 'trace' logging
	 * is enabled, the stack trace will be printed to the output stream.
	 * 
	 * This is only for Non-Nullable fields
	 * 
	 * @param connection the connection to use for the database, this __MUST__ be 
	 *   closed by the calling function.
	 * @param idUser  maps to id_user
	 * @param idUserType  maps to id_user_type
	 * @param numAge  maps to num_age
	 * @param nmUsername  maps to nm_username
	 * @param txtAddressEmail  maps to txt_address_email
	 * @param txtPassword  maps to txt_password
	 * 
	 * @return the number of rows that were inserted, or -1 if an error occurred
	 */
	public static int insertSilent(Connection connection, Long idUser, Long idUserType, Integer numAge, String nmUsername, String txtAddressEmail, String txtPassword) {
		int numResults = -1;
		try {
			numResults = insert(connection, idUser, idUserType, numAge, nmUsername, txtAddressEmail, txtPassword);
		} catch (SQLException sqlex) {
			LOGGER.error("SQLException caught, message was: {}", sqlex.getMessage());
			if(LOGGER.isTraceEnabled()){
				sqlex.printStackTrace();
			}
		}
		return(numResults);
	}

	/**
	 * Silently (i.e. swallow any exceptions) Insert a new User into the 
	 * database, creating and closing a connection in the process. If an exception is thrown 
	 * by the method, the exception message will be logged as an 'error', if 'trace' logging
	 * is enabled, the stack trace will be printed to the output stream.
	 * 
	 * @param idUser  maps to id_user
	 * @param idUserType  maps to id_user_type
	 * @param flIsAlive  maps to fl_is_alive
	 * @param numAge  maps to num_age
	 * @param nmUsername  maps to nm_username
	 * @param txtAddressEmail  maps to txt_address_email
	 * @param txtPassword  maps to txt_password
	 * @param dtmSignup  maps to dtm_signup
	 * 
	 * @return the number of rows that were inserted, or -1 if an error occurred
	 */
	public static int insertSilent(Long idUser, Long idUserType, Boolean flIsAlive, Integer numAge, String nmUsername, String txtAddressEmail, String txtPassword, Timestamp dtmSignup) {
		int numResults = -1;
		Connection connection = null;
		try {
			connection = ConnectionManager.getConnection();
			numResults = insert(connection, idUser, idUserType, flIsAlive, numAge, nmUsername, txtAddressEmail, txtPassword, dtmSignup);
		} catch (SQLException sqlex) {
			LOGGER.error("SQLException caught, message was: {}", sqlex.getMessage());
			if(LOGGER.isTraceEnabled()){
				sqlex.printStackTrace();
			}
		} finally {
			ConnectionManager.closeAll(connection);
		}
		return(numResults);
	}

	/**
	 * Silently (i.e. swallow any exceptions) Insert a new User into the 
	 * database, creating and closing a connection in the process. If an exception is thrown 
	 * by the method, the exception message will be logged as an 'error', if 'trace' logging
	 * is enabled, the stack trace will be printed to the output stream.
	 * 
	 * This is for non-nullabel fields only
	 * 
	 * @param idUser  maps to id_user
	 * @param idUserType  maps to id_user_type
	 * @param numAge  maps to num_age
	 * @param nmUsername  maps to nm_username
	 * @param txtAddressEmail  maps to txt_address_email
	 * @param txtPassword  maps to txt_password
	 * 
	 * @return the number of rows that were inserted, or -1 if an error occurred
	 */
	public static int insertSilent(Long idUser, Long idUserType, Integer numAge, String nmUsername, String txtAddressEmail, String txtPassword) {
		int numResults = -1;
		Connection connection = null;
		try {
			connection = ConnectionManager.getConnection();
			numResults = insert(connection, idUser, idUserType, numAge, nmUsername, txtAddressEmail, txtPassword);
		} catch (SQLException sqlex) {
			LOGGER.error("SQLException caught, message was: {}", sqlex.getMessage());
			if(LOGGER.isTraceEnabled()){
				sqlex.printStackTrace();
			}
		} finally {
			ConnectionManager.closeAll(connection);
		}
		return(numResults);
	}

}
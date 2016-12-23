package synapticloop.sample.h2zero.inserter;

// - - - - thoughtfully generated by synapticloop h2zero - - - - 
//    with the use of synapticloop templar templating language
//                (java-create-inserter.templar)

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import synapticloop.h2zero.base.manager.mysql.ConnectionManager;
import synapticloop.sample.h2zero.model.util.Constants;

public class UserInserter {
	// the binder is unused in code, but will generate compile problems if this 
	// class is no longer referenced in the h2zero file. Just a nicety for
	// removing dead code
	@SuppressWarnings("unused")
	private static final String BINDER = Constants.USER_BINDER;

	// static fields generated by synapticloop h2zero
	private static final String SQL_BUILTIN_INSERT_ALL = "insert into user(id_user, id_user_type, fl_is_alive, num_age, nm_username, txt_address_email, txt_password, dtm_signup)";
	private static final String SQL_BUILTIN_INSERT_VALUES = SQL_BUILTIN_INSERT_ALL + " values (?, ?, ?, ?, ?, ?, ?, ?)";
	// static inserter SQL generated from the user input

	private UserInserter() {}

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

	public static int insertSilent(Connection connection, Long idUser, Long idUserType, Boolean flIsAlive, Integer numAge, String nmUsername, String txtAddressEmail, String txtPassword, Timestamp dtmSignup) {
		int numResults = -1;
		try {
			numResults = insert(connection, idUser, idUserType, flIsAlive, numAge, nmUsername, txtAddressEmail, txtPassword, dtmSignup);
		} catch (SQLException sqlex) {
			sqlex.printStackTrace();
		}
		return(numResults);
	}

	public static int insertSilent(Long idUser, Long idUserType, Boolean flIsAlive, Integer numAge, String nmUsername, String txtAddressEmail, String txtPassword, Timestamp dtmSignup) {
		int numResults = -1;
		Connection connection = null;
		try {
			connection = ConnectionManager.getConnection();
			numResults = insert(connection, idUser, idUserType, flIsAlive, numAge, nmUsername, txtAddressEmail, txtPassword, dtmSignup);
		} catch (SQLException sqlex) {
			sqlex.printStackTrace();
		} finally {
			ConnectionManager.closeAll(connection);
		}
		return(numResults);
	}

}
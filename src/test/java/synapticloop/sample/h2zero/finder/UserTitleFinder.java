package synapticloop.sample.h2zero.finder;

// - - - - thoughtfully generated by synapticloop h2zero - - - - 
//    with the use of synapticloop templar templating language
//                (java-create-finder.templar)

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

import synapticloop.h2zero.base.exception.H2ZeroFinderException;
import synapticloop.h2zero.base.manager.mysql.ConnectionManager;
import synapticloop.h2zero.util.LruCache;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import synapticloop.sample.h2zero.model.util.Constants;
import synapticloop.sample.h2zero.bean.FindIdUserTitleNmUserTitleOrderedBean;

import synapticloop.sample.h2zero.model.UserTitle;

public class UserTitleFinder {
	// the binder is unused in code, but will generate compile problems if this 
	// class is no longer referenced in the h2zero file. Just a nicety for
	// removing dead code
	@SuppressWarnings("unused")
	private static final String BINDER = Constants.USER_TITLE_BINDER;

	private static final Logger LOGGER = LoggerFactory.getLogger(UserTitleFinder.class);
	private static final String SQL_SELECT_START = "select id_user_title, nm_user_title, num_order_by from user_title";
	private static final String SQL_BUILTIN_FIND_BY_PRIMARY_KEY = SQL_SELECT_START + " where id_user_title = ?";

	private static final String SQL_FIND_ID_USER_TITLE_NM_USER_TITLE_ORDERED = "select id_user_title, nm_user_title from user_title order by num_order_by";
	private static final String SQL_FIND_ALL_ORDERED = SQL_SELECT_START + " order by num_order_by";

	// now for the statement limit cache(s)
	private static LruCache<String, String> findAll_limit_statement_cache = new LruCache<String, String>(1024);
	private static LruCache<String, String> findIdUserTitleNmUserTitleOrdered_limit_statement_cache = new LruCache<String, String>(1024);
	private static LruCache<String, String> findAllOrdered_limit_statement_cache = new LruCache<String, String>(1024);

	private UserTitleFinder() {}

	/**
	 * Find a UserTitle by its primary key
	 * 
	 * @param connection the connection item
	 * @param idUserTitle the primary key
	 * 
	 * @return the unique result or throw an exception if one couldn't be found
	 * 
	 * @throws H2ZeroFinderException if one couldn't be found
	 */
	public static UserTitle findByPrimaryKey(Connection connection, Long idUserTitle) throws H2ZeroFinderException {
		UserTitle userTitle = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		if(null == idUserTitle) {
			throw new H2ZeroFinderException("Could not find result as the primary key field [idUserTitle] was null.");
		}

		try {
			preparedStatement = connection.prepareStatement(SQL_BUILTIN_FIND_BY_PRIMARY_KEY);
			preparedStatement.setLong(1, idUserTitle);
			resultSet = preparedStatement.executeQuery();
			userTitle = uniqueResult(resultSet);
		} catch (SQLException sqlex) {
			throw new H2ZeroFinderException(sqlex);
		} catch (H2ZeroFinderException h2zfex) {
			throw new H2ZeroFinderException(h2zfex.getMessage() + "  Additionally, the parameters were [idUserTitle:" + idUserTitle + "].");
		} finally {
			ConnectionManager.closeAll(resultSet, preparedStatement);
		}

		if(null == userTitle) {
			throw new H2ZeroFinderException("Could not find result the parameters were [idUserTitle:" + idUserTitle + "].");
		}
		return(userTitle);
	}

	/**
	 * Find a UserTitle by its primary key
	 * 
	 * @param idUserTitle the primary key
	 * 
	 * @return the unique result or throw an exception if one coudn't be found.
	 * 
	 * @throws H2ZeroFinderException if one couldn't be found
	 */
	public static UserTitle findByPrimaryKey(Long idUserTitle) throws H2ZeroFinderException {
		UserTitle userTitle = null;
		Connection connection = null;

		if(null == idUserTitle) {
			throw new H2ZeroFinderException("Could not find result as the primary key field [idUserTitle] was null.");
		}

		try {
			connection = ConnectionManager.getConnection();
			userTitle = findByPrimaryKey(connection, idUserTitle);
		} catch (SQLException sqlex) {
			throw new H2ZeroFinderException(sqlex);
		} catch (H2ZeroFinderException h2zfex) {
			throw new H2ZeroFinderException(h2zfex.getMessage() + "  Additionally, the parameters were [idUserTitle:" + idUserTitle + "].");
		} finally {
			ConnectionManager.closeAll(connection);
		}

		if(null == userTitle) {
			throw new H2ZeroFinderException("Could not find result the parameters were [idUserTitle:" + idUserTitle + "].");
		}
		return(userTitle);
	}

	/**
	 * Find a UserTitle by its primary key and silently fail.
	 * I.e. Do not throw an exception on error.
	 * 
	 * @param connection the connection item
	 * @param idUserTitle the primary key
	 * 
	 * @return the unique result or null if it couldn't be found
	 * 
	 */
	public static UserTitle findByPrimaryKeySilent(Connection connection, Long idUserTitle) {
		try {
			return(findByPrimaryKey(connection, idUserTitle));
		} catch(H2ZeroFinderException h2zfex){
			if(LOGGER.isWarnEnabled()) {
				LOGGER.warn("H2ZeroFinderException findByPrimaryKeySilent(" + idUserTitle + "): " + h2zfex.getMessage());
				if(LOGGER.isDebugEnabled()) {
					h2zfex.printStackTrace();
				}
			}
			return(null);
		}
	}

	/**
	 * Find a UserTitle by its primary key and silently fail.
	 * I.e. Do not throw an exception on error.
	 * 
	 * @param idUserTitle the primary key
	 * 
	 * @return the unique result or null if it couldn't be found
	 * 
	 */
	public static UserTitle findByPrimaryKeySilent(Long idUserTitle) {
		try {
			return(findByPrimaryKey(idUserTitle));
		} catch(H2ZeroFinderException h2zfex){
			if(LOGGER.isWarnEnabled()) {
				LOGGER.warn("H2ZeroFinderException findByPrimaryKeySilent(" + idUserTitle + "): " + h2zfex.getMessage());
				if(LOGGER.isDebugEnabled()) {
					h2zfex.printStackTrace();
				}
			}
			return(null);
		}
	}

	/**
	 * Find all UserTitle objects with the passed in connection, with limited
	 * results starting at a particular offset.
	 * 
	 * If the limit parameter is null, there will be no limit applied.
	 * 
	 * If the offset is null, then this will be set to 0
	 * 
	 * If both limit and offset are null, then no limit and no offset will be applied
	 * to the statement.
	 * 
	 * The passed in connection object is usable for transactional SQL statements,
	 * where the connection has already had a transaction started on it.
	 * 
	 * If the connection object is null an new connection object will be created 
	 * and closed at the end of the method.
	 * 
	 * If the connection object is not null, then it will not be closed.
	 * 
	 * @param connection - the connection object to use (or null if not part of a transaction)
	 * @param limit - the limit for the result set
	 * @param offset - the offset for the start of the results.
	 * 
	 * @return a list of all of the UserTitle objects
	 * 
	 * @throws SQLException if there was an error in the SQL statement
	 */
	public static List<UserTitle> findAll(Connection connection, Integer limit, Integer offset) throws SQLException {
		boolean hasConnection = (null != connection);
		String statement = null;
		// first find the statement that we want

		String cacheKey = limit + ":" + offset;
		if(!findAll_limit_statement_cache.containsKey(cacheKey)) {
			// place the cacheKey in the cache for later use

			StringBuilder stringBuilder = new StringBuilder(SQL_SELECT_START);

			if(null != limit) {
				stringBuilder.append(" limit ");
				stringBuilder.append(limit);
			}

			if(null != offset) {
				stringBuilder.append(" offset ");
				stringBuilder.append(offset);
			}

			statement = stringBuilder.toString();
			findAll_limit_statement_cache.put(cacheKey, statement);
		} else {
			statement = findAll_limit_statement_cache.get(cacheKey);
		}

		// now set up the statement
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		if(connection == null) {
			connection = ConnectionManager.getConnection();
		}

		List<UserTitle> results = new ArrayList<UserTitle>();

		try {
			preparedStatement = connection.prepareStatement(statement);
			resultSet = preparedStatement.executeQuery();
			results = list(resultSet);
		} catch(SQLException sqlex) {
			if(LOGGER.isWarnEnabled()) {
				LOGGER.warn("SQLException findAll(): " + sqlex.getMessage());
				if(LOGGER.isDebugEnabled()) {
					sqlex.printStackTrace();
				}
			}
			throw sqlex;
		} finally {
			if(hasConnection) {
				ConnectionManager.closeAll(resultSet, preparedStatement, null);
			} else {
				ConnectionManager.closeAll(resultSet, preparedStatement, connection);
			}
		}

		return(results);
	}

	public static List<UserTitle> findAll() throws SQLException {
		return(findAll(null, null, null));
	}

	public static List<UserTitle> findAll(Connection connection) throws SQLException {
		return(findAll(connection, null, null));
	}

	public static List<UserTitle> findAll(Integer limit, Integer offset) throws SQLException {
		return(findAll(null, limit, offset));
	}

	public static List<UserTitle> findAllSilent(Connection connection, Integer limit, Integer offset) {
		try {
			return(findAll(connection, limit, offset));
		} catch(SQLException sqlex){
			if(LOGGER.isWarnEnabled()) {
				LOGGER.warn("SQLException findAllSilent(connection: " + connection + ", limit: " +  limit + ", offset: " + offset + "): " + sqlex.getMessage());
				if(LOGGER.isDebugEnabled()) {
					sqlex.printStackTrace();
				}
			}
			return(new ArrayList<UserTitle>());
		}
	}

	public static List<UserTitle> findAllSilent(Connection connection) {
		return(findAllSilent(connection, null, null));
	}

	public static List<UserTitle> findAllSilent(Integer limit, Integer offset) {
		return(findAllSilent(null, limit, offset));
	}

	public static List<UserTitle> findAllSilent() {
		return(findAllSilent(null, null, null));
	}

	/**
	 * findAllOrdered
	 * 
	 * @return the list of UserTitle results found
	 * 
	 * @throws H2ZeroFinderException if no results could be found
	 * @throws SQLException if there was an error in the SQL statement
	 */
	public static List<UserTitle> findAllOrdered(Connection connection, Integer limit, Integer offset) throws H2ZeroFinderException, SQLException {
		boolean hasConnection = (null != connection);
		String statement = null;

		// first find the statement that we want

		String cacheKey = limit + ":" + offset;
		if(!findAllOrdered_limit_statement_cache.containsKey(cacheKey)) {
			// place the cacheKey in the cache for later use

			StringBuilder stringBuilder = new StringBuilder(SQL_FIND_ALL_ORDERED);

			if(null != limit) {
				stringBuilder.append(" limit ");
				stringBuilder.append(limit);
			}

			if(null != offset) {
				stringBuilder.append(" offset ");
				stringBuilder.append(offset);
			}

			statement = stringBuilder.toString();
			findAllOrdered_limit_statement_cache.put(cacheKey, statement);
		} else {
			statement = findAllOrdered_limit_statement_cache.get(cacheKey);
		}

		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<UserTitle> results = null;
		try {
			if(!hasConnection) {
				connection = ConnectionManager.getConnection();
			}
			preparedStatement = connection.prepareStatement(statement);

			resultSet = preparedStatement.executeQuery();
			results = list(resultSet);
		} catch (SQLException sqlex) {
			throw sqlex;
		} finally {
			if(hasConnection) {
				ConnectionManager.closeAll(resultSet, preparedStatement, null);
			} else {
				ConnectionManager.closeAll(resultSet, preparedStatement, connection);
			}
		}


		if(null == results) {
			throw new H2ZeroFinderException("Could not find result.");
		}
		return(results);
	}

	public static List<UserTitle> findAllOrdered(Connection connection) throws H2ZeroFinderException, SQLException {
		return(findAllOrdered(connection, null, null));
	}

	public static List<UserTitle> findAllOrdered(Integer limit, Integer offset) throws H2ZeroFinderException, SQLException {
		return(findAllOrdered(null, limit, offset));
	}

	public static List<UserTitle> findAllOrdered() throws H2ZeroFinderException, SQLException {
		return(findAllOrdered(null, null, null));
	}

// silent connection, params..., limit, offset
	public static List<UserTitle> findAllOrderedSilent(Connection connection, Integer limit, Integer offset) {
		try {
			return(findAllOrdered(connection, limit, offset));
		} catch(H2ZeroFinderException h2zfex) {
			if(LOGGER.isWarnEnabled()) {
				LOGGER.warn("H2ZeroFinderException findAllOrderedSilent(connection: " + connection + ", limit: " + limit + ", offset: " + offset + "): " + h2zfex.getMessage());
				if(LOGGER.isDebugEnabled()) {
					h2zfex.printStackTrace();
				}
			}
			return(new ArrayList<UserTitle>());
		} catch(SQLException sqlex) {
			if(LOGGER.isWarnEnabled()) {
				LOGGER.warn("SQLException findAllOrderedSilent(connection: " + connection + ", limit: " + limit + ", offset: " + offset + "): " + sqlex.getMessage());
				if(LOGGER.isDebugEnabled()) {
					sqlex.printStackTrace();
				}
			}
			return(new ArrayList<UserTitle>());
		}
	}

// silent connection, params...
	public static List<UserTitle> findAllOrderedSilent(Connection connection) {
		return(findAllOrderedSilent(connection, null, null));
	}

// silent params..., limit, offset
	public static List<UserTitle> findAllOrderedSilent(Integer limit, Integer offset) {
		return(findAllOrderedSilent(null , limit, offset));
	}

	public static List<UserTitle> findAllOrderedSilent() {
		return(findAllOrderedSilent(null, null, null));
	}

	/**
	 * Return a unique result for the query - in effect just the first result of
	 * query.
	 * 
	 * @param resultSet The result set of the query
	 * 
	 * @return The UserTitle that represents this result
	 * 
	 * @throws H2ZeroFinderException if no results were found
	 * @throws SQLException if there was a problem retrieving the results
	 */
	private static UserTitle uniqueResult(ResultSet resultSet) throws H2ZeroFinderException, SQLException {
		if(resultSet.first()) {
			// we have a result
			Long idUserTitle = resultSet.getLong(1);
			String nmUserTitle = resultSet.getString(2);
			Integer numOrderBy = resultSet.getInt(3);

			UserTitle userTitle = new UserTitle(idUserTitle, nmUserTitle, numOrderBy);

			if(resultSet.next()) {
				throw new H2ZeroFinderException("More than one result in resultset for unique finder.");
			} else {
				return(userTitle);
			}
		} else {
			// could not get a result
			return(null);
		}
	}

	/**
	 * Return the results as a list of UserTitle, this will be empty if
	 * none are found.
	 * 
	 * @param resultSet the results as a list of UserTitle
	 * 
	 * @return the list of results
	 * 
	 * @throws SQLException if there was a problem retrieving the results
	 */
	private static List<UserTitle> list(ResultSet resultSet) throws SQLException {
		List<UserTitle> arrayList = new ArrayList<UserTitle>();
		while(resultSet.next()) {
			arrayList.add(new UserTitle(
					resultSet.getLong(1),
					resultSet.getString(2),
					resultSet.getInt(3)));
		}
		return(arrayList);
	}

// SELECTBEAN - CONNECTION, PARAMS..., LIMIT, OFFSET
	public static List<FindIdUserTitleNmUserTitleOrderedBean> findIdUserTitleNmUserTitleOrdered(Connection connection, Integer limit, Integer offset) throws H2ZeroFinderException, SQLException {
		boolean hasConnection = (null != connection);
		if(!hasConnection) {
			connection = ConnectionManager.getConnection();
		}

		String statement = null;

		// first find the statement that we want

		String cacheKey = limit + ":" + offset;
		if(!findIdUserTitleNmUserTitleOrdered_limit_statement_cache.containsKey(cacheKey)) {
			// place the cacheKey in the cache for later use

			StringBuilder stringBuilder = new StringBuilder(SQL_FIND_ID_USER_TITLE_NM_USER_TITLE_ORDERED);

			if(null != limit) {
				stringBuilder.append(" limit ");
				stringBuilder.append(limit);
			}

			if(null != offset) {
				stringBuilder.append(" offset ");
				stringBuilder.append(offset);
			}

			statement = stringBuilder.toString();
			findIdUserTitleNmUserTitleOrdered_limit_statement_cache.put(cacheKey, statement);
		} else {
			statement = findIdUserTitleNmUserTitleOrdered_limit_statement_cache.get(cacheKey);
		}

		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			preparedStatement = connection.prepareStatement(SQL_FIND_ID_USER_TITLE_NM_USER_TITLE_ORDERED);

			resultSet = preparedStatement.executeQuery();
			List<FindIdUserTitleNmUserTitleOrderedBean> results = listFindIdUserTitleNmUserTitleOrderedBean(resultSet);
			return(results);
		} catch (SQLException sqlex) {
			throw sqlex;
		} finally {
			if(hasConnection) {
				ConnectionManager.closeAll(resultSet, preparedStatement, null);
			} else {
				ConnectionManager.closeAll(resultSet, preparedStatement, connection);
			}
		}

	}

// SELECTBEAN - PARAMS..., LIMIT, OFFSET 
	public static List<FindIdUserTitleNmUserTitleOrderedBean> findIdUserTitleNmUserTitleOrdered(Integer limit, Integer offset) throws H2ZeroFinderException, SQLException {
		return(findIdUserTitleNmUserTitleOrdered(null, limit, offset));
	}

// SELECTBEAN - CONNECTION, PARAMS...
	public static List<FindIdUserTitleNmUserTitleOrderedBean> findIdUserTitleNmUserTitleOrdered(Connection connection) throws H2ZeroFinderException, SQLException {
		return(findIdUserTitleNmUserTitleOrdered(null, null, null));
	}

// SELECTBEAN - PARAMS...
	public static List<FindIdUserTitleNmUserTitleOrderedBean> findIdUserTitleNmUserTitleOrdered() throws H2ZeroFinderException, SQLException {
		return(findIdUserTitleNmUserTitleOrdered(null, null, null));
	}

// SILENT SELECTBEAN: CONNECTION, PARAMS..., LIMIT, OFFSET
	public static List<FindIdUserTitleNmUserTitleOrderedBean> findIdUserTitleNmUserTitleOrderedSilent(Connection connection, Integer limit, Integer offset) {
		try {
			return(findIdUserTitleNmUserTitleOrdered(connection, limit, offset));
		} catch(H2ZeroFinderException h2zfex) {
			if(LOGGER.isWarnEnabled()) {
				LOGGER.warn("H2ZeroFinderException findIdUserTitleNmUserTitleOrderedSilent(connection: " + connection  + ", limit: " + limit + ", offset: " + offset + "): " + h2zfex.getMessage());
				if(LOGGER.isDebugEnabled()) {
					h2zfex.printStackTrace();
				}
			}
			return(new ArrayList<FindIdUserTitleNmUserTitleOrderedBean>());
		} catch(SQLException sqlex) {
			if(LOGGER.isWarnEnabled()) {
				LOGGER.warn("SQLException findIdUserTitleNmUserTitleOrderedSilent(connection: " + connection  + ", limit: " + limit + ", offset: " + offset + "): " + sqlex.getMessage());
				if(LOGGER.isDebugEnabled()) {
					sqlex.printStackTrace();
				}
			}
			return(new ArrayList<FindIdUserTitleNmUserTitleOrderedBean>());
		}
	}

// CONNECTION, PARAMS...
	public static List<FindIdUserTitleNmUserTitleOrderedBean> findIdUserTitleNmUserTitleOrderedSilent(Connection connection) {
		return(findIdUserTitleNmUserTitleOrderedSilent(connection, null, null));
	}

// PARAMS..., LIMIT, OFFSET
	public static List<FindIdUserTitleNmUserTitleOrderedBean> findIdUserTitleNmUserTitleOrderedSilent(Integer limit, Integer offset) {
		return(findIdUserTitleNmUserTitleOrderedSilent(null, limit, offset));
	}

// PARAMS...
	public static List<FindIdUserTitleNmUserTitleOrderedBean> findIdUserTitleNmUserTitleOrderedSilent() {
		return(findIdUserTitleNmUserTitleOrderedSilent(null, null, null));
	}

	/**
	 * Return the results as a list of FindIdUserTitleNmUserTitleOrderedBeans, this will be empty if
	 * none are found.
	 * 
	 * @param resultSet the results as a list of FindIdUserTitleNmUserTitleOrderedBean
	 * 
	 * @return the list of results
	 * 
	 * @throws SQLException if there was a problem retrieving the results
	 */
	private static List<FindIdUserTitleNmUserTitleOrderedBean> listFindIdUserTitleNmUserTitleOrderedBean(ResultSet resultSet) throws SQLException {
		List<FindIdUserTitleNmUserTitleOrderedBean> arrayList = new ArrayList<FindIdUserTitleNmUserTitleOrderedBean>();
		while(resultSet.next()) {
			arrayList.add(new FindIdUserTitleNmUserTitleOrderedBean(
					resultSet.getLong(1),
					resultSet.getString(2)));
		}
		return(arrayList);
	}

}
package synapticloop.sample.h2zero.sqlite3.finder;

// - - - - thoughtfully generated by synapticloop h2zero - - - - 
//    with the use of synapticloop templar templating language
//                (java-create-finder.templar)

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Blob;
import java.util.List;
import java.util.ArrayList;

import synapticloop.h2zero.base.exception.H2ZeroFinderException;
import synapticloop.h2zero.base.manager.sqlite3.ConnectionManager;
import synapticloop.h2zero.util.LruCache;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import synapticloop.sample.h2zero.sqlite3.model.util.Constants;

import synapticloop.sample.h2zero.sqlite3.model.Pet;

public class PetFinder {
	// the binder is unused in code, but will generate compile problems if this 
	// class is no longer referenced in the h2zero file. Just a nicety for
	// removing dead code
	@SuppressWarnings("unused")
	private static final String BINDER = Constants.PET_BINDER;

private static final Logger LOGGER = LoggerFactory.getLogger(UserFinder.class);
	private static final String SQL_SELECT_START = "select id_pet, nm_pet, num_age, flt_weight, dt_birthday, img_photo from pet";
	private static final String SQL_BUILTIN_FIND_BY_PRIMARY_KEY = SQL_SELECT_START + " where id_pet = ?";


	// now for the statement limit cache(s)
	private static LruCache<String, String> findAll_limit_statement_cache = new LruCache<String, String>(1024);

	private PetFinder() {}

	/**
	 * Find a Pet by its primary key
	 * 
	 * @param connection the connection item
	 * @param idPet the primary key
	 * 
	 * @return the unique result or throw an exception if one couldn't be found
	 * 
	 * @throws H2ZeroFinderException if one couldn't be found
	 */
	public static Pet findByPrimaryKey(Connection connection, Long idPet) throws H2ZeroFinderException {
		Pet pet = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		if(null == idPet) {
			throw new H2ZeroFinderException("Could not find result as the primary key field [idPet] was null.");
		}

		try {
			preparedStatement = connection.prepareStatement(SQL_BUILTIN_FIND_BY_PRIMARY_KEY);
			preparedStatement.setLong(1, idPet);
			resultSet = preparedStatement.executeQuery();
			pet = uniqueResult(resultSet);
		} catch (SQLException sqlex) {
			throw new H2ZeroFinderException(sqlex);
		} catch (H2ZeroFinderException h2zfex) {
			throw new H2ZeroFinderException(h2zfex.getMessage() + "  Additionally, the parameters were [idPet:" + idPet + "].");
		} finally {
			ConnectionManager.closeAll(resultSet, preparedStatement);
		}

		if(null == pet) {
			throw new H2ZeroFinderException("Could not find result the parameters were [idPet:" + idPet + "].");
		}
		return(pet);
	}

	/**
	 * Find a Pet by its primary key
	 * 
	 * @param idPet the primary key
	 * 
	 * @return the unique result or throw an exception if one coudn't be found.
	 * 
	 * @throws H2ZeroFinderException if one couldn't be found
	 */
	public static Pet findByPrimaryKey(Long idPet) throws H2ZeroFinderException {
		Pet pet = null;
		Connection connection = null;

		if(null == idPet) {
			throw new H2ZeroFinderException("Could not find result as the primary key field [idPet] was null.");
		}

		try {
			connection = ConnectionManager.getConnection();
			pet = findByPrimaryKey(connection, idPet);
		} catch (SQLException sqlex) {
			throw new H2ZeroFinderException(sqlex);
		} catch (H2ZeroFinderException h2zfex) {
			throw new H2ZeroFinderException(h2zfex.getMessage() + "  Additionally, the parameters were [idPet:" + idPet + "].");
		} finally {
			ConnectionManager.closeAll(connection);
		}

		if(null == pet) {
			throw new H2ZeroFinderException("Could not find result the parameters were [idPet:" + idPet + "].");
		}
		return(pet);
	}

	/**
	 * Find a Pet by its primary key and silently fail.
	 * I.e. Do not throw an exception on error.
	 * 
	 * @param connection the connection item
	 * @param idPet the primary key
	 * 
	 * @return the unique result or null if it couldn't be found
	 * 
	 */
	public static Pet findByPrimaryKeySilent(Connection connection, Long idPet) {
		try {
			return(findByPrimaryKey(connection, idPet));
		} catch(H2ZeroFinderException h2zfex){
			if(LOGGER.isWarnEnabled()) {
				LOGGER.warn("H2ZeroFinderException findByPrimaryKeySilent(" + idPet + "): " + h2zfex.getMessage());
				if(LOGGER.isDebugEnabled()) {
					h2zfex.printStackTrace();
				}
			}
			return(null);
		}
	}

	/**
	 * Find a Pet by its primary key and silently fail.
	 * I.e. Do not throw an exception on error.
	 * 
	 * @param idPet the primary key
	 * 
	 * @return the unique result or null if it couldn't be found
	 * 
	 */
	public static Pet findByPrimaryKeySilent(Long idPet) {
		try {
			return(findByPrimaryKey(idPet));
		} catch(H2ZeroFinderException h2zfex){
			if(LOGGER.isWarnEnabled()) {
				LOGGER.warn("H2ZeroFinderException findByPrimaryKeySilent(" + idPet + "): " + h2zfex.getMessage());
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
	public static List<Pet> findAll(Connection connection, Integer limit, Integer offset) throws SQLException {
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

		List<Pet> results = new ArrayList<Pet>();

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

	public static List<Pet> findAll() throws SQLException {
		return(findAll(null, null, null));
	}

	public static List<Pet> findAll(Connection connection) throws SQLException {
		return(findAll(connection, null, null));
	}

	public static List<Pet> findAll(Integer limit, Integer offset) throws SQLException {
		return(findAll(null, limit, offset));
	}

	public static List<Pet> findAllSilent(Connection connection, Integer limit, Integer offset) {
		try {
			return(findAll(connection, limit, offset));
		} catch(SQLException sqlex){
			if(LOGGER.isWarnEnabled()) {
				LOGGER.warn("SQLException findAllSilent(connection: " + connection + ", limit: " +  limit + ", offset: " + offset + "): " + sqlex.getMessage());
				if(LOGGER.isDebugEnabled()) {
					sqlex.printStackTrace();
				}
			}
			return(new ArrayList<Pet>());
		}
	}

	public static List<Pet> findAllSilent(Connection connection) {
		return(findAllSilent(connection, null, null));
	}

	public static List<Pet> findAllSilent(Integer limit, Integer offset) {
		return(findAllSilent(null, limit, offset));
	}

	public static List<Pet> findAllSilent() {
		return(findAllSilent(null, null, null));
	}

	/**
	 * Return a unique result for the query - in effect just the first result of
	 * query.
	 * 
	 * @param resultSet The result set of the query
	 * 
	 * @return The Pet that represents this result
	 * 
	 * @throws H2ZeroFinderException if no results were found
	 * @throws SQLException if there was a problem retrieving the results
	 */
	private static Pet uniqueResult(ResultSet resultSet) throws H2ZeroFinderException, SQLException {
		if(resultSet.first()) {
			// we have a result
			Long idPet = resultSet.getLong(1);
			String nmPet = resultSet.getString(2);
			Integer numAge = resultSet.getInt(3);
			Float fltWeight = resultSet.getFloat(4);
			if(resultSet.wasNull()) {
				fltWeight = null;
			}
			Date dtBirthday = resultSet.getDate(5);
			if(resultSet.wasNull()) {
				dtBirthday = null;
			}
			Blob imgPhoto = resultSet.getBlob(6);
			if(resultSet.wasNull()) {
				imgPhoto = null;
			}

			Pet pet = new Pet(idPet, nmPet, numAge, fltWeight, dtBirthday, imgPhoto);

			if(resultSet.next()) {
				throw new H2ZeroFinderException("More than one result in resultset for unique finder.");
			} else {
				return(pet);
			}
		} else {
			// could not get a result
			return(null);
		}
	}

	/**
	 * Return the results as a list of Pet, this will be empty if
	 * none are found.
	 * 
	 * @param resultSet the results as a list of Pet
	 * 
	 * @return the list of results
	 * 
	 * @throws SQLException if there was a problem retrieving the results
	 */
	private static List<Pet> list(ResultSet resultSet) throws SQLException {
		List<Pet> arrayList = new ArrayList<Pet>();
		while(resultSet.next()) {
			arrayList.add(new Pet(
					resultSet.getLong(1),
					resultSet.getString(2),
					resultSet.getInt(3),
					ConnectionManager.getNullableResultFloat(resultSet, 4),
					ConnectionManager.getNullableResultDate(resultSet, 5),
					ConnectionManager.getNullableResultBlob(resultSet, 6)));
		}
		return(arrayList);
	}

}
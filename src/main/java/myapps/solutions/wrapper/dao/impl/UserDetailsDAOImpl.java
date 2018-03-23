package myapps.solutions.wrapper.dao.impl;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

import org.apache.http.util.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import myapps.solutions.wrapper.dao.IPasswordDAO;
import myapps.solutions.wrapper.dao.ITermsConditionsDAO;
import myapps.solutions.wrapper.dao.IUserDetailsDAO;
import myapps.solutions.wrapper.model.AccountDevice;
import myapps.solutions.wrapper.model.Authorization;
import myapps.solutions.wrapper.model.AuthorizationResult;
import myapps.solutions.wrapper.model.AuthorizationResult.AuthorizationCodes;
import myapps.solutions.wrapper.model.State;
import myapps.solutions.wrapper.model.SyncDetails;
import myapps.solutions.wrapper.model.TermsConditions;
import myapps.solutions.wrapper.model.TermsConditionsHistory;
import myapps.solutions.wrapper.model.UserDetails;
import myapps.solutions.wrapper.model.UserPref;
import myapps.solutions.wrapper.model.UserSession;
import myapps.solutions.wrapper.model.UserSubscription;
import myapps.solutions.wrapper.model.UserType;
import myapps.solutions.wrapper.model.Wallet;
import myapps.solutions.wrapper.utils.Auth;
import myapps.solutions.wrapper.utils.Misc;

@Repository
@Transactional(value = "wrapperTranscationManager")
public class UserDetailsDAOImpl implements IUserDetailsDAO {

	@PersistenceContext(unitName = "wrapper")
	private EntityManager wrapperEntityManager;

	@PersistenceContext(unitName = "huddil")
	private EntityManager huddilEntityManager;

	@Autowired
	ITermsConditionsDAO termsConditionsDAO;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	IPasswordDAO pass;

	private final static String FETCH_BY_USERNAME = "select username,password, authorities from auth_details a join oauth_client_details o ON a.id = o.authId where username=?";

	private final static String FETCH_BY_CLIENT_ID = "select o.client_id, o.client_secret, o.resource_ids, o.scope, o.authorized_grant_types, o.access_token_validity, o.refresh_token_validity from oauth_client_details o join wrapper.user_details u on u.id = o.userId where u.userName = ? OR u.emailId = ?  OR u.mobileNo = ?";

	/***
	 * UserDetails operation
	 * 
	 * @throws IOException
	 * @throws MessagingException
	 */
	@Override
	@Transactional(value = "wrapperTranscationManager")
	public boolean add(UserDetails userDetails, String redirectUrl) throws IOException, MessagingException {
		UserType userType = null;
		if (userDetails.getAuthorization() != null)
			userDetails.getAuthorization().setUserDetails(userDetails);
		if (userDetails.getAccountDevices() != null && !userDetails.getAccountDevices().isEmpty())
			userDetails.getAccountDevices().get(0).setUserDetails(userDetails);
		/*
		 * if (TextUtils.isEmpty(userDetails.getCity()) ||
		 * !isStateExist(userDetails.getCity())) userDetails.setCity(null);
		 */
		if (TextUtils.isEmpty(userDetails.getProduct()))
			userDetails.setProduct("ProductNotValid");
		else {
			userType = getUserType(userDetails.getProduct(), userDetails.getUserType());
			if (userType == null)
				userDetails.setProduct("ProductNotValid");
			else {
				List<UserSubscription> userSubscriptions = new ArrayList<UserSubscription>();
				userSubscriptions.add(new UserSubscription(userDetails, userType));
				userDetails.setUserSubscriptions(userSubscriptions);
				TermsConditions tcps = termsConditionsDAO.getTermsAndConditions(userDetails.getUserType(),
						userDetails.getProduct());
				List<TermsConditionsHistory> conditionsHistories = userDetails.getTermsConditionsHistories();
				if (!conditionsHistories.isEmpty()) {
					if (conditionsHistories.get(0).getTermsConditions().getId() != tcps.getId())
						conditionsHistories.get(0).getTermsConditions().setDescription("NotValid");
					conditionsHistories.get(0).setUserDetails(userDetails);
				}
			}
		}
		if (userDetails.getProduct().equalsIgnoreCase("huddil"))
			userDetails.setAge(0);
		userDetails.setWallet(new Wallet(userDetails, 0));
		wrapperEntityManager.persist(userDetails);
		return true;
	}

	@Override
	public UserDetails get(String sessionId) {
		try {
			UserDetails userDetails = (UserDetails) wrapperEntityManager
					.createQuery(
							"SELECT d FROM UserDetails d, UserSession s WHERE s.userId = d.id AND s.sessionId = :sessionId")
					.setParameter("sessionId", sessionId).getSingleResult();
			return userDetails;
		} catch (NoResultException e) {
			return null;
		}
	}

	@Transactional(value = "wrapperTranscationManager")
	@Override
	public boolean update(String sessionId, UserDetails latest) {
		Object obj = wrapperEntityManager
				.createQuery(
						"SELECT d FROM UserDetails d, UserSession s WHERE s.userId = d.id AND s.sessionId = :sessionId")
				.setParameter("sessionId", sessionId).getSingleResult();
		if (obj == null)
			return false;
		UserDetails old = (UserDetails) obj;
		String value;
		value = latest.getFirstName();
		if (value != null)
			old.setFirstName(value);
		value = latest.getLastName();
		if (value != null)
			old.setLastName(value);
		value = latest.getAddressingName();
		if (value != null)
			old.setAddressingName(value);
		value = latest.getAddress();
		if (value != null)
			old.setAddress(value);
		value = latest.getCity();
		if (value != null)
			old.setCity(value);
		value = latest.getGender();
		if (value != null)
			old.setGender(value);
		if (latest.getDob() != null) {
			old.setAge(Misc.calculateAge(latest.getDob()));
			old.setDob(latest.getDob());
		}
		if (latest.getPhoto() != null && latest.getPhoto().getImage() == null)
			old.getPhoto().setImage(null);
		else if (latest.getPhoto() != null && latest.getPhoto().getImage() != null) {
			old.getPhoto().setImage(latest.getPhoto().getImage());
		}
		old.setProduct("huddil");
		wrapperEntityManager.merge(old);
		wrapperEntityManager.flush();
		return true;
	}

	@Override
	public boolean delete(String sessionId) {
		return (wrapperEntityManager
				.createNativeQuery(
						"DELETE d FROM user_details d INNER JOIN user_session s WHERE d.id = s.userId and s.sessionId = :sessionId")
				.setParameter("sessionId", sessionId).executeUpdate()) == 0 ? false : true;
	}

	@Override
	@Transactional(value = "wrapperTranscationManager")
	public boolean deleteUserAccountThroughEmail(String emailId, String token) {
		return (wrapperEntityManager
				.createNativeQuery(
						"DELETE u FROM user_details u JOIN tokens t on u.id = t.userId WHERE u.emailId= :emailId and t.token=:token")
				.setParameter("emailId", emailId).setParameter("token", token).executeUpdate()) == 0 ? false : true;

	}

	@Override
	@Transactional(value = "wrapperTranscationManager")
	public boolean activateAcc(String sessionId, boolean state) {
		return (wrapperEntityManager
				.createNativeQuery(
						"UPDATE user_details d INNER JOIN user_session s SET d.isActive = :state where d.id = s.userId and s.sessionId = :sessionId")
				.setParameter("sessionId", sessionId).setParameter("state", state).executeUpdate()) == 0 ? false : true;
	}

	/***
	 * AccountDevice operations
	 */
	@Override
	@Transactional(value = "wrapperTranscationManager")
	public AuthorizationCodes addAccDevice(AccountDevice accountDevice, int userId) {
		// String hql = "select macAddress, rs.c from account_device as ad right
		// join (select count(macAddress) as c from account_device where userId
		// = :userId) as rs on ad.userId=:userId and ad.macAddress =:mac";
		// Query query =
		// entityManagerWrapper.createNativeQuery(hql).setParameter("userId",
		// userId).setParameter("mac", accountDevice.getMacAddress());
		// Object[] rs = (Object[]) query.getSingleResult();
		// String macAddress = rs[0] == null ? null : rs[0].toString();
		// int count = Integer.parseInt(rs[1].toString());
		/*
		 * if (macAddress != null) return AuthorizationCodes.Success; else if
		 * (dftSettings.getMaxDeviceCnt() > count && macAddress == null) {
		 * accountDevice.setUserDetails(entityManagerWrapper.getReference(
		 * UserDetails.class, userId));
		 * entityManagerWrapper.persist(accountDevice); return
		 * AuthorizationCodes.Success; } else
		 */
		return AuthorizationCodes.DeviceLimitReached;

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AccountDevice> getAccDevice(String sessionId) {
		Query query = wrapperEntityManager
				.createQuery(
						"SELECT d FROM AccountDevice d, UserSession u WHERE u.userId = d.userDetails AND u.sessionId = :sessionId")
				.setParameter("sessionId", sessionId);
		return (List<AccountDevice>) query.getResultList();
	}

	/***
	 * Verify whether the userName, mobileNo, emailId already exist
	 */
	@Override
	public boolean isUserNameExist(String userName) {
		return (String) wrapperEntityManager
				.createQuery("SELECT userName FROM UserDetails u WHERE userName = :userName OR mobileNo = :userName")
				.setParameter("userName", userName).getSingleResult() != null;
	}

	@Override
	public boolean isMobileNoExist(String mobileNo) {
		return (String) wrapperEntityManager
				.createQuery("SELECT mobileNo FROM UserDetails WHERE userName = :mobileNo OR mobileNo = :mobileNo")
				.setParameter("mobileNo", mobileNo).getSingleResult() != null;
	}

	@Override
	public boolean isEmailIdExists(String emailId) {
		return wrapperEntityManager.createQuery("SELECT emailId FROM UserDetails WHERE emailId = :emailId")
				.setParameter("emailId", emailId).getSingleResult() != null;
	}

	/***
	 * Authorization & Session operations
	 */

	@Override
	public AuthorizationResult canAuthenticate(String userName, byte[] password) {
		Object isActive = wrapperEntityManager
				.createNativeQuery(
						"SELECT u.isActive FROM user_details u WHERE (u.userName = :userName OR u.emailId =:userName)")
				.setParameter("userName", userName).getSingleResult();
		if (isActive == null)
			return new AuthorizationResult(AuthorizationCodes.NotFound);
		else if ((int) isActive == -1)
			return new AuthorizationResult(AuthorizationCodes.EmailIdNotVerified);
		else if ((int) isActive == 0 || (int) isActive == 1) {
			Query query = wrapperEntityManager.createNativeQuery(
					"SELECT a.* FROM authorization a join user_details u ON a.userId = u.id WHERE (u.userName = :id OR u.emailId =:id  OR u.mobileNo =:id)",
					"auth").setParameter("id", userName);
			@SuppressWarnings("unchecked")
			List<Authorization> list = query.getResultList();
			if (list.size() == 1) {
				Authorization authorization = list.get(0);
				if (Auth.isPasswordMatch(password, authorization.getPassword(), authorization.getSalt())) {
					return new AuthorizationResult(authorization.getUserId(), AuthorizationCodes.Success);
				} else
					return new AuthorizationResult(AuthorizationCodes.Fail);
			}
			return new AuthorizationResult(AuthorizationCodes.NotFound);
		} else
			return new AuthorizationResult(AuthorizationCodes.AccountDeActivated);
	}

	@Override
	@Transactional(value = "huddilTranscationManager")
	public AuthorizationResult createSession(int userId, String emailId, String deviceName, String deviceType,
			String macAddress, String product) {
		String sessionId = Auth.getSessionId(userId);
		StoredProcedureQuery query = wrapperEntityManager.createStoredProcedureQuery("createSession", "sync_details")
				.registerStoredProcedureParameter("p_emailId", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("p_sessionId", String.class, ParameterMode.INOUT)
				.registerStoredProcedureParameter("p_deviceName", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("p_deviceType", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("p_macAddress", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("p_product", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("p_userType", Integer.class, ParameterMode.OUT)
				.registerStoredProcedureParameter("p_userId", Integer.class, ParameterMode.OUT)
				.registerStoredProcedureParameter("p_result", Integer.class, ParameterMode.OUT)
				.setParameter("p_emailId", emailId).setParameter("p_sessionId", sessionId)
				.setParameter("p_deviceName", deviceName).setParameter("p_deviceType", deviceType)
				.setParameter("p_macAddress", macAddress).setParameter("p_product", product);
		query.execute();
		int result = Integer.parseInt(query.getOutputParameterValue("p_result").toString());
		Object obj = query.getOutputParameterValue("p_userType");
		if (obj == null)
			return new AuthorizationResult(AuthorizationCodes.NotFound);
		String userType = obj.toString();
		sessionId = query.getOutputParameterValue("p_sessionId").toString();
		if (result == -1)
			return new AuthorizationResult(AuthorizationCodes.NotFound);
		else if (result == 3 || result == 4) {
			userId = Integer.parseInt(query.getOutputParameterValue("p_userId").toString());
			huddilEntityManager.createQuery("UPDATE UserPref u SET u.sessionId = :sessionId WHERE u.userId = :userId")
					.setParameter("sessionId", sessionId).setParameter("userId", userId).executeUpdate();
			return new AuthorizationResult(userId, userType, sessionId, AuthorizationCodes.Success,
					(SyncDetails) query.getSingleResult());
		} else if (result == 2)
			return new AuthorizationResult(AuthorizationCodes.DeviceLimitReached);
		else if (result == 1)
			return new AuthorizationResult(userId, userType, sessionId, AuthorizationCodes.Success,
					(SyncDetails) query.getSingleResult());
		else if (result == 0)
			return new AuthorizationResult(AuthorizationCodes.SessionExist);
		return new AuthorizationResult(AuthorizationCodes.NotFound);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserSession> getSessions() {
		return wrapperEntityManager.createQuery("SELECT us FROM UserSession us").getResultList();
	}

	@Override
	public boolean isSessionExistWithDifferentUser(int userId, String macAddress) {
		AccountDevice accountDevice = wrapperEntityManager.find(AccountDevice.class, macAddress);
		if (accountDevice != null && accountDevice.getUserDetails().getId() != userId)
			return true;
		return false;
	}

	@Override
	@Transactional(value = "wrapperTranscationManager")
	public boolean logout(String sessionId) {
		return wrapperEntityManager.createQuery("DELETE FROM UserSession u WHERE u.sessionId = :sessionId")
				.setParameter("sessionId", sessionId).executeUpdate() == 1;
	}

	@Override
	@Transactional(value = "huddilTranscationManager")
	public boolean logoutHuddil(String sessionId) {
		return huddilEntityManager
				.createQuery("UPDATE UserPref u SET u.sessionId = NULL WHERE u.sessionId = :sessionId")
				.setParameter("sessionId", sessionId).executeUpdate() == 1;
	}

	@Override
	public String getCashupUserType(String id) {
		return wrapperEntityManager
				.createNativeQuery("SELECT p.userType FROM cashup.user_preferences p JOIN wrapper.user_details u "
						+ "ON p.userId = u.id WHERE u.userName = :id OR u.emailId = :id OR u.mobileNo = :id")
				.setParameter("id", id).getSingleResult().toString();
	}

	/* change userType based on sessionId & User Email */
	@Override
	@Transactional(value = "wrapperTranscationManager")
	public boolean blockUser(String sessionId, int state, String user) {
		int UserType = (int) wrapperEntityManager
				.createNativeQuery("SELECT u.userType FROM cashup.user_preferences u where u.sessionId= :sessionId")
				.setParameter("sessionId", sessionId).getSingleResult();
		if (UserType == 3) {

			return (wrapperEntityManager
					.createNativeQuery("UPDATE user_details u SET u.isActive =:state WHERE u.emailId =:user")
					.setParameter("state", state).setParameter("user", user).executeUpdate()) == 0 ? false : true;
		} else
			return false;

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<State> sateList() {
		return wrapperEntityManager.createQuery("SELECT s FROM State s").getResultList();
	}

	/*
	 * private boolean isStateExist(String state) { if
	 * (state.equalsIgnoreCase("all")) return false; return
	 * wrapperEntityManager.
	 * createQuery("SELECT s FROM State s WHERE s.name = :state")
	 * .setParameter("state", state).getSingleResult() != null; }
	 */

	@Override
	@Transactional(value = "huddilTranscationManager")
	public int activateAccountThroughEmail(String emailId, String token, String product) {
		StoredProcedureQuery query = wrapperEntityManager.createStoredProcedureQuery("activateUser")
				.registerStoredProcedureParameter("p_emailId", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("p_token", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("p_userId", Integer.class, ParameterMode.OUT)
				.registerStoredProcedureParameter("p_userType", Integer.class, ParameterMode.OUT)
				.registerStoredProcedureParameter("p_result", Integer.class, ParameterMode.OUT)
				.setParameter("p_emailId", emailId).setParameter("p_token", token);
		query.execute();
		return Integer.parseInt(query.getOutputParameterValue("p_result").toString());
	}

	@Transactional(value = "huddilTranscationManager")
	@Override
	public void createHuddilUser(int userId, int userType, String emailId, String mobileNo, String displayName) {
		UserPref user = new UserPref(userId, userType, emailId, mobileNo, displayName);
		huddilEntityManager.persist(user);
	}

	@Override
	@Transactional(value = "wrapperTranscationManager")
	public boolean verifyMobileNumberWithOTP(String OTP, String mobileNo, String productName, String userType,
			String redirectUrl) throws MessagingException, IOException {
		Object obj = wrapperEntityManager
				.createNativeQuery(
						"SELECT u.mobileNo FROM user_details u JOIN tokens t ON u.id = t.userId WHERE t.otp =:OTP")
				.setParameter("OTP", OTP).getSingleResult();

		if (obj == null)
			return false;
		String mobNum = (String) obj;
		mobileNo = mobileNo.trim();
		if ((mobileNo).equals(mobNum)) {
			wrapperEntityManager.createNativeQuery("DELETE t.* FROM tokens t WHERE t.otp =:OTP")
					.setParameter("OTP", OTP).executeUpdate();
			return true;
		}
		return false;
	}

	@Override
	public SyncDetails syncDetails(String sessionId) {
		StoredProcedureQuery query = wrapperEntityManager.createStoredProcedureQuery("syncDetails", "sync_details")
				.registerStoredProcedureParameter("v_sessionId", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("v_tcps", Boolean.class, ParameterMode.OUT)
				.registerStoredProcedureParameter("v_appVersion", Boolean.class, ParameterMode.OUT)
				.registerStoredProcedureParameter("v_offers", Boolean.class, ParameterMode.OUT)
				.registerStoredProcedureParameter("v_messages", Boolean.class, ParameterMode.OUT)
				.setParameter("v_sessionId", sessionId);
		boolean tcps = (boolean) query.getOutputParameterValue("v_tcps");
		boolean appVersion = (boolean) query.getOutputParameterValue("v_appVersion");
		boolean offers = (boolean) query.getOutputParameterValue("v_offers");
		String messages = (String) query.getOutputParameterValue("v_messages");

		SyncDetails details = new SyncDetails(tcps, appVersion, offers, messages, "");
		return details;
	}

	@Override
	public User getByUsername(String username) {
		return jdbcTemplate.queryForObject(FETCH_BY_USERNAME, new Object[] { username }, new RowMapper<User>() {
			@Override
			public User mapRow(ResultSet resultSet, int i) throws SQLException {
				List<SimpleGrantedAuthority> grantedAuths = new ArrayList<>();
				grantedAuths.add(new SimpleGrantedAuthority(resultSet.getString("authorities")));
				System.out.println(grantedAuths.get(0));
				User user = new User(resultSet.getString("username"), resultSet.getString("password"), grantedAuths);
				return user;
			}
		});
	}

	@Override
	public BaseClientDetails getByClientId(String clientId) {
		return jdbcTemplate.queryForObject(FETCH_BY_CLIENT_ID, new Object[] { clientId, clientId, clientId },
				new RowMapper<BaseClientDetails>() {
					@Override
					public BaseClientDetails mapRow(ResultSet rs, int i) throws SQLException {
						BaseClientDetails details = new BaseClientDetails(rs.getString(1), rs.getString(3),
								rs.getString(4), rs.getString(5), null, null);
						details.setClientSecret(rs.getString(2));
						if (rs.getObject(6) != null) {
							details.setAccessTokenValiditySeconds(Integer.valueOf(rs.getInt(6)));
						}

						if (rs.getObject(7) != null) {
							details.setRefreshTokenValiditySeconds(Integer.valueOf(rs.getInt(7)));
						}

						// details.setAutoApproveScopes(null);
						return details;
					}
				});
	}

	private UserType getUserType(String product, String userType) {
		Object obj = wrapperEntityManager
				.createQuery(
						"SELECT u FROM UserType u JOIN FETCH u.product p WHERE p.name = :product AND u.type = :type")
				.setParameter("product", product).setParameter("type", userType).getSingleResult();
		if (obj == null)
			return null;
		return (UserType) obj;
	}

	@Override
	public int updateMobileNumber(String sessionId, String newMobileNumber, String otp) {

		Object obj = wrapperEntityManager
				.createNativeQuery("SELECT u.userId FROM user_session u WHERE u.sessionId =:sessionId")
				.setParameter("sessionId", sessionId).getSingleResult();
		if (obj == null)
			return -1;
		int user = (int) obj;
		Object obj1 = wrapperEntityManager
				.createNativeQuery(
						"SELECT t.otp FROM tokens t JOIN user_details u ON t.userId = u.id WHERE t.userId =:userId")
				.setParameter("userId", user).getSingleResult();
		if (obj1 == null)
			return 2;
		String dbOTP = (String) obj1;
		if (dbOTP.equals(otp)) {
			int result = wrapperEntityManager
					.createNativeQuery("UPDATE user_details u SET u.mobileNo =:mobileNo, isActive = 1 WHERE u.id =:userId")
					.setParameter("mobileNo", newMobileNumber.trim()).setParameter("userId", user).executeUpdate();
			wrapperEntityManager.createNativeQuery("DELETE t.* FROM tokens t WHERE t.otp =:OTP")
					.setParameter("OTP", otp).executeUpdate();
			return result;
		} else
			return -4;
	}

	@Transactional(value = "huddilTranscationManager")
	@Override
	public int updateHuddilMobileNumber(String newMobilenumber, String sessionId, String displayName) {
		Object obj = huddilEntityManager
				.createNativeQuery("SELECT u.userId FROM user_pref u WHERE u.sessionId =:sessionId")
				.setParameter("sessionId", sessionId).getSingleResult();
		if (obj == null)
			return -1;
		int user = (int) obj;
		if (newMobilenumber.equals(""))
			return huddilEntityManager
					.createNativeQuery("UPDATE user_pref p SET p.displayName =:displayName WHERE p.userId =:userId")
					.setParameter("displayName", displayName).setParameter("userId", user).executeUpdate();
		else
			return huddilEntityManager
					.createNativeQuery(
							"UPDATE user_pref p SET p.mobileNo = :mobileNo, p.mobileNoVerified = 1 WHERE p.userId =:userId")
					.setParameter("mobileNo", newMobilenumber.trim()).setParameter("userId", user).executeUpdate();
	}

	@Override
	public int getUserStatus(String data) {
		Object obj = wrapperEntityManager
				.createNativeQuery("SELECT isActive FROM wrapper.user_details WHERE emailId = :data")
				.setParameter("data", data).getSingleResult();
		if (obj == null)
			return -2;
		else
			return (int) obj;
	}

	@Override
	public int mobileNumberVerificationWithOTP(String sessionId, String otp, String mobileNo) {
		Object userId = wrapperEntityManager
				.createNativeQuery(
						"SELECT u.id FROM user_details u JOIN user_session s ON u.id = s.userId WHERE s.sessionId =:sessionId")
				.setParameter("sessionId", sessionId).getSingleResult();
		if (userId == null)
			return -1;
		int isActive = 0;
		isActive = (int) wrapperEntityManager
				.createNativeQuery("SELECT u.isActive FROM user_details u WHERE u.id =:userId")
				.setParameter("userId", userId).getSingleResult();
		if (isActive == 0) {
			Object obj = wrapperEntityManager
					.createNativeQuery(
							"SELECT t.otp FROM tokens t JOIN user_details u ON t.userId = u.id WHERE u.mobileNo =:mobileNo AND t.userId =:userId")
					.setParameter("mobileNo", mobileNo.trim()).setParameter("userId", (int) userId).getSingleResult();
			if (obj == null)
				return 4;
			if (!otp.equals(obj))
				return 1;
			wrapperEntityManager.createNativeQuery("UPDATE user_details u SET u.isActive = 1 WHERE u.id =:userId")
					.setParameter("userId", userId).executeUpdate();
			wrapperEntityManager.createNativeQuery("DELETE t.* FROM tokens t WHERE t.otp =:OTP")
					.setParameter("OTP", otp).executeUpdate();
			return 2;

		} else if (isActive == 1)
			return 3;
		else
			return -2;
	}
}
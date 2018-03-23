package myapps.solutions.wrapper.dao.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import myapps.solutions.wrapper.dao.ITermsConditionsDAO;
import myapps.solutions.wrapper.model.TermsConditions;
import myapps.solutions.wrapper.utils.UserType;

@Repository
@Transactional(value = "wrapperTranscationManager")
public class TermsConditionsDAOImpl implements ITermsConditionsDAO {

	@PersistenceContext(unitName = "wrapper")
	private EntityManager entityManager;

	/* insert the data in termsAndConditions */
	@Override
	public boolean insertTermsAndConditionsDetails(String sessionId, MultipartFile tcpsFile, int userType,
			int productId, Date startDate, String title) throws FileNotFoundException {

		Calendar cal = Calendar.getInstance();
		String destinationDir = System.getProperty("user.home") + File.separator + "uploads" + File.separator;
		File uploadFolder = new File(destinationDir + cal.get(Calendar.YEAR) + "-" + cal.get(Calendar.MONTH));
		if (!uploadFolder.exists())
			uploadFolder.mkdirs();
		File destinationFile = new File(uploadFolder + File.separator + System.currentTimeMillis()
				+ tcpsFile.getOriginalFilename().replaceAll("[^\\w.]", ""));
		try {
			tcpsFile.transferTo(destinationFile);
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
			return false;

		}

		String fileName = destinationFile.getAbsolutePath().substring((int) destinationDir.length());

		Object obj = entityManager
				.createNativeQuery(
						"SELECT s.productUserType FROM user_subscription s JOIN user_session u ON u.userId = s.userId WHERE u.sessionId = :sessionId")
				.setParameter("sessionId", sessionId).getSingleResult();
		if (obj == null || ((int) obj) != UserType.huddilAdministrator)
			return false;
		String description = null;
		String destinationDirectory = System.getProperty("user.home") + File.separator + "uploads" + File.separator;
		File file = new File(destinationDirectory + fileName);
		Scanner in = new Scanner(new FileReader(file));
		StringBuilder sb = new StringBuilder();
		while (in.hasNext()) {
			sb.append(in.nextLine());
		}
		in.close();
		description = sb.toString();
		entityManager
				.createNativeQuery(
						"INSERT INTO wrapper.terms_conditions(`title`,`description`, `userType`, `productId`, `startDate`) VALUES(:title, :desc, :user, :product, :startDate)")
				.setParameter("title", title).setParameter("desc", description).setParameter("user", userType)
				.setParameter("product", productId).setParameter("startDate", startDate).executeUpdate();
		return true;
	}

	/* Update the status in termsAndConditions based on sessionId,UserType */
	@Override
	public boolean updateTermsAndConditions(int status, String sessionId) {
		int UserType = (int) entityManager
				.createNativeQuery("SELECT u.userType FROM cashup.user_preferences u where u.sessionId= :sessionId")
				.setParameter("sessionId", sessionId).getSingleResult();
		if (UserType == 3) {
			return entityManager.createNativeQuery("UPDATE wrapper.terms_conditions p SET p.status =:status")
					.setParameter("status", status).executeUpdate() == 0 ? false : true;
		} else
			return false;
	}

	@Override
	public boolean updateTermsAndConditionsHistory(int terms_conditions_id, String sessionId) {
		int userId = (int) entityManager
				.createNativeQuery("SELECT u.userId FROM wrapper.user_session u WHERE u.sessionId= :sessionId")
				.setParameter("sessionId", sessionId).getSingleResult();
		TermsConditions id = entityManager.find(TermsConditions.class, terms_conditions_id);
		if(id == null)
			return false;
		if (userId != 0)
			return entityManager
					.createNativeQuery(
							"INSERT INTO terms_conditions_history (userId, terms_conditions_id) values (:userId, :terms_conditions_id)")
					.setParameter("terms_conditions_id", terms_conditions_id).setParameter("userId", userId)
					.executeUpdate() == 0 ? false : true;
		return false;
	}

	@Override
	public TermsConditions getTermsAndConditions(String userType, String product) {
		return (TermsConditions) entityManager
				.createNativeQuery(
						"SELECT t.id, t.description FROM terms_conditions t JOIN product p ON p.id = t.productId "
								+ "JOIN user_type u ON u.productId = p.id AND t.userType = u.id "
								+ "WHERE t.status = 1 AND u.type = :type AND p.name = :product",
						"tcps")
				.setParameter("type", userType).setParameter("product", product).getSingleResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TermsConditions> getTCPS(String sessionId, String product) {
		Object obj = entityManager
				.createNativeQuery(
						"SELECT productUserType FROM user_session s JOIN user_subscription u ON u.userId = s.userId WHERE s.sessionId = :sessionId")
				.setParameter("sessionId", sessionId).getSingleResult();
		List<TermsConditions> conditions = new ArrayList<TermsConditions>();
		if (obj == null)
			conditions.add(new TermsConditions(-1));
		else if (UserType.huddilAdministrator != (int) obj)
			conditions.add(new TermsConditions(-2));
		else
			conditions = entityManager
					.createQuery("SELECT t FROM TermsConditions t JOIN FETCH t.product p WHERE p.name = :name")
					.setParameter("name", product).getResultList();
		return conditions;
	}
}
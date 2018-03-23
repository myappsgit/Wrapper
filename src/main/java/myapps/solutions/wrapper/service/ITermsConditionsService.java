package myapps.solutions.wrapper.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import myapps.solutions.wrapper.model.TermsConditions;

public interface ITermsConditionsService {

	boolean insertTermsAndConditionsDetails(String sessionId, MultipartFile fileName, int userType, int productId, Date startDate, String title) throws IOException, FileNotFoundException;
	boolean updateTermsAndConditions(int id, String sessionId);
	boolean updateTermsAndConditionsHistory(int terms_conditions_id, String sessionId);
	TermsConditions getTermsAndConditions(String userType, String product);
	List<TermsConditions> getTCPS(String sessionId, String product);
}

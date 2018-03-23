package myapps.solutions.wrapper.service.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import myapps.solutions.wrapper.dao.ITermsConditionsDAO;
import myapps.solutions.wrapper.model.TermsConditions;
import myapps.solutions.wrapper.service.ITermsConditionsService;

@Service
// @Transactional("transactionManager")
public class TermsConditionsServiceImpl implements ITermsConditionsService {

	@Autowired
	ITermsConditionsDAO termsConditionsDAO;

	@Override
	public boolean insertTermsAndConditionsDetails(String sessionId, MultipartFile fileName, int userType,
			int productId, Date startDate, String title) throws IOException, FileNotFoundException {
		return termsConditionsDAO.insertTermsAndConditionsDetails(sessionId, fileName, userType, productId, startDate, title);
	}

	@Override
	public boolean updateTermsAndConditions(int id, String sessionId) {
		return termsConditionsDAO.updateTermsAndConditions(id, sessionId);
	}

	@Override
	public boolean updateTermsAndConditionsHistory(int terms_conditions_id, String sessionId) {
		return termsConditionsDAO.updateTermsAndConditionsHistory(terms_conditions_id, sessionId);
	}

	@Override
	public TermsConditions getTermsAndConditions(String userType, String product) {
		return termsConditionsDAO.getTermsAndConditions(userType, product);
	}

	@Override
	public List<TermsConditions> getTCPS(String sessionId, String product) {
		return termsConditionsDAO.getTCPS(sessionId, product);
	}
}

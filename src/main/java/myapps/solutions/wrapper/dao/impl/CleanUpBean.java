package myapps.solutions.wrapper.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;

@Component("cleanUpBean")
public class CleanUpBean {

	@PersistenceContext(unitName = "wrapper")
	private EntityManager entityManager;

	public void execCleanUp() {
		entityManager.createStoredProcedureQuery("dbCleanUp");
		/*StoredProcedureQuery query = entityManager.createStoredProcedureQuery("dbCleanUp");
		@SuppressWarnings("unchecked")
		List<String> locations = query.getResultList();
		if (!locations.isEmpty()) {
			for (String location : locations) {
				final File f = new File(location);
				if (f.exists())
					f.delete();
				else if (!f.getName().contains(".")) {
					final File folder = new File(f.getParent());
					final File[] files = folder.listFiles(new FilenameFilter() {
						public boolean accept(final File dir, final String name) {
							return name.matches(f.getName() + ".*\\.mp4");
						}
					});
					for (final File file : files)
						file.delete();
				}

			}
		}*/
	}
}

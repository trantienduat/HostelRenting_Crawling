package hibernate;

import java.io.File;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import entity.ProvinceEntity;


public class HibernateUtil {
	public static final SessionFactory SESSION_FACTORY = buildSessionFactory();

	private static SessionFactory buildSessionFactory() {
		try {
			return new Configuration().configure(new File("src/main/resources/hibernate.cfg.xml")).addPackage("entity").buildSessionFactory();
		} catch (Throwable e) {
			System.err.println("Initial SessionFactory creation failed! " + e);
			throw new ExceptionInInitializerError(e);
		}
	}
	
	public static SessionFactory getSessionFactory() {
		return SESSION_FACTORY;
	}
	
	public static void shutdown() {
		// Close caches and connection pools
		getSessionFactory().close();
	}
}

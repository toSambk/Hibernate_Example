package service;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class SessionFactoryInitializer {

    private static SessionFactory factory;

    static {
        Configuration configuration = new Configuration().configure();
        factory = configuration.buildSessionFactory();
    }

    public static SessionFactory getFactory() {
        return factory;
    }

}

package service;

import entity.User;
import entity.UserInfo;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class ApplicationUserServiceImpl implements ApplicationUserService {

   private SessionFactory factory;

   public ApplicationUserServiceImpl(SessionFactory factory) {
       this.factory = factory;
   }

    public boolean createUser(String login, String password) {
       if (login == null || password == null) return false;
       if (getUserList().stream().filter(x-> x.getUserLogin().equals(login)).count() > 0) {
            try {
                throw new MyTestException();
            } catch (MyTestException e) {
                System.out.println("Пользователь с таким логином уже существует");
                return false;
            }
        } else {
            User user = new User();
            user.setUserLogin(login);
            user.setPassword(password);
            Session session = factory.openSession();
            Transaction transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
            session.close();
            return true;
        }
    }

    public List<User> getUserList() {
       Session session = factory.openSession();
       List from_user = session.createQuery("from User", User.class).getResultList();
       session.close();
       return from_user;
    }

    @Override
    public boolean updateUser(String newLogin, String oldLogin, String password) {
       User user;
       if (newLogin == null || oldLogin == null || password == null) return false;

       try {
            user = getUserList().stream().filter(x -> x.getUserLogin().equals(oldLogin)).findFirst().orElseThrow(() -> new MyTestException());
        } catch (MyTestException e) {
            System.out.println("Пользователя с таким логином не существует. Необходимо сначала создать пользователя");
            return false;
        }
        user.setUserLogin(newLogin);
        user.setPassword(password);
        Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();
        session.update(user);
        transaction.commit();
        session.close();
        return true;
    }

    @Override
    public boolean deleteUser(String login) {
       User user;
       if (login == null) return false;
       try {
            user = getUserList().stream().filter(x -> x.getUserLogin().equals(login)).findFirst().orElseThrow(()->new MyTestException());
        } catch (MyTestException e) {
            System.out.println("Пользователя с таким логином не существует");
               return false;
        }
            Session session = factory.openSession();
            Transaction transaction = session.beginTransaction();
            session.delete(user);
            transaction.commit();
            session.close();
            return true;
    }

    @Override
    public User findByLogin(String login) throws MyTestException {
        if (login == null) throw new MyTestException();
        Session session = factory.openSession();
        User user = session.createQuery("from User where userLogin = :login", User.class)
                    .setParameter("login", login)
                    .getResultList()
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> {
                        session.close();
                        return new MyTestException();
                    });
        session.close();
        return user;
    }

    @Override
    public List<UserInfo> getUserInfoList() {
        Session session = factory.openSession();
        List from_userInfo = session.createQuery("from UserInfo", UserInfo.class).getResultList();
        session.close();
        return from_userInfo;
    }

    @Override
    public boolean passwordCorrect(String login, String password) {
       if (login == null || password == null) return false;
       User user;
        Session session = factory.openSession();
        try {
            user = session.createQuery("from User where userLogin = :login", User.class)
                    .setParameter("login", login)
                    .getResultList()
                    .stream()
                    .findFirst().orElseThrow(() -> {
                        session.close();
                        return new MyTestException();
                    });
        } catch (MyTestException e) {
            System.out.println("Пользователя с таким логином не существует");
            return false;
        }
        session.close();
        return user.getPassword().equals(password) ? true : false;
    }

    @Override
    public UserInfo getUserInfo(String login, String password) throws MyTestException {
       if (passwordCorrect(login, password)) {
           System.out.println("Пароль верен");
           UserInfo userInfo;
           Session session = factory.openSession();
               userInfo =  session
                       .createQuery("from UserInfo where user_id = :id", UserInfo.class)
                       .setParameter("id", findByLogin(login).getId())
                       .getResultList()
                       .stream()
                       .findFirst()
                       .orElseThrow(() -> {
                           session.close();
                           return new MyTestException();
               });
           session.close();
           return userInfo;
       } else throw new MyTestException();
    }

    @Override
    public void factoryClose() {
        factory.close();
    }

}

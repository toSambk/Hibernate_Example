package service;

import com.sun.org.apache.xpath.internal.operations.Bool;
import entity.User;
import entity.UserInfo;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.debugging.MockitoDebuggerImpl;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationUserServiceImplTest {

    @Mock
    private SessionFactory factory;
    @InjectMocks
    private ApplicationUserServiceImpl service;

    private Session session;
    private Transaction transaction;
    private Query query;
    private Query param;
    private Query paramInfo;
    private Query queryLogin;
    private Query queryLoginInfo;
    private Query userInfoQuery;

    @Before
    public void setup() {

        session = Mockito.mock(Session.class);
        transaction = Mockito.mock(Transaction.class);
        query = Mockito.mock(Query.class);
        param = Mockito.mock(Query.class);
        queryLogin = Mockito.mock(Query.class);
        userInfoQuery = Mockito.mock(Query.class);
        paramInfo = Mockito.mock(Query.class);
        queryLoginInfo = Mockito.mock(Query.class);
        Mockito.when(factory.openSession()).thenReturn(session);
        Mockito.when(session.beginTransaction()).thenReturn(transaction);
        Mockito.when(session.createQuery("from User", User.class)).thenReturn(query);
        Mockito.when(session.createQuery("from User where userLogin = :login", User.class)).thenReturn(param);
        Mockito.when(session.createQuery("from UserInfo where user_id = :id", UserInfo.class)).thenReturn(paramInfo);
        Mockito.when(param.setParameter(Mockito.anyString(), Mockito.anyString())).thenReturn(queryLogin);
        Mockito.when(paramInfo.setParameter(Mockito.anyString(), Mockito.anyString())).thenReturn(queryLoginInfo);
        Mockito.when(session.createQuery("from UserInfo", UserInfo.class)).thenReturn(userInfoQuery);
    }

    @Test
    public void testGetUserList() {
        List obj = service.getUserList();
        Mockito.verify(session, Mockito.times(1)).close();
        Assert.assertEquals(List.class, Arrays.stream(obj.getClass().getInterfaces()).findFirst().get());
    }

    @Test
    public void testCreateUser_UserIsAlreadyFound_returnFalse() {
        User user = new User();
        user.setId(1);
        user.setUserLogin("login");
        user.setPassword("password");
        List <User> list = new LinkedList<>();
        list.add(user);
        Mockito.when(query.getResultList()).thenReturn(list);
        boolean result =  service.createUser("login", "password");
        Assert.assertFalse(result);
    }

    @Test
    public void testCreateUser_LoginIsNull_returnFalse() {
        List <User> list = new LinkedList<>();
        Mockito.when(query.getResultList()).thenReturn(list);
        boolean result = service.createUser(null, null);
        Assert.assertFalse(result);
    }

    @Test
    public void testCreateUser_UserIsNotFound_returnTrue() {
        User user;
        ArgumentCaptor captor = ArgumentCaptor.forClass(User.class);
        List<User> list = new LinkedList<>();
        Mockito.when(query.getResultList()).thenReturn(list);
        boolean result = service.createUser("login", "password");
        Mockito.verify(session).save(captor.capture());
        user = (User)captor.getValue();
        Assert.assertTrue(User.class == user.getClass());
        Assert.assertTrue(user.getUserLogin() == "login");
        Assert.assertTrue(user.getPassword() == "password");
        Mockito.verify(transaction).commit();
        Mockito.verify(session, Mockito.times(2)).close();  //Сначала сессия закрывается в методе getUsers(), а затем в createUser
        Assert.assertTrue(result);
    }

    @Test
    public void testUpdateUser_userNotExists_returnFalse() {
        User user = new User();
        user.setUserLogin("login01");
        user.setPassword("password");
        List<User> list = new LinkedList<>();
        list.add(user);
        Mockito.when(query.getResultList()).thenReturn(list);
        boolean result = service.updateUser("newLogin", "login", "newPassword");
        Assert.assertFalse(result);
    }

    @Test
    public void testUpdateUser_loginIsNull_returnFalse() {
        List<User> list = new LinkedList<>();
        Mockito.when(query.getResultList()).thenReturn(list);
        boolean result = service.updateUser(null,null,null);
        Assert.assertFalse(result);
    }

    @Test
    public void testUpdateUser_userIsAlreadyExists_returnTrue() {
        User user = new User();
        ArgumentCaptor captor = ArgumentCaptor.forClass(User.class);
        user.setUserLogin("login");
        user.setPassword("password");
        List<User> list = new LinkedList<>();
        list.add(user);
        Mockito.when(query.getResultList()).thenReturn(list);
        boolean result = service.updateUser("newLogin", "login", "newPassword");
        Mockito.verify(session).update(captor.capture());
        user = (User) captor.getValue();
        Assert.assertTrue(user.getUserLogin() == "newLogin" && user.getPassword() == "newPassword" && user.getClass() == User.class);
        Mockito.verify(transaction).commit();
        Mockito.verify(session, Mockito.times(2)).close();
        Assert.assertTrue(result);
    }

    @Test
    public void testDeleteUser_loginIsNull_returnFalse() {
        boolean result = service.deleteUser(null);
        Assert.assertFalse(result);
    }

    @Test
    public void testDeleteUser_userIsNotExist_returnFalse() {
        User user = new User();
        user.setUserLogin("login01");
        user.setPassword("password");
        List<User> list = new LinkedList<>();
        list.add(user);
        Mockito.when(query.getResultList()).thenReturn(list);
        boolean result = service.deleteUser("login");
        Assert.assertFalse(result);
    }

    @Test
    public void testDeleteUser_userExists_returnTrue() {
        User user = new User();
        ArgumentCaptor captor = ArgumentCaptor.forClass(User.class);
        user.setUserLogin("login");
        user.setPassword("password");
        List<User> list = new LinkedList<>();
        list.add(user);
        Mockito.when(query.getResultList()).thenReturn(list);
        boolean result = service.deleteUser("login");
        Mockito.verify(session).delete(captor.capture());
        user = (User) captor.getValue();
        Assert.assertTrue(user.getUserLogin() == "login" && user.getClass() == User.class);
        Mockito.verify(transaction).commit();
        Mockito.verify(session, Mockito.times(2)).close();
        Assert.assertTrue(result);
    }

    @Test
    public void testFindByLogin_loginIsNull_returnException() {
        boolean result = true;
            try {
                service.findByLogin(null);
            } catch (MyTestException e) {
                result = false;
            }
        Assert.assertFalse(result);
    }

    @Test
    public void testFindByLogin_loginExists_returnUser() {
        boolean result = false;
        List<User> list = new LinkedList<>();
        User user = new User();
        user.setUserLogin("login");
        list.add(user);
        Mockito.when(queryLogin.getResultList()).thenReturn(list);
        User user1 = new User();
        try {
            user1 = service.findByLogin("login");
        } catch (MyTestException e) {
            result = false;
        }
        if(user1.getUserLogin() == "login") result = true;
        Assert.assertTrue(result);
        Mockito.verify(session).close();
    }

    @Test
    public void testFindByLogin_loginNotExist_returnException() {
        boolean result = true;
        List<User> list = new LinkedList<>();
        Mockito.when(queryLogin.getResultList()).thenReturn(list);
        try {
            User user = service.findByLogin("login");
        } catch (MyTestException e) {
            result = false;
        }
        Mockito.verify(session).close();
        Assert.assertFalse(result);
    }


    @Test
    public void testGetUserInfoList() {
        List obj = service.getUserInfoList();
        Mockito.verify(session, Mockito.times(1)).close();
        Assert.assertEquals(List.class, Arrays.stream(obj.getClass().getInterfaces()).findFirst().get());
    }

    @Test
    public void testPasswordCorrect_loginIsNull_returnFalse() {
        boolean result = service.passwordCorrect(null, null);
        Assert.assertFalse(result);
    }

    @Test
    public void testPasswordCorrect_userNotExist_returnFalse() {
        List<User> list = new LinkedList<>();
        Mockito.when(queryLogin.getResultList()).thenReturn(list);
        boolean result = service.passwordCorrect("login", "password");
        Mockito.verify(session).close();
        Assert.assertFalse(result);
    }

    @Test
    public void testPasswordCorrect_userExists_returnTrue() {
        List<User> list = new LinkedList<>();
        User user = new User();
        user.setUserLogin("login");
        user.setPassword("password");
        list.add(user);
        Mockito.when(queryLogin.getResultList()).thenReturn(list);
        boolean result = service.passwordCorrect("login", "password");
        Mockito.verify(session).close();
        Assert.assertTrue(result);
    }

    @Test
    public void testGetUserInfo_passwordNotCorrect_returnException() {
        boolean result = true;
        List<User> list = new LinkedList<>();
        User user = new User();
        user.setUserLogin("login01");
        user.setPassword("password01");
        list.add(user);
        Mockito.when(queryLogin.getResultList()).thenReturn(list);
        try {
            service.getUserInfo("login","password");
        } catch (MyTestException e) {
            result = false;
        }
        Mockito.verify(session).close();
        Assert.assertFalse(result);
    }

    /*
    @Test
    public void testGetUserInfo_userInfoNotExists_returnException() throws MyTestException {
        boolean result = true;
        User user = new User();
        user.setId(1);
        user.setUserLogin("login");
        user.setPassword("password");
        List<UserInfo>list = new LinkedList<>();
        UserInfo userInfo = new UserInfo();
        //userInfo.setUserId(1);
        //userInfo.setName("Alex");
        list.add(userInfo);
        ApplicationUserServiceImpl newService = Mockito.mock(ApplicationUserServiceImpl.class);
        Mockito.when(newService.passwordCorrect(anyString(), anyString())).thenReturn(true);
        Mockito.when(newService.findByLogin(anyString())).thenReturn(user);
        Mockito.when(queryLoginInfo.getResultList()).thenReturn(list);
        try {
            service.getUserInfo("login", "password");
        } catch (MyTestException e) {
            result = false;
        }
        Assert.assertFalse(result);
    }*/



}

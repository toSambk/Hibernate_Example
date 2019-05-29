import entity.User;
import entity.UserInfo;
import org.hibernate.SessionFactory;
import service.ApplicationUserServiceImpl;

import java.util.List;

import static service.SessionFactoryInitializer.getFactory;

public class Main {

    public static void main (String[] args) throws Exception {
        ApplicationUserServiceImpl service = new ApplicationUserServiceImpl(getFactory());
        //service.createUser("abba", "new_password");
        //service.updateUser("krueger", "abba", "256");
        //service.deleteUser("abba");
        /*try {
            service.findByLogin("erbte");
        } catch (Exception e) {
            System.out.println("Пользователя с таким именем не существует");
        }*/
        //service.passwordCorrect("Petr", "wefvwr");

       UserInfo userInfo =  service.getUserInfo("Petr", "12345");
       System.out.println(userInfo.getUserId() + "\t" + userInfo.getName() + "\t" + userInfo.getLastLogin() + "\t" + userInfo.getEmail());

        //service.getUserList().stream().forEach(x-> System.out.println(x.getId() + "\t" + x.getUserLogin() + "\t" + x.getPassword()));
        //service.getUserInfoList().stream().forEach(x-> System.out.println(x.getUserId() + "\t" + x.getName()));

    }

}

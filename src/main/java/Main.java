import entity.User;
import entity.UserInfo;
import service.ApplicationUserServiceImpl;
import java.util.Scanner;

import static service.SessionFactoryInitializer.getFactory;

public class Main {

    public static void main (String[] args) {
        ApplicationUserServiceImpl service = new ApplicationUserServiceImpl(getFactory());
        String login, newLogin, oldLogin, password = null;
        first :
        while(true) {
            Scanner in = new Scanner(System.in);
            System.out.println( "----------------------------------\n" + "Введите номер операции: \n" +
                    "1. Создать пользователя\n" +
                    "2. Изменить пользователя\n" +
                    "3. Получить список пользователей\n" +
                    "4. Удалить пользователя\n" +
                    "5. Найти пользователя по логину\n" +
                    "6. Получить информацию о пользователях\n" +
                    "7. Авторизация\n" + "----------------------------------\n");
            int num = in.nextInt();

            switch (num) {
                case 1:
                    System.out.println("Введите логин: ");
                    login = in.next();
                    System.out.println("Введите пароль: ");
                    password = in.next();
                    System.out.println(login + " " + password);
                    service.createUser(login, password);
                    break;

                case 2:
                    System.out.println("Введите новый логин: ");
                    newLogin = in.next();
                    System.out.println("Введите старый логин: ");
                    oldLogin = in.next();
                    System.out.println("Введите новый пароль: ");
                    password = in.next();
                    service.updateUser(newLogin, oldLogin, password);
                    break;

                case 3:
                    service.getUserList()
                            .stream()
                            .forEach(x -> System.out.println(x.getId() + "\t" + x.getUserLogin() + "\t" + x.getPassword()));
                    break;

                case 4:
                    System.out.println("Введите логин: ");
                    login = in.next();
                    service.deleteUser(login);
                    break;

                case 5:
                    System.out.println("Введите логин: ");
                    login = in.next();
                    User user;
                    try {
                        user = service.findByLogin(login);
                    } catch (Exception e) {
                        System.out.println("Пользователя с таким логином не существует");
                        break;
                    }
                    System.out.println("----------------------------------\n" + user.getId() + "\t" + user.getUserLogin() + "\t" + user.getPassword());
                    break;

                case 6:
                    System.out.println("----------------------------------\n");
                    service.getUserInfoList()
                            .stream()
                            .forEach(x -> System.out.println(x.getUserId() + "\t" + x.getName() + "\t" +
                                    "\t" + x.getLastLogin() + "\t" + x.getEmail() + "\t" + x.getPhone()));
                    break;

                case 7:
                    System.out.println("Введите логин: ");
                    login = in.next();
                    System.out.println("Введите пароль: ");
                    password = in.next();
                    UserInfo userInfo;
                    try {
                        userInfo = service.getUserInfo(login, password);
                        System.out.println("----------------------------------\n" + userInfo.getUserId() + "\t" + userInfo.getName() + "\t" +
                                "\t" + userInfo.getLastLogin() + "\t" + userInfo.getEmail() + "\t" + userInfo.getPhone());
                    } catch (Exception e) {
                        System.out.println("Неверный пароль либо информация о пользователе отсутствует");
                    }
                    break;

                case 8:
                   for (int i = 0; i < 1000000; i++) {
                       service.deleteUser("user" + i);
                   }
                    break;

                default:
                    in.close();
                    break first;
            }

        }
        service.factoryClose();
    }

}

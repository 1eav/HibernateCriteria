import enumeration.CourseType;
import jakarta.persistence.Parameter;
import jakarta.persistence.criteria.*;
import models.Course;
import models.Student;
import models.Teacher;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml").build();
        Metadata metadata = new MetadataSources(registry).getMetadataBuilder().build();
        SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        CriteriaBuilder builder = session.getCriteriaBuilder();

        String m1 = "_______________";
        String m2 = "CriteriaBuilder";
        String m3 = "===============";
        String m4 = "1 <| Простой WHERE запрос";
        String m5 = "2 <| Сложное WHERE условие";
        String m6 = "3 <| Использование параметров";
        String m7 = "4 <| Ограничение результатов";
        String m8 = "5 <| Использование OR";
        String m9 = "6 <| Использование BETWEEN";
        String m10 = "0 <| Выход";
        String m11 = "Выбор: ";
        Scanner input = new Scanner(System.in);
        char choice;
        do {
            System.out.println(m1);
            System.out.println(m2);
            System.out.println(m3);
            System.out.println(m4);
            System.out.println(m5);
            System.out.println(m6);
            System.out.println(m7);
            System.out.println(m8);
            System.out.println(m9);
            System.out.println(m10);
            System.out.println(m11);
            switch (choice = input.next().charAt(0)) {
                case '1':
                    CriteriaQuery<Course> query1 = builder.createQuery(Course.class);
                    Root<Course> courseRoot1 = query1.from(Course.class);
                    query1.select(courseRoot1);
                    query1.where(builder.greaterThan(courseRoot1.get("duration"), 50));
                    List<Course> resultList1 = session.createQuery(query1).getResultList();
                    resultList1.forEach(course -> System.out.println("Продолжительность: " + course.getDuration()));
                    break;
                case '2':
                    CriteriaQuery<Course> query2 = builder.createQuery(Course.class);
                    Root<Course> courseRoot2 = query2.from(Course.class);
                    query2.select(courseRoot2);
                    Predicate predicate = builder.and(builder.lessThan(courseRoot2.get("duration"), 40),
                            builder.greaterThan(courseRoot2.get("teacher").get("age"), 30));
                    query2.where(predicate);
                    List<Course> resultList2 = session.createQuery(query2).getResultList();
                    resultList2.forEach(course -> System.out.println("Продолжительность: " + course.getDuration() + " - " + "Возраст: " + course.getTeacher().getAge()));
                    break;
                case '3':
                    CriteriaQuery<Course> query3 = builder.createQuery(Course.class);
                    Root<Course> courseRoot3 = query3.from(Course.class);
                    query3.select(courseRoot3);
                    query3.where(builder.greaterThan(courseRoot3.get("price"), 120_000));
                    List<Course> resultList3 = session.createQuery(query3).getResultList();
                    resultList3.forEach(course -> System.out.println("Цена: " + course.getPrice()));
                    break;
                case '4':
                    CriteriaQuery<Course> query4 = builder.createQuery(Course.class);
                    Root<Course> courseRoot4 = query4.from(Course.class);
                    query4.select(courseRoot4);
                    query4.orderBy(builder.desc(courseRoot4.get("duration")));
                    List<Course> resultList4 = session.createQuery(query4).setMaxResults(5).getResultList();
                    resultList4.forEach((course -> System.out.println("Продолжительность: " + course.getDuration())));
                    break;
                case '5':
                    CriteriaQuery<Course> query5 = builder.createQuery(Course.class);
                    Root<Course> courseRoot = query5.from(Course.class);
                    query5.select(courseRoot);
                    Predicate predicate1 = builder.or(builder.greaterThan(courseRoot.get("duration"), 40),
                            builder.greaterThan(courseRoot.get("teacher").get("age"), 35));
                    query5.where(predicate1);
                    List<Course> resultList5 = session.createQuery(query5).getResultList();
                    resultList5.forEach(course -> System.out.println("Продолжительность: " + course.getDuration() + " - " + "Возраст: " + course.getTeacher().getAge()));
                    break;
                case '6':
                    CriteriaQuery<Course> query6 = builder.createQuery(Course.class);
                    Root<Course> courseRoot6 = query6.from(Course.class);
                    ParameterExpression<Integer> minPriceParameter = builder.parameter(Integer.class);
                    ParameterExpression<Integer> maxPriceParameter = builder.parameter(Integer.class);
                    query6.where(builder.between(courseRoot6.get("price"), minPriceParameter, maxPriceParameter));
                    List<Course> resultList6 = session.createQuery(query6).setParameter(minPriceParameter, 50_000)
                            .setParameter(maxPriceParameter, 150_000)
                            .getResultList();
                    resultList6.forEach(course -> System.out.println("Цена: " + course.getPrice()));
                    break;
                case '0':
                    break;
                default:
                    System.out.println("Ошибка ввода. Выберите категорию");
            }
        } while (choice != '0');
        transaction.commit();
        session.close();
        sessionFactory.close();
    }
}
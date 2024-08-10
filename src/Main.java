import java.util.List;
import java.util.Spliterator;

public class Main {
    public static void main(String[] args) {

        PersonController personController = new PersonController();

        //Person person = new Person();
        try {
            //person = new Person("aasd","asd","asd","asd@aasd.as","98082804935");
            //personController.create("aasd","qweqwe","asd","3asd@aasd.as","98082804935", false);
            /*List<Person> testList = personController.find(null,"aasd","asd",null,null,null);

            for (Person person : testList){
                System.out.println(person.toString());
            }*/
            //personController.remove("3");
            //personController.modify(null,"aasd","asd",null,null,null,null, "qwerty",null,"qwea@asd.as",null);
        }catch (Exception e){
            e.printStackTrace();
        }

        //System.out.println(person.toString());
    }
}
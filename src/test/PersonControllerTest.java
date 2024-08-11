import org.junit.Test;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import java.util.List;

import static org.junit.Assert.*;

public class PersonControllerTest {
    PersonController personController = new PersonController();

    @Test
    public void create_testFind() throws ParserConfigurationException, TransformerException {
        //Tworzymy nowego pracownika
        personController.create("zxc","cvb","rty","wee@asd.pa","52073089525", true);

        List<Person> foundPersonList = personController.find(null,"zxc",null,null,null, null);

        for (Person p : foundPersonList){
            assertEquals("zxc", p.firstName);
        }
    }

    @Test
    public void createFind_testRemove() throws ParserConfigurationException, TransformerException {
        //Tworzymy nowego pracownika
        personController.create("zxc","cvb","rty","wee@asd.pa","52073089525", true);

        //Sprawdzamy czy istnieje
        List<Person> foundPersonList = personController.find(null,"zxc",null,null,null, null);

        //Sprawdzamy czy udało się go usunąć
        for (Person p : foundPersonList){
            assertTrue(personController.remove(p.personID));
        }
        //Sprawdzamy czy został/li usunięty/ci
        foundPersonList = personController.find(null,"zxc",null,null,null, null);
        assertTrue(foundPersonList.isEmpty());
    }

    @Test
    public void createFind_testModifyOne() throws ParserConfigurationException, TransformerException {
        //Tworzymy nowego pracownika
        personController.create("zxc","cvb","rty","wee@asd.pa","52073089525", true);

        //Sprawdzamy czy istnieje
        List<Person> foundPersonList = personController.find(null,"zxc",null,null,null, null);
        assertFalse(foundPersonList.isEmpty());

        //Modyfikujemy pracownika
        personController.modify(null,"zxc",null,null,null, null,
                "ras",null,null, null,null);

        //Sprawdzamy czy istnieje ze starymi atrybutami
        foundPersonList = personController.find(null,"zxc",null,null,null, null);
        assertTrue(foundPersonList.isEmpty());

        //Sprawdzamy czy istnieje z nowymi
        foundPersonList = personController.find(null,"ras",null,null,null, null);
        assertFalse(foundPersonList.isEmpty());
    }
    @Test
    public void createFind_testModifyMore() throws ParserConfigurationException, TransformerException {
        //Tworzymy nowego pracownika
        personController.create("zxc","cvb","rty","wee@asd.pa","52073089525", true);
        personController.create("zxc","qweasd","asdasd","wee@asd.pa","89030214948", true);

        //Sprawdzamy czy istnieje
        List<Person> foundPersonList = personController.find(null,"zxc",null,null,null, null);
        assertFalse(foundPersonList.isEmpty());

        //Modyfikujemy pracowników
        personController.modify(null,"zxc",null,null,null, null,
                "ras",null,null, null,null);

        //Sprawdzamy czy istnieje ze starymi atrybutami
        foundPersonList = personController.find(null,"zxc",null,null,null, null);
        assertTrue(foundPersonList.isEmpty());

        //Sprawdzamy czy istnieje z nowymi
        foundPersonList = personController.find(null,"ras",null,null,null, null);
        assertFalse(foundPersonList.isEmpty());

        assertEquals(2,foundPersonList.size());
    }

    @Test
    public void createFind_testModifyError() throws ParserConfigurationException, TransformerException, IllegalArgumentException {
        //Tworzymy nowego pracownika
        personController.create("zxc","cvb","rty","wee@asd.pa","52073089525", true);

        //Sprawdzamy czy istnieje
        List<Person> foundPersonList = personController.find(null,"zxc",null,null,null, null);
        assertFalse(foundPersonList.isEmpty());

        //Modyfikujemy pracownika
        assertThrows(IllegalArgumentException.class, ()-> {
            personController.modify(null,"zxc",null,null,null, null,
                    null,null,null, "asdasdasd",null);
        });

    }

    @Test
    public void testCreate() throws ParserConfigurationException, TransformerException, IllegalArgumentException {
        //Tworzymy nowego pracownika
        personController.create("zxc1","cvb","rty","wee@asd.pa","52073089525", true);
        //Sprawdzamy czy istnieje
        List<Person> foundPersonList = personController.find(null,"zxc1",null,null,null, null);
        assertFalse(foundPersonList.isEmpty());
        //Tworzymy nowego pracownika z błędnym adresem email
        assertThrows(IllegalArgumentException.class, ()-> {
            personController.create("zxc2","cvb","rty","weeasd.pa","52073089525", true);
        });
        //Tworzymy nowego pracownika z błędnym peselem
        assertThrows(IllegalArgumentException.class, ()-> {
            personController.create("zxc3","cvb","rty","wee@asd.pa","asdasd", true);
        });
        //Tworzymy nowego pracownika z błędnym peselem i adresem email
        assertThrows(IllegalArgumentException.class, ()-> {
            personController.create("zxc4","cvb","rty","weeasd.pa","asdasd", true);
        });
    }
}
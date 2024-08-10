
//Klasa kontroler zachowan dla klasy Person
//Znajdz    find(String personId, String firstName, String lastName, String mobile, String email, String pesel)
//Dodaj     create(Person person, boolean isExternal)
//Usun      remove(String personID)
//Modyfikuj modify(Person person)

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PersonController {
    static final String EXTERNAL_DIR = "External";
    static final String INTERNAL_DIR = "Internal";

    //Znalezienie pracownika po dowolnym argumencie
    public List<Person> find(String personID, String firstName, String lastName, String mobile, String email, String pesel){
        //W podanym kryterium/ach może być więcej niż 1 pracownik robimy listę
        List<Person> people = new ArrayList<>();

        //Szukamy po dowolnym argumencie, musimy sprawdzić wszystkie pliki
        List<File> files = new ArrayList<>();
        files.addAll(listFile(EXTERNAL_DIR));
        files.addAll(listFile(INTERNAL_DIR));

        //Sprawdzamy ile pól ma wartość
        int controlNumber = 0;
        if (personID != null) controlNumber++;
        if (firstName != null) controlNumber++;
        if (lastName != null) controlNumber++;
        if (mobile != null) controlNumber++;
        if (email != null) controlNumber++;
        if (pesel != null) controlNumber++;
        int sumControl = controlNumber;

        //przeglądamy każdy z plików
        for(File file : files){
            try {
                //reset licznika
                controlNumber = sumControl;

                //parsujemy plik
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
                Document document = documentBuilder.parse(file);
                document.getDocumentElement().normalize();

                //Łapiemy atrybuty node'a <person>
                NodeList nodeList = document.getElementsByTagName("person");
                for (int temp  = 0; temp  < nodeList.getLength(); temp ++){
                    Node node = nodeList.item(temp);
                    if(node.getNodeType() == Node.ELEMENT_NODE){
                        Element element = (Element) node;
                        //sprawdzamy czy osoba z pliku spełnia wymagania

                        if (personID != null && element.getElementsByTagName("personID").item(0).getTextContent().equals(personID)){
                            controlNumber--;
                        }
                        if (firstName != null && element.getElementsByTagName("firstName").item(0).getTextContent().equals(firstName)) {
                            controlNumber--;
                        }
                        if (lastName != null && element.getElementsByTagName("lastName").item(0).getTextContent().equals(lastName)) {
                            controlNumber--;
                        }
                        if (mobile != null && element.getElementsByTagName("mobile").item(0).getTextContent().equals(mobile)) {
                            controlNumber--;
                        }
                        if (email != null && element.getElementsByTagName("email").item(0).getTextContent().equals(email)) {
                            controlNumber--;
                        }
                        if (pesel != null && element.getElementsByTagName("pesel").item(0).getTextContent().equals(pesel)) {
                            controlNumber--;
                        }

                        if (controlNumber == 0){
                            people.add(new Person(
                                    element.getElementsByTagName("personID").item(0).getTextContent(),
                                    element.getElementsByTagName("firstName").item(0).getTextContent(),
                                    element.getElementsByTagName("lastName").item(0).getTextContent(),
                                    element.getElementsByTagName("mobile").item(0).getTextContent(),
                                    element.getElementsByTagName("email").item(0).getTextContent(),
                                    element.getElementsByTagName("pesel").item(0).getTextContent()
                            ));
                        }
                    }
                }
            }catch (Exception e) {e.printStackTrace();}
        }
        return people;
    }
    //====================================================================

    //====================Utworzenie nowego pracownika=====================
    public void create(String firstName, String lastName, String mobile, String email, String pesel, boolean isExternal) throws ParserConfigurationException, TransformerException {
        //sprawdzamy czy jest pracownikiem zewnętrznym
        String dir = isExternal ? EXTERNAL_DIR : INTERNAL_DIR;
        //Tworzymy obiekt i ustalamy ścieżkę + nazwę pliku
        Person person = new Person(firstName, lastName, mobile, email, pesel);
        String path = dir + "/" + person.personID + ".xml";

        //Tworzymy strukturę pliku xml
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
        Document document = documentBuilder.newDocument();
        //xml root <person>
        Element rootElement = document.createElement("person");
        document.appendChild(rootElement);
        //Dodajemy elementy do root
        Element element = document.createElement("personID");
        element.appendChild(document.createTextNode(person.personID));
        rootElement.appendChild(element);
        element = document.createElement("firstName");
        element.appendChild(document.createTextNode(person.firstName));
        rootElement.appendChild(element);
        element = document.createElement("lastName");
        element.appendChild(document.createTextNode(person.lastName));
        rootElement.appendChild(element);
        element = document.createElement("mobile");
        element.appendChild(document.createTextNode(person.mobile));
        rootElement.appendChild(element);
        element = document.createElement("email");
        element.appendChild(document.createTextNode(person.email));
        rootElement.appendChild(element);
        element = document.createElement("pesel");
        element.appendChild(document.createTextNode(person.pesel));
        rootElement.appendChild(element);

        //Stworzenie pliku xml
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.STANDALONE, "no");
        DOMSource source = new DOMSource(document);
        StreamResult streamResult = new StreamResult(new File(path));

        transformer.transform(source, streamResult);
    }
    //=====================================================================

    //====================Usunięcie pracownika===============================
    public boolean remove(String personID){
        File file = findFile(EXTERNAL_DIR, personID);
        if (file == null){
            file = findFile(INTERNAL_DIR, personID);
            if (file == null){
                return false;
            }
        }
        return file.delete();
    }
    //=====================================================================

    //==================Zmodyfikowanie pracowanika========================
    public void modify(String personID, String firstName, String lastName, String mobile, String email, String pesel,
                       String modifyFirstName, String modifyLastName, String modifyMobile, String modifyEmail, String modifyPesel){
        //Połączenie metody find() i create()

        //Szukamy po dowolnym argumencie, musimy sprawdzić wszystkie pliki
        List<File> files = new ArrayList<>();
        files.addAll(listFile(EXTERNAL_DIR));
        files.addAll(listFile(INTERNAL_DIR));

        //Sprawdzamy ile pól ma wartość
        int controlNumber = 0;
        if (personID != null) controlNumber++;
        if (firstName != null) controlNumber++;
        if (lastName != null) controlNumber++;
        if (mobile != null) controlNumber++;
        if (email != null) controlNumber++;
        if (pesel != null) controlNumber++;
        int sumControl = controlNumber;

        //przeglądamy każdy z plików
        for(File file : files){
            try {
                //reset licznika
                controlNumber = sumControl;

                //parsujemy plik
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
                Document document = documentBuilder.parse(file);
                document.getDocumentElement().normalize();

                //Łapiemy atrybuty node'a <person>
                NodeList nodeList = document.getElementsByTagName("person");
                for (int temp  = 0; temp  < nodeList.getLength(); temp ++){
                    Node node = nodeList.item(temp);
                    if(node.getNodeType() == Node.ELEMENT_NODE){
                        Element element = (Element) node;
                        //sprawdzamy czy osoba z pliku spełnia wymagania

                        if (personID != null && element.getElementsByTagName("personID").item(0).getTextContent().equals(personID)){
                            controlNumber--;
                        }
                        if (firstName != null && element.getElementsByTagName("firstName").item(0).getTextContent().equals(firstName)) {
                            controlNumber--;
                        }
                        if (lastName != null && element.getElementsByTagName("lastName").item(0).getTextContent().equals(lastName)) {
                            controlNumber--;
                        }
                        if (mobile != null && element.getElementsByTagName("mobile").item(0).getTextContent().equals(mobile)) {
                            controlNumber--;
                        }
                        if (email != null && element.getElementsByTagName("email").item(0).getTextContent().equals(email)) {
                            controlNumber--;
                        }
                        if (pesel != null && element.getElementsByTagName("pesel").item(0).getTextContent().equals(pesel)) {
                            controlNumber--;
                        }

                        if (controlNumber == 0){
                            if (modifyFirstName != null) element.getElementsByTagName("firstName").item(0).setTextContent(modifyFirstName);
                            if (modifyLastName != null) element.getElementsByTagName("lastName").item(0).setTextContent(modifyLastName);
                            if (modifyMobile != null) element.getElementsByTagName("mobile").item(0).setTextContent(modifyMobile);
                            if (modifyEmail != null) element.getElementsByTagName("email").item(0).setTextContent(modifyEmail);
                            if (modifyPesel != null) element.getElementsByTagName("pesel").item(0).setTextContent(modifyPesel);

                            TransformerFactory transformerFactory = TransformerFactory.newInstance();
                            Transformer transformer = transformerFactory.newTransformer();
                            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                            transformer.setOutputProperty(OutputKeys.STANDALONE, "no");
                            DOMSource domSource = new DOMSource(document);
                            StreamResult result = new StreamResult(file);
                            transformer.transform(domSource,result);
                        }
                    }
                }
            }catch (Exception e) {e.printStackTrace();}
        }
    }
    //=================================================================

    //=================metody pomocnicze ==================
    //=========Lista wszystkich plików xml z lokalizacji=====
    List<File> listFile(String dir){
        //Filtrujemy listę wszystkich plików z lokalizacji i bierzemy tylko .xml
        File[] allFiles = new File(dir).listFiles();
        List<File> xmlFiles = new ArrayList<>();
        if(allFiles != null){
            for (File file : allFiles){
                if (file.isFile() && file.getName().endsWith(".xml")){
                    xmlFiles.add(file);
                }
            }
        }
        return xmlFiles;
    }
    //================================================================
    //============Plik xml z lokalizacji==================
    File findFile(String dir, String personID){
        File[] allFiles = new File(dir).listFiles();
        File returnFile = null;
        if(allFiles != null){
            for (File file : allFiles){
                if (file.isFile() && file.getName().endsWith(personID + ".xml")){
                    returnFile = file;
                }
            }
        }
        return returnFile;
    }
    //=========================================================
    //=========================================================
}

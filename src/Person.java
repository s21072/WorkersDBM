// Klasa pracownika

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Person {
    String personID;
    String firstName;
    String lastName;
    String mobile;
    String email;
    String pesel;
    //Nie określamy wewnetrzny/zewnetrzny tutaj przez trzymanie danych w odzielnych plikach
    static final Pattern PESEL_PATTERN = Pattern.compile("\\d{4}[0-3]\\d{6}");
    static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    static final String EXTERNAL_DIR = "External";
    static final String INTERNAL_DIR = "Internal";

    //konstruktory
    //pusty
    public Person(){}
    //Do tworzenia nowych pracowników
    public Person(String firstName, String lastName, String mobile, String email, String pesel) {
        this.personID = setPersonId();
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobile = mobile;
        setEmail(email);
        setPesel(pesel);
    }
    //Do wyszukiwania / testów
    public Person(String personId, String firstName, String lastName, String mobile, String email, String pesel) {
        this.personID = personId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobile = mobile;
        this.email = email;
        this.pesel = pesel;
    }
    //===============================================================

    //================ Gettery & Settery ===========================
    public String getPersonId() {
        return personID;
    }

    public String setPersonId() {
        String personID = "1";
        List<File> files = new ArrayList<>();
        files.addAll(listFile(EXTERNAL_DIR));
        files.addAll(listFile(INTERNAL_DIR));

        for(File file : files){
            String fileName = file.getName().split("\\.")[0];
            if(Integer.parseInt(personID) < Integer.parseInt(fileName)+1){
                personID = String.valueOf(Integer.parseInt(fileName)+1);
            }
        }
        return personID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if(email == null || !EMAIL_PATTERN.matcher(email).matches()){
            throw new IllegalArgumentException(email + " is wrong. Please check formating.");
        }
        this.email = email;
    }

    public String getPesel() {
        return pesel;
    }

    public void setPesel(String pesel) {
        if(pesel == null || !PESEL_PATTERN.matcher(pesel).matches()){
            throw new IllegalArgumentException(pesel + " is wrong. Please check formating.");
        }
        this.pesel = pesel;
    }
    //=================================================

    //==========toString==============================
    @Override
    public String toString() {
        return "Person{" +
                "personID='" + personID + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", mobile='" + mobile + '\'' +
                ", email='" + email + '\'' +
                ", pesel='" + pesel + '\'' +
                '}';
    }
    //================================================
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

    //=========================================================

}

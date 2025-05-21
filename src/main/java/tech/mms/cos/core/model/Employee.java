package tech.mms.cos.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import tech.mms.cos.exception.AppValidationException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Objects;
import java.util.UUID;


/*
- Profil einfügen
- Zugriff auf 8080 (expose)
- Umgebungsvariablen hängen an der Session (google wird die umgebungsvariable an den container mitgegeben)

- alternative: docker compose
- docker compose file schreiben weche den container startet (das imageeee startet)
- 8080 soll exposed werden
- umgebugnsvariable soll übergeben werden (mongo_url)-------------> ich darf hardcoden




- Dockerfile rauskopieren und in die lokale Dockerfile (hier) übertragen -> commit and push
- Im Github Action Build das Docker image baust (docker build . -t xcc-employee-app) nur wenn der branch master oder develop ist
- Gib dem image folgende tags
        - commit sha
        - master als tag falls branch master
        - develop asl tag falls develop
- Upload des Docker images auf docker hub (registrieren, neues image anlegen, hol dir einen api key, api key in github hinterlegen)

// Docker Image über die VM ziehen, die Anwendung darüber laufen lassen und mit curl (8080) getDummy in die Datenbank speichern

TODO: Also das secret sich zu holen geht jetzt mit "gcloud secrets versions access latest --secret=xcc-employee-mongo-pw"

 In dem Verzeichnis wo die docker-compose.yml liegt soll ich ein Skript anlegen was folgendermaßen heißt: startup.sh
export MONGO_PW=$(gcloud secrets versions access latest --secret=xcc-employee-mongo-pw)
sudo docker compose up -d

IN Startup Script von GCloud sudo startup.sh starten (sudo chmod +x startup.sh) -> Test: Fahr VM per UI runter und wieder hoch -> Geh in die VM sudo docker ps

restart.sh
-> Docker compose runterfahren
-> Wiederhochfahren

-> Wenn du ein Tag in Github/Git anlegst, triggert ein neuer Workflow
   -> Läuft nur wenn "Branch ist master / develop" UND der Tag hat format "x.x.x" (CMd check if string is semantic versioning)
   -> Bauen der Anwendung, Bauen des Image, Hochladen des Image mit Tag "1.0.0"



Manueller Workflow erstellen
Der VM soll manuell eine Version gegeben werden
Ändere Datei welche Versionsnummer enthält ab zu "VERSION" (in VM) und restart Docker Container

1. Geh in die VM und ändere das Docker Compose File ab, sodass es nicht "latest" verwendet, sondern eine Umgebungsvariable welche "DEPLOYED_IMAGE_TAG" heißt. ${DEPLOYED_IMAGE_TAG] heißt.
2. Lege eine neue Datei an die heißt "deployed_version.txt" welche den Text "0.0.2" beinhaltet (direkt wo die docker compose file auch liegt)
3. Neue Zeile zu startup-sh hinzufügen, welche DEPLOYED_IMAGE_TAG als Umgebungsvariable setzt, indem es die Datei "deployed_version.txt" ausliest.

---

1. Neuen GitHub Workflow anlegen, welcher manuell getriggered wird mit Variable "VERSION" als Freitext-Feld.
2. github.com/google-github-actions/setup-gcloud



Setze NGINX auf der VM auf. Leite jeglichen Traffic von 80 auf 8080 um.




 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Employee {

    @Id
    private String uuid;

    private LocalDate birthdate;
    private double hourlyRate;
    private int hoursPerWeek;
    private Genders gender;
    private Name name;
    private String department;



    public Employee(LocalDate birthdate, double hourlyRate, int hoursPerWeek, Genders gender, Name name, String department) {
        this.birthdate = birthdate;
        this.hourlyRate = hourlyRate;
        this.hoursPerWeek = hoursPerWeek;
        this.gender = gender;
        this.name = name;
        this.department = department;
        this.uuid = UUID.randomUUID().toString();

        validate();
    }

    private Employee() {

    }

    @Override
    public String toString() {
        return getFullName() + "\t |" + getAge() + "yo\t |" + getGender();
    }


    public static int MINIMUM_AGE = 14;

    public void validate() throws AppValidationException {
        this.name.validate();

        if (getHoursPerWeek() < 0) {
            throw new AppValidationException(EmployeeErrorMessage.hoursPerWeekIsNegativ);
        }
        if (getHourlyRate() < 0) {
            throw new AppValidationException(EmployeeErrorMessage.hourlyRateIsNegative);
        }
        if (birthdate.isAfter(LocalDate.now())) {
            throw new AppValidationException(EmployeeErrorMessage.birthdateIsInFuture);
        } else if (this.getAge() < MINIMUM_AGE) {
            throw new AppValidationException(EmployeeErrorMessage.employeeTooYoung);
        }
    }


    @JsonIgnore
    public int getAge() {
        LocalDate currentDate = LocalDate.from(LocalDateTime.now());
        return Period.between(birthdate, currentDate).getYears();
    }

    @JsonIgnore
    public int getBirthYear() {
        return birthdate.getYear();
    }

    @JsonIgnore
    public String getFullName() {
        return name.getFullName();
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public int getHoursPerWeek() {
        return hoursPerWeek;
    }

    public double getMonthlySalary() {
        return getHoursPerWeek() * 4 * getHourlyRate();
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public Genders getGender() {
        return gender;
    }

    public Name getName() {
        return name;
    }

    public String getUuid() {
        return uuid;
    }

    public String getDepartment() {
        return department;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee employee)) return false;
        return Objects.equals(uuid, employee.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(uuid);
    }


    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public void setHoursPerWeek(int hoursPerWeek) {
        this.hoursPerWeek = hoursPerWeek;
    }

    public void setGender(Genders gender) {
        this.gender = gender;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void updateAllFields(LocalDate birthdate, double hourlyRate, int hoursPerWeek,
                                Genders gender, Name name, String department) {
        this.birthdate = birthdate;
        this.hourlyRate = hourlyRate;
        this.hoursPerWeek = hoursPerWeek;
        this.gender = gender;
        this.name = name;
        this.department = department;


        validate();
    }
}


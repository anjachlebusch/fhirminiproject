package fhir_project;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.r4.model.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

public class FHIRUebung7 {
    public static void main(String[] args) {
        // Create a client
        FhirContext ctx = FhirContext.forR4();
        IGenericClient client = ctx.newRestfulGenericClient("https://funke.imi.uni-luebeck.de/public/fhir");

        //first bulletpoint - create Patient
		createPatient(ctx, client);

        //second bulletpoint - list all patients in file
        readAllPatientsAndNamesWriteToFile(ctx, client);

        //third bulletpoint - create organization
        Organization organization = createOrganization(ctx, client, "Geburtsstation \"MIO\" Hosptial Luebeck");

        //fourth bulletpoint - create doctor and nurse
        createCareTeam(ctx, client, organization);

        //fifth bulletpoint - create encounter
        createEncounter(ctx, client);
    }

    /**
     * Create a patient and write patient in json format to file "patient.json"
     */
    public static void createPatient(FhirContext ctx, IGenericClient client) {
        // Create a patient
        Patient newPatient = new Patient();

        // Populate the patient with information
        int value = new Random().nextInt();
        newPatient
                .addIdentifier()
                .setSystem("http://www.kh-uzl.de/fhir/patients")
                .setValue(Integer.toString(value));
        newPatient.setGender(Enumerations.AdministrativeGender.FEMALE);
        newPatient.addName().addGiven("Antonie").setFamily("Grünlich");
        newPatient.setBirthDateElement(new DateType("1827-08-06"));

        // Create the resource on the server
        MethodOutcome outcome = client
                .create()
                .resource(newPatient)
                .execute();

        // Log the ID that the server assigned
        String id = outcome.getId().toString();
        Patient createdPatient = getPatientById(client, id);
        if(createdPatient != null){
            exportToJsonFile(ctx, createdPatient);
        }
    }

    private static Patient getPatientById(IGenericClient client, String patientId) {
        try {
            return client.read().resource(Patient.class).withUrl(patientId).execute();
        } catch (ResourceNotFoundException e) {
            System.out.println("Resource not found!");
            return null;
        }
    }

    /**
     * helper for writing resource to Json-File
     * @param ctx - FHIR Context
     * @param resource - Resource to print in json
     */
    private static void exportToJsonFile(FhirContext ctx, DomainResource resource) {
        try {
            int value = new Random().nextInt();
            FileWriter fileWriter = new FileWriter(value + ".json");
            ctx.newJsonParser()
                    .setPrettyPrint(true)
                    .encodeResourceToWriter(resource, fileWriter);
        } catch (IOException exception) {
            System.out.println("An error occurred.");
            exception.printStackTrace();
        }
    }

    public static void readAllPatientsAndNamesWriteToFile(FhirContext ctx, IGenericClient client) {
        Bundle patients;
        try {
            patients = client
                    .search()
                    .forResource(Patient.class)
                    .returnBundle(Bundle.class)
                    .elementsSubset("name")
                    .count(354)
                    .execute();


        } catch (ResourceNotFoundException e) {
            System.out.println("Resource not found!");
            return;
        }


        try {

            FileWriter patientFileWriter = new FileWriter("patients.json");

            ctx.newJsonParser()
                    .setPrettyPrint(true)
                    .encodeResourceToWriter(patients, patientFileWriter);

        } catch (IOException exception) {
            System.out.println("An error occurred.");
            exception.printStackTrace();
        }

    }

    /**
     * Create an organization
     * @param ctx fhir context
     * @param client fhir client
     * @param name name of the organization
     */
    public static Organization createOrganization(FhirContext ctx, IGenericClient client, String name) {
        // Create an organization
        Organization organization = new Organization();

        // add alias to organization
        organization.addAlias(name);

        // Create the resource on the server
        MethodOutcome outcome = client
                .create()
                .resource(organization)
                .execute();

        // Log the ID that the server assigned
        String id = outcome.getId().toString();
        Organization createdOrganization = getOrganizationById(client, id);
        exportToJsonFile(ctx, createdOrganization);
        return createdOrganization;
    }

    /**
     * gets organization by id
     * @param client
     * @param organizationId
     * @return Organization object
     */
    private static Organization getOrganizationById(IGenericClient client, String organizationId) {
        try {
            return client.read().resource(Organization.class).withId(organizationId).execute();
        } catch (ResourceNotFoundException e) {
            System.out.println("Resource not found!");
            return null;
        }
    }

    /**
     * Create careTeam
     * @param ctx fhir context
     * @param client fhir client
     */
    public static void createCareTeam(FhirContext ctx, IGenericClient client, Organization organization) {
        // Create a careTeam
        CareTeam careTeam = new CareTeam();
        CareTeam.CareTeamParticipantComponent doctor = createDoctor();
        CareTeam.CareTeamParticipantComponent nurse = createNurse();

        // add attributes to careTeam
        careTeam.addManagingOrganization(new Reference(organization))
                .addParticipant(doctor).addParticipant(nurse);

        // Create the resource on the server
        MethodOutcome outcome = client
                .create()
                .resource(careTeam)
                .execute();

        // Log the ID that the server assigned
        String id = outcome.getId().toString();
        CareTeam createdCareTeam = getCareTeamById(client, id);
        exportToJsonFile(ctx, createdCareTeam);
    }

    private static CareTeam.CareTeamParticipantComponent createDoctor() {
        CareTeam.CareTeamParticipantComponent doctor = new CareTeam.CareTeamParticipantComponent();
        CodeableConcept doctorRole = new CodeableConcept().setText("Doctor");
        ArrayList roleList = new ArrayList<CodeableConcept>();
        roleList.add(doctorRole);
        doctor.setRole(roleList);
        return doctor;
    }

    private static CareTeam.CareTeamParticipantComponent createNurse() {
        CareTeam.CareTeamParticipantComponent nurse = new CareTeam.CareTeamParticipantComponent();
        CodeableConcept nurseRole = new CodeableConcept().setText("Nurse");
        ArrayList roleList = new ArrayList<CodeableConcept>();
        roleList.add(nurseRole);
        nurse.setRole(roleList);
        return nurse;
    }

    /**
     * gets careTeam by id
     * @param client
     * @param careTeamId
     * @return CareTeam object
     */
    private static CareTeam getCareTeamById(IGenericClient client, String careTeamId) {
        try {
            return client.read().resource(CareTeam.class).withId(careTeamId).execute();
        } catch (ResourceNotFoundException e) {
            System.out.println("Resource not found!");
            return null;
        }
    }

    /**
     * Create Encounter
     * @param ctx fhir context
     * @param client fhir client
     */
    public static void createEncounter(FhirContext ctx, IGenericClient client) {
        // Create an Encounter
        Encounter encounter = new Encounter();

        Coding snomedCodeEncounterForSign = new Coding()
                .setCode("185346005")
                .setSystem("SNOMED-CT");
        ArrayList codeList = new ArrayList<Coding>();
        codeList.add(snomedCodeEncounterForSign);
        CodeableConcept reasonCode = new CodeableConcept()
                .setCoding(codeList)
                .setText("Birth");
        Appointment appointment = new Appointment()
                .setStart(new GregorianCalendar(1846, Calendar.OCTOBER, 1).getTime());

        // add attributes to Encounter
        ArrayList reasoncodeList = new ArrayList<CodeableConcept>();
        reasoncodeList.add(reasonCode);
        ArrayList referenceList = new ArrayList<Reference>();
        referenceList.add(new Reference(appointment));
        encounter.setStatus(Encounter.EncounterStatus.PLANNED)
                .setReasonCode(reasoncodeList)
                .setAppointment(referenceList);

        // Create the resource on the server
        MethodOutcome outcome = client
                .create()
                .resource(encounter)
                .execute();

        // Log the ID that the server assigned
        String id = outcome.getId().toString();
        Encounter createdEncounter = getEncounterById(client, id);
        exportToJsonFile(ctx, createdEncounter);
    }

    /**
     * gets encounter by id
     * @param client
     * @param encounterId
     * @return Encounter object
     */
    private static Encounter getEncounterById(IGenericClient client, String encounterId) {
        try {
            return client.read().resource(Encounter.class).withId(encounterId).execute();
        } catch (ResourceNotFoundException e) {
            System.out.println("Resource not found!");
            return null;
        }
    }
}

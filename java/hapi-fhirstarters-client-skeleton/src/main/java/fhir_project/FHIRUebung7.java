package fhir_project;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.r4.model.codesystems.ObservationCategory;

import java.util.*;

public class FHIRUebung7 {
    public static void main(String[] args) {

        // Create a client
        FhirContext ctx = FhirContext.forR4();
        IGenericClient client = ctx.newRestfulGenericClient("https://funke.imi.uni-luebeck.de/public/fhir");

       //Patient
       Patient antonie = createPatient(client);

       //Blutgruppe
       Observation bloodType = createBloodTypeForPatient(client, antonie);



       //Arztpraxis
       Organization doctorsOffice = createDoctorsOffice(client);

       //Arzt
       Practitioner doctor = createDoctor(client);

      //Arzt-Rolle
       PractitionerRole doctorRole = createDoctorRole(client, doctor, doctorsOffice);



      //Appointents
       Appointment vaccine1Appointment = createAppointment(client, new GregorianCalendar(1846, Calendar.OCTOBER, 1).getTime());
       Appointment vaccine2Appointment = createAppointment(client, new GregorianCalendar(1842, Calendar.APRIL, 12).getTime());
       Appointment vaccine3Appointment = createAppointment(client, new GregorianCalendar(1835, Calendar.JUNE, 23).getTime());
       Appointment vaccine4Appointment = createAppointment(client, new GregorianCalendar(1828, Calendar.JANUARY, 2).getTime());
       Appointment vaccine5Appointment = createAppointment(client, new GregorianCalendar(1830, Calendar.SEPTEMBER, 25).getTime());
       Appointment vaccine6Appointment = createAppointment(client, new GregorianCalendar(1852, Calendar.JULY, 12).getTime());

       Appointment covidAntiGenAppointment = createAppointment(client, new GregorianCalendar(1853, Calendar.SEPTEMBER, 5).getTime());
       Appointment roetelnAntiGenAppointment = createAppointment(client, new GregorianCalendar(1828, Calendar.FEBRUARY, 15).getTime());

       //Encounter - Impfung1
       Encounter vaccine1Encounter = createVaccineEncounter(client, vaccine1Appointment, antonie, doctor);

       //Encounter - Impfung2
       Encounter vaccine2Encounter = createVaccineEncounter(client, vaccine2Appointment, antonie, doctor);

       //Encounter - Impfung3
       Encounter vaccine3Encounter = createVaccineEncounter(client, vaccine3Appointment, antonie, doctor);

       //Encounter - Impfung4
       Encounter vaccine4Encounter = createVaccineEncounter(client, vaccine4Appointment, antonie, doctor);

       //Encounter - Impfung5
       Encounter vaccine5Encounter = createVaccineEncounter(client, vaccine5Appointment, antonie, doctor);

       //Encounter - Impfung6
       Encounter vaccine6Encounter = createVaccineEncounter(client, vaccine6Appointment, antonie, doctor);

       //Encounter - Anti-Körper Röteln
       Encounter roetelnEncounter = createAntiGenTestEncounter(client, roetelnAntiGenAppointment, antonie, doctor);

       //Encounter - Anti-Körper COVID
       Encounter covidEncounter = createAntiGenTestEncounter(client, covidAntiGenAppointment, antonie, doctor);

       // Impfung1
       CodeableConcept vaccineCode1 = new CodeableConcept().setCoding(Collections.singletonList(new Coding("http://hl7.org/fhir/sid/cvx", "140", "Influenza, seasonal, injectable, preservative free")));
       DateTimeType occurenceDate1 = new DateTimeType(new GregorianCalendar(1846, Calendar.OCTOBER, 1));
       Immunization vaccine1 = createImmunization(client, antonie, doctor, vaccine1Encounter, vaccineCode1, "123456", occurenceDate1);

       // Impfung2
       CodeableConcept vaccineCode2 = new CodeableConcept().setCoding(Collections.singletonList(new Coding("http://hl7.org/fhir/sid/cvx", "07", "mumps virus vaccine")));
       DateTimeType occurenceDate2 = new DateTimeType(new GregorianCalendar(1842, Calendar.APRIL, 12));
       Immunization vaccine2 = createImmunization(client, antonie, doctor, vaccine2Encounter, vaccineCode2, "98765", occurenceDate2);

       // Impfung3
       CodeableConcept vaccineCode3 = new CodeableConcept().setCoding(Collections.singletonList(new Coding("http://hl7.org/fhir/sid/cvx", "67", "malaria vaccine")));
       DateTimeType occurenceDate3 = new DateTimeType(new GregorianCalendar(1835, Calendar.JUNE, 23));
       Immunization vaccine3 = createImmunization(client, antonie, doctor, vaccine3Encounter, vaccineCode3, "98765", occurenceDate3);

       // Impfung4
       CodeableConcept vaccineCode4 = new CodeableConcept().setCoding(Collections.singletonList(new Coding("http://hl7.org/fhir/sid/cvx", "102", "DTP- Haemophilus influenzae type b conjugate and hepatitis b vaccine")));
       DateTimeType occurenceDate4 = new DateTimeType(new GregorianCalendar(1828, Calendar.JANUARY, 2));
       Immunization vaccine4 = createImmunization(client, antonie, doctor, vaccine4Encounter, vaccineCode4, "98765", occurenceDate4);

       // Impfung5
       CodeableConcept vaccineCode5 = new CodeableConcept().setCoding(Collections.singletonList(new Coding("http://hl7.org/fhir/sid/cvx", "162", "meningococcal B vaccine, fully recombinant")));
       DateTimeType occurenceDate5 = new DateTimeType(new GregorianCalendar(1930, Calendar.SEPTEMBER, 25));
       Immunization vaccine5 = createImmunization(client, antonie, doctor, vaccine5Encounter, vaccineCode5, "243546", occurenceDate5);

       // Impfung6
       CodeableConcept vaccineCode6 = new CodeableConcept().setCoding(Collections.singletonList(new Coding("http://hl7.org/fhir/sid/cvx", "140", "Influenza, seasonal, injectable, preservative free")));
       DateTimeType occurenceDate6 = new DateTimeType(new GregorianCalendar(1852, Calendar.JULY, 12));
       Immunization vaccine6 = createImmunization(client, antonie, doctor, vaccine6Encounter, vaccineCode6, "123456", occurenceDate6);

       //Anti-Körper-Test - Röteln
       //TODO: Titer und Methode
       CodeableConcept roetelnTest = new CodeableConcept(new Coding("http://loinc.org", "74415-1","display: Rubella virus IgG Ab [Presence] in Body fluid by Immunoassay"));
       CodeableConcept roetelnResult = new CodeableConcept(
             new Coding("http://snomed.info/sct", "260385009", "Negative (qualifier value)")
          ).setText("Schutz nicht vorhanden");
       Observation immunizationTestRoeteln = createImmunizationTest(client, antonie, doctor, roetelnEncounter, roetelnTest, roetelnResult);

       //Anti-Körper-Test - COVID
       CodeableConcept covidTest = new CodeableConcept(new Coding("http://loinc.org", "95209-3", "SARS-CoV+SARS-CoV-2 (COVID-19) Ag [Presence] in Respiratory specimen by Rapid immunoassay"));
       CodeableConcept covidResult = new CodeableConcept(
             new Coding("http://snomed.info/sct", "10828004", "Positive (qualifier value)")
          ).setText("Schutz vorhanden");
       Observation immunizationTestCovid = createImmunizationTest(client, antonie, doctor, covidEncounter, covidTest, covidResult);
       Composition c =createComposition(client,ctx,antonie,doctor,vaccine1);
    }

    //TODO: wie viele Antikörper-Tests / Impfungen


   private static Patient createPatient(IGenericClient client) {
      Patient antonie = new Patient();

      // Identifier
      Identifier identifier = new Identifier();
      identifier.setValue("1234567890");
      identifier.setSystem("http://kh-uzl.de/fhir/patients");
      antonie.addIdentifier(identifier);

      // Official Name
      HumanName gruenlich = new HumanName();
      gruenlich.addGiven("Antonie");
      gruenlich.setFamily("Grünlich");
      gruenlich.setUse(HumanName.NameUse.OFFICIAL);
      antonie.addName(gruenlich);

      // Birthday
      Calendar cal = Calendar.getInstance();
      // CAVE: Java integer starts by 0!
      cal.set(1827, 7, 6);
      antonie.setBirthDate(cal.getTime());

      //Birthplace
      antonie.addExtension().setUrl("http://hl7.org/fhir/StructureDefinition/patient-birthPlace")
         .setValue(new StringType("Hamburg"));

      //Address
      antonie.addAddress(
         new Address()
            .setLine(Collections.singletonList(new StringType("Stresemannstraße 12")))
            .setPostalCode("22179")
            .setCity("Hamburg")
      );

      //Passport number
      antonie.addExtension().setUrl("http://acme.org/fhir/StructureDefinition/passport-number")
         .setValue(new StringType("12345ABC"));

      //Riskfactors for immunization
      //TODO: brauchen wir Risikofaktoren?
      RiskAssessment risikoFaktoren = new RiskAssessment();

      MethodOutcome patientOutcome = createResource(client, antonie);
      antonie.setId(patientOutcome.getId());

      return antonie;
   }

   private static Observation createBloodTypeForPatient(IGenericClient client, Patient patient) {
      //Blood type
      Observation bloodType = new Observation()
         .setStatus(Observation.ObservationStatus.FINAL)
         .setCategory(
            Collections.singletonList(new CodeableConcept().setCoding(
               Collections.singletonList(new Coding("http://terminology.hl7.org/CodeSystem/observation-category", "Laboratory", "The results of observations generated by laboratories. Laboratory results are typically generated by laboratories providing analytic services in areas such as chemistry, hematology, serology, histology, cytology, anatomic pathology (including digital pathology), microbiology, and/or virology. These observations are based on analysis of specimens obtained from the patient and submitted to the laboratory."))
            ))
         )
         .setCode(
            new CodeableConcept().setCoding(
               Collections.singletonList(new Coding("http ://loinc.org", "882-1", "ABO and Rh group [Type] in Blood"))
            )
         )
         .setSubject(new Reference(patient))
         .setEffective(new DateTimeType(new GregorianCalendar(1900, Calendar.APRIL, 12)))
         .setValue(new CodeableConcept().setCoding(
            Arrays.asList(new Coding("http://snomed.info/sct", "112144000", "Blood group A (finding)"),
               new Coding("http://snomed.info/sct", "165747007", "RhD positive (finding)")))
         );
      MethodOutcome bloodTypeOutcome = createResource(client, patient);
      bloodType.setId(bloodTypeOutcome.getId());
      return bloodType;
   }

   private static Organization createDoctorsOffice(IGenericClient client) {
      Organization doctorsOffice = new Organization()
         .addAlias("Schöne Praxis")
         .addAddress(
            new Address()
               .setLine(Collections.singletonList(new StringType("Max-Brauer-Allee 122")))
               .setPostalCode("22179")
               .setCity("Hamburg")
         )
         .addTelecom(new ContactPoint()
            .setSystem(ContactPoint.ContactPointSystem.PHONE)
            .setValue("040/678123"));

      MethodOutcome arztpraxisOutcome = createResource(client, doctorsOffice);
      doctorsOffice.setId(arztpraxisOutcome.getId());
      return doctorsOffice;
   }

   private static Practitioner createDoctor(IGenericClient client) {
      Practitioner doctor = new Practitioner()
         .addName(new HumanName()
            .addPrefix("Dr.")
            .addGiven("Frauke")
            .setFamily("Lehmann")
         );
      MethodOutcome doctorOutcome = createResource(client, doctor);
      doctor.setId(doctorOutcome.getId());
      return doctor;
   }

   private static PractitionerRole createDoctorRole(IGenericClient client, Practitioner doctor, Organization doctorsOffice) {
      PractitionerRole doctorRole = new PractitionerRole()
         .setPractitionerTarget(doctor).setPractitioner(new Reference(doctor))
         .setOrganizationTarget(doctorsOffice).setOrganization(new Reference(doctorsOffice))
         .addCode(new CodeableConcept(new Coding("http://hl7.org/fhir/ValueSet/practitioner-role", "doctor",
         "A qualified/registered medical practitioner")));
      MethodOutcome doctorRoleOutcome = createResource(client, doctorRole);
      doctorRole.setId(doctorRoleOutcome.getId());
      return doctorRole;
   }

   private static Appointment createAppointment(IGenericClient client, Date date) {
      Appointment appointment = new Appointment()
         .setStart(date);
      appointment.setId(IdType.newRandomUuid());
      MethodOutcome vaccine1AppointmentOutcome = createResource(client, appointment);
      appointment.setId(vaccine1AppointmentOutcome.getId());
      return appointment;
   }

   private static Encounter createVaccineEncounter(IGenericClient client, Appointment appointment, Patient patient,
                                                   Practitioner doctor) {
      Encounter encounter = new Encounter()
         .setStatus(Encounter.EncounterStatus.FINISHED)
         .setClass_(new Coding("http://terminology.hl7.org/ValueSet/v3-ActEncounterCode", "AMB",
            "A comprehensive term for health care provided in a healthcare facility (e.g. a practitioneraTMs office, clinic setting, or hospital) on a nonresident basis. The term ambulatory usually implies that the patient has come to the location and is not assigned to a bed. Sometimes referred to as an outpatient encounter."))
         .setServiceType(
            new CodeableConcept(new Coding("http://hl7.org/fhir/ValueSet/service-type", "57", "Immunization")))
         .setSubject(new Reference(patient))
         .addParticipant(new Encounter.EncounterParticipantComponent().setIndividual(new Reference(doctor)))
         .addReasonCode(
            new CodeableConcept(
               new Coding("http://snomed.info/sct", "185346005", "Encounter for sign (procedure)")
            ).setText("Immunization")
         )
         .addAppointment(new Reference(appointment));

      MethodOutcome vaccineEncounterOutcome = createResource(client, encounter);
      encounter.setId(vaccineEncounterOutcome.getId());
      return encounter;
   }

   private static Encounter createAntiGenTestEncounter(IGenericClient client, Appointment appointment, Patient patient,
                                                   Practitioner doctor) {
      Encounter encounter = new Encounter()
         .setStatus(Encounter.EncounterStatus.FINISHED)
         .setClass_(new Coding("http://terminology.hl7.org/ValueSet/v3-ActEncounterCode", "AMB",
            "A comprehensive term for health care provided in a healthcare facility (e.g. a practitioneraTMs office, clinic setting, or hospital) on a nonresident basis. The term ambulatory usually implies that the patient has come to the location and is not assigned to a bed. Sometimes referred to as an outpatient encounter."))
         .setServiceType(
            new CodeableConcept(new Coding("http://hl7.org/fhir/ValueSet/service-type", "162", "Pathology - Immunology")))
         .setSubject(new Reference(patient))
         .addParticipant(new Encounter.EncounterParticipantComponent().setIndividual(new Reference(doctor)))
         .addReasonCode(
            new CodeableConcept(
               new Coding("http://snomed.info/sct", "185346005", "Encounter for sign (procedure)")
            ).setText("anti-gen test")
         )
         .addAppointment(new Reference(appointment));

      MethodOutcome antigentestEncounterOutcome = createResource(client, encounter);
      encounter.setId(antigentestEncounterOutcome.getId());
      return encounter;
   }

   private static Immunization createImmunization(IGenericClient client, Patient patient, Practitioner doctor, Encounter encounter,
                                                  CodeableConcept vaccineCode, String lotNumber, DateTimeType occurence) {
      Immunization vaccine = new Immunization()
         .setPatient(new Reference(patient))
         .setVaccineCode(vaccineCode)
         .setLotNumber(lotNumber)
         .setOccurrence(occurence)
         .setPerformer(Collections.singletonList(new Immunization.ImmunizationPerformerComponent(new Reference(doctor))))
         .setEncounter(new Reference(encounter));
      //.setManufacturer(/*TODO: manufacturer oder Name der Impfung?*/)

      MethodOutcome vaccineOutcome = createResource(client, vaccine);
      vaccine.setId(vaccineOutcome.getId());
      return vaccine;
   }

   private static Observation createImmunizationTest(IGenericClient client, Patient patient, Practitioner performer, Encounter encounter,
                                                     CodeableConcept testType, CodeableConcept testResult) {
      Observation immunizationTest = new Observation()
         .addCategory(
            new CodeableConcept(
               new Coding("http://terminology.hl7.org/CodeSystem/observation-category", ObservationCategory.LABORATORY.toCode(), ObservationCategory.LABORATORY.getDisplay())
            ))
         .setCode(testType)
         .setSubject(new Reference(patient))
         .addPerformer(new Reference(performer))
         .setValue(testResult)
         .setEncounter(new Reference(encounter));

      MethodOutcome immunizationTestRoetelnOutcome = createResource(client, immunizationTest);
      immunizationTest.setId(immunizationTestRoetelnOutcome.getId());
      return immunizationTest;
   }

   private static MethodOutcome createResource(IGenericClient client, Resource res)
   {
      MethodOutcome outcome = client.create()
         .resource(res)
         .prettyPrint()
         .encodedJson()
         .execute();
      System.out.println("ID of created Resource: " + outcome.getId());
      return outcome;
   }

   private static Composition createComposition(IGenericClient client,FhirContext ctx,Patient patient, Practitioner performer,Immunization I) {
      Composition comp = new Composition();
      //comp.addSection().addEntry().setResource(new AllergyIntolerance().addNote(new Annotation().setText("Section0_Allergy0")));
      comp.addSection().addEntry().setResource(new AllergyIntolerance().addNote(new Annotation().setText("Section1_Allergy0")));
      comp.addSection().addEntry().setResource(patient);
      comp.addSection().addEntry().setResource(I);
      IParser parser = ctx.newJsonParser().setPrettyPrint(true);
      String string = parser.encodeResourceToString(comp);


      Composition parsed = parser.parseResource(Composition.class, string);
      parsed.getSection().remove(0);

      string = parser.encodeResourceToString(parsed);


      parsed = parser.parseResource(Composition.class, string);
      MethodOutcome compOutcome = createResource(client, comp);
      comp.setId(compOutcome.getId());
      return comp;
   }



}

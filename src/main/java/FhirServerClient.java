// hapi-fhir stuff
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.gclient.ICriterion;
import ca.uhn.fhir.rest.gclient.StringClientParam;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Patient;

// basic java
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class FhirServerClient {

    private static FhirServerClient instance = null;

    private final FhirContext context;
    private IGenericClient client;

    // constructor
    private FhirServerClient() {
        this.context = FhirContext.forDstu3();
        this.client = context.newRestfulGenericClient("http://localhost:8080/baseDstu3/");
    }

    public static FhirServerClient getInstance() {
        if (instance == null) {
            instance = new FhirServerClient();
        }
        return instance;
    }

    // search for given surname on server
    private Bundle searchForBundles(String surname) {
        ICriterion criterion = new StringClientParam("name").matches().value(surname);
        return client.search()
                .forResource(Patient.class)
                .where(criterion)
                .returnBundle(Bundle.class)
                .execute();
    }

    // convert Bundle type to Patient type
    private List<Patient> convertBundlesToList(Bundle bundle) {
        List<Patient> patientsList = new ArrayList<>();
        List<Patient> tmpPatients;

        while(true) {
            tmpPatients = bundle.getEntry()
                    .stream()
                    .map(e -> (Patient) e.getResource())
                    .collect(Collectors.toList());
            patientsList.addAll(tmpPatients);
            if(bundle.getLink(Bundle.LINK_NEXT) == null) break;
            bundle = client.loadPage().next(bundle).execute();
        }
        return patientsList;
    }

    public List<Patient> getPatients(String surname) {
        Bundle fetchBundles = searchForBundles(surname);
        List<Patient> patientsList = convertBundlesToList(fetchBundles);
        return patientsList;
    }
}
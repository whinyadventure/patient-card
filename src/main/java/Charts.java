import org.hl7.fhir.dstu3.model.Observation;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Charts {
    private static Charts instance = null;  // only one instance at the moment


    private List<Observation> observations;
    private  Map<String, List<SingleObservation>> mappedObservations;

    public static Charts getInstance() {
            if (instance == null) {
                instance = new Charts();
            }
            return instance;
        }

        public  void setObservations(List<Observation> observations){
            this.observations = observations;
            setMappedObservations(observations);
        }

        public  void setMappedObservations(List<Observation> observations){
            Map<String, List<SingleObservation>> tmp = observations.stream()
                    .filter(Observation::hasValueQuantity)
                    .map(SingleObservation::new)
                    .collect(Collectors.groupingBy(SingleObservation::getCategory));
            this.mappedObservations = tmp;
        }

        public Map<String, List<SingleObservation>> getMappedObservations(){
            return this.mappedObservations;
        }

        public List<Observation> getObservations(){
            return this.observations;
        }
}


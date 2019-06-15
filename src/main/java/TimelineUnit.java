import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Builder(toBuilder = true) @Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class TimelineUnit {

    private int id;
    private String title;
    private String details;
    private Date dateTime;
    private String status;
}
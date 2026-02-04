package huynhtanhao.hutech.haohuynh.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Contact {
    private String fullName;
    private String email;
    private String message;
    private LocalDate submissionDate;
}

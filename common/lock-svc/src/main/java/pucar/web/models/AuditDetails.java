package pucar.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuditDetails {
    @JsonProperty("createdBy")
    @Column(name = "createdBy")
    private String createdBy = null;

    @Column(name = "lastModifiedBy")
    @JsonProperty("lastModifiedBy")
    private String lastModifiedBy = null;

    @Column(name = "createdTime")
    @JsonProperty("createdTime")
    private Long createdTime = null;

    @Column(name = "lastModifiedTime")
    @JsonProperty("lastModifiedTime")
    private Long lastModifiedTime = null;
}
package org.pucar.dristi.web.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.egov.common.contract.models.AuditDetails;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Validated
@JsonIgnoreProperties(
        ignoreUnknown = true
)
public class Identifier {
    @JsonProperty("id")
    private @Size(
            min = 2,
            max = 64
    ) String id = null;
    @JsonProperty("clientReferenceId")
    private @Size(
            min = 2,
            max = 64
    ) String clientReferenceId = null;
    @JsonProperty("individualId")
    private @Size(
            min = 2,
            max = 64
    ) String individualId = null;
    @JsonProperty("identifierType")
    private @NotNull @Size(
            min = 2,
            max = 64
    ) String identifierType = null;
    @JsonProperty("identifierId")
    private @NotNull @Size(
            min = 2,
            max = 64
    ) String identifierId = null;
    @JsonProperty("isDeleted")
    private Boolean isDeleted;
    @JsonProperty("auditDetails")
    private @Valid AuditDetails auditDetails;

    public static IdentifierBuilder builder() {
        return new IdentifierBuilder();
    }

    public String getId() {
        return this.id;
    }

    public String getClientReferenceId() {
        return this.clientReferenceId;
    }

    public String getIndividualId() {
        return this.individualId;
    }

    public String getIdentifierType() {
        return this.identifierType;
    }

    public String getIdentifierId() {
        return this.identifierId;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public AuditDetails getAuditDetails() {
        return this.auditDetails;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("clientReferenceId")
    public void setClientReferenceId(String clientReferenceId) {
        this.clientReferenceId = clientReferenceId;
    }

    @JsonProperty("individualId")
    public void setIndividualId(String individualId) {
        this.individualId = individualId;
    }

    @JsonProperty("identifierType")
    public void setIdentifierType(String identifierType) {
        this.identifierType = identifierType;
    }

    @JsonProperty("identifierId")
    public void setIdentifierId(String identifierId) {
        this.identifierId = identifierId;
    }

    @JsonProperty("isDeleted")
    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    @JsonProperty("auditDetails")
    public void setAuditDetails(AuditDetails auditDetails) {
        this.auditDetails = auditDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Identifier)) return false;
        Identifier other = (Identifier) o;
        if (!other.canEqual(this)) return false;

        Object this$isDeleted = this.getIsDeleted();
        Object other$isDeleted = other.getIsDeleted();
        if (this$isDeleted != null ? !this$isDeleted.equals(other$isDeleted) : other$isDeleted != null) return false;

        Object this$id = this.getId();
        Object other$id = other.getId();
        if (this$id != null ? !this$id.equals(other$id) : other$id != null) return false;

        Object this$clientReferenceId = this.getClientReferenceId();
        Object other$clientReferenceId = other.getClientReferenceId();
        if (this$clientReferenceId != null ? !this$clientReferenceId.equals(other$clientReferenceId) : other$clientReferenceId != null) return false;

        Object this$individualId = this.getIndividualId();
        Object other$individualId = other.getIndividualId();
        if (this$individualId != null ? !this$individualId.equals(other$individualId) : other$individualId != null) return false;

        Object this$identifierType = this.getIdentifierType();
        Object other$identifierType = other.getIdentifierType();
        if (this$identifierType != null ? !this$identifierType.equals(other$identifierType) : other$identifierType != null) return false;

        Object this$identifierId = this.getIdentifierId();
        Object other$identifierId = other.getIdentifierId();
        if (this$identifierId != null ? !this$identifierId.equals(other$identifierId) : other$identifierId != null) return false;

        Object this$auditDetails = this.getAuditDetails();
        Object other$auditDetails = other.getAuditDetails();
        if (this$auditDetails != null ? !this$auditDetails.equals(other$auditDetails) : other$auditDetails != null) return false;

        return true;
    }

    protected boolean canEqual(Object other) {
        return other instanceof Identifier;
    }

    @Override
    public int hashCode() {
        int result = 1;
        Object $isDeleted = this.getIsDeleted();
        result = result * 59 + ($isDeleted == null ? 43 : $isDeleted.hashCode());
        Object $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        Object $clientReferenceId = this.getClientReferenceId();
        result = result * 59 + ($clientReferenceId == null ? 43 : $clientReferenceId.hashCode());
        Object $individualId = this.getIndividualId();
        result = result * 59 + ($individualId == null ? 43 : $individualId.hashCode());
        Object $identifierType = this.getIdentifierType();
        result = result * 59 + ($identifierType == null ? 43 : $identifierType.hashCode());
        Object $identifierId = this.getIdentifierId();
        result = result * 59 + ($identifierId == null ? 43 : $identifierId.hashCode());
        Object $auditDetails = this.getAuditDetails();
        result = result * 59 + ($auditDetails == null ? 43 : $auditDetails.hashCode());
        return result;
    }

    public String toString() {
        return "Identifier(id=" + this.getId() + ", clientReferenceId=" + this.getClientReferenceId() + ", individualId=" + this.getIndividualId() + ", identifierType=" + this.getIdentifierType() + ", identifierId=" + this.getIdentifierId() + ", isDeleted=" + this.getIsDeleted() + ", auditDetails=" + this.getAuditDetails() + ")";
    }

    public Identifier() {
        this.isDeleted = Boolean.FALSE;
        this.auditDetails = null;
    }

    public Identifier(String id, String clientReferenceId, String individualId, String identifierType, String identifierId, Boolean isDeleted, AuditDetails auditDetails) {
        this.isDeleted = Boolean.FALSE;
        this.auditDetails = null;
        this.id = id;
        this.clientReferenceId = clientReferenceId;
        this.individualId = individualId;
        this.identifierType = identifierType;
        this.identifierId = identifierId;
        this.isDeleted = isDeleted;
        this.auditDetails = auditDetails;
    }

    public static class IdentifierBuilder {
        private String id;
        private String clientReferenceId;
        private String individualId;
        private String identifierType;
        private String identifierId;
        private Boolean isDeleted;
        private AuditDetails auditDetails;

        IdentifierBuilder() {
        }

        @JsonProperty("id")
        public IdentifierBuilder id(String id) {
            this.id = id;
            return this;
        }

        @JsonProperty("clientReferenceId")
        public IdentifierBuilder clientReferenceId(String clientReferenceId) {
            this.clientReferenceId = clientReferenceId;
            return this;
        }

        @JsonProperty("individualId")
        public IdentifierBuilder individualId(String individualId) {
            this.individualId = individualId;
            return this;
        }

        @JsonProperty("identifierType")
        public IdentifierBuilder identifierType(String identifierType) {
            this.identifierType = identifierType;
            return this;
        }

        @JsonProperty("identifierId")
        public IdentifierBuilder identifierId(String identifierId) {
            this.identifierId = identifierId;
            return this;
        }

        @JsonProperty("isDeleted")
        public IdentifierBuilder isDeleted(Boolean isDeleted) {
            this.isDeleted = isDeleted;
            return this;
        }

        @JsonProperty("auditDetails")
        public IdentifierBuilder auditDetails(AuditDetails auditDetails) {
            this.auditDetails = auditDetails;
            return this;
        }

        public Identifier build() {
            return new Identifier(this.id, this.clientReferenceId, this.individualId, this.identifierType, this.identifierId, this.isDeleted, this.auditDetails);
        }

        public String toString() {
            return "Identifier.IdentifierBuilder(id=" + this.id + ", clientReferenceId=" + this.clientReferenceId + ", individualId=" + this.individualId + ", identifierType=" + this.identifierType + ", identifierId=" + this.identifierId + ", isDeleted=" + this.isDeleted + ", auditDetails=" + this.auditDetails + ")";
        }
    }
}

package digit.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.common.contract.request.Role;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

/**
 * This is acting ID token of the authenticated user on the server. Any value provided by the clients will be ignored and actual user based on authtoken will be used on the server.
 */
@Schema(description = "This is acting ID token of the authenticated user on the server. Any value provided by the clients will be ignored and actual user based on authtoken will be used on the server.")
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-15T13:15:39.759211883+05:30[Asia/Kolkata]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {


    @JsonProperty("tenantId")
    @NotNull
    private String tenantId = null;

    @JsonProperty("id")
    private Integer id = null;

    @JsonProperty("uuid")
    private String uuid = null;

    @JsonProperty("userName")
    @NotNull
    private String userName = null;

    @JsonProperty("mobileNumber")
    private String mobileNumber = null;

    @JsonProperty("emailId")
    private String emailId = null;

    @JsonProperty("roles")
    @NotNull
    @Valid
    private List<Role> roles = new ArrayList<>();


    public User addRolesItem(Role rolesItem) {
        this.roles.add(rolesItem);
        return this;
    }

}

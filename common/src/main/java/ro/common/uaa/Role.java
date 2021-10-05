/* Licensed under Apache-2.0 */
package ro.common.uaa;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Role bean */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role {
  @JsonProperty private String authority;
}

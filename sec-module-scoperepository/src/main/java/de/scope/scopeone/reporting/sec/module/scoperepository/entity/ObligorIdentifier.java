package de.scope.scopeone.reporting.sec.module.scoperepository.entity;

import com.google.common.base.MoreObjects;
import de.scope.scopeone.reporting.sec.module.scoperepository.entity.enums.ObligorOrIssuerIdentifierSchemeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "OBLIGOR_IDENTIFIER")
public class ObligorIdentifier {

  /**
   * <h1>either OIS or OIOS - scheme type</h1>
   */
  @EmbeddedId
  private ObligorIdentifierPK id;

  /**
   * <h1>OI - Obligor Identifier</h1>
   * The unique identifier used to identify an obligor. The identifier used is based on the identification scheme used. Only one identifier at max can be
   * selected in report for the legal entity. See the prioritization in {@link ObligorOrIssuerIdentifierSchemeType}
   */
  @NotBlank
  @Column(name = "IDENTIFIER")
  private String identifier;

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("id", id)
        .add("identifier", identifier)
        .toString();
  }
}

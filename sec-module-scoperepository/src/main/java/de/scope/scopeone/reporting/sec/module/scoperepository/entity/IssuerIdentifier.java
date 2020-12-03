package de.scope.scopeone.reporting.sec.module.scoperepository.entity;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
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
@Table(name = "ISSUER_IDENTIFIER")
public class IssuerIdentifier {

  /**
   * <h1>either ISIS or ISIOS - scheme type</h1>
   */
  @EmbeddedId
  private IssuerIdentifierPK id;

  /**
   * <h1>ISI - Issuer Identifier</h1>
   * The unique identifier used to identify an issuer. The identifier used is based on the identification scheme used. Only one identifier at max can be
   * selected in report for the legal entity. See the prioritization in {@link ObligorOrIssuerIdentifierSchemeType}
   */
  @NotBlank
  @Column(name = "IDENTIFIER")
  private String identifier;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IssuerIdentifier that = (IssuerIdentifier) o;
    return Objects.equal(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("id", id)
        .add("identifier", identifier)
        .toString();
  }
}

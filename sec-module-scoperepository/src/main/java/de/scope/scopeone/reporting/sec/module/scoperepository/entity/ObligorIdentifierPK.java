package de.scope.scopeone.reporting.sec.module.scoperepository.entity;

import com.google.common.base.MoreObjects;
import de.scope.scopeone.reporting.sec.module.scoperepository.entity.enums.ObligorOrIssuerIdentifierSchemeType;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@Embeddable
public class ObligorIdentifierPK implements Serializable {

  private static final long serialVersionUID = 1;

  /**
   * Reverse-ID to the issuer. Not used in reporting file
   */
  @Column(name = "obligor_id")
  private Integer obligorId;

  /**
   * ... mapped to
   * <h1>OIS - Obligor Identifier Scheme</h1>
   * The naming standard used for the obligor identifier. This could be CUSIP, DUNS, BIC, SICC, NRSRO or Other. This taxonomy allows multiple identifiers to be
   * described. If the identifier is the proprietary numbering identifier of the NRSRO then use "NRSRO". If the identifier does not exist in the enumerated list
   * then use the item "Obligor Identifier Other Scheme" to define the alternative identifier scheme. The value used for "Obligor Identifier Other Scheme"
   * should be the acronym of the identifier scheme in upper case. CIK should not be used as an identification scheme as this is explicitly defined.
   * <p>
   * ... or to
   * <h1>OIOS - Obligor Identifier Other Scheme</h1>
   * This item may be used if the value of "NRSRO" is used in the item "Issuer Identifier Scheme". The value entered should be the acronym of the identifier
   * scheme in upper case.
   */
  @Enumerated(EnumType.STRING)
  @Column(name = "IDENTIFIER_SCHEME")
  private ObligorOrIssuerIdentifierSchemeType identifierScheme;

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("identifierScheme", identifierScheme)
        .toString();
  }
}

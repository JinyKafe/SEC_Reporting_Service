package de.scope.scopeone.reporting.sec.module.scoperepository.entity;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import de.scope.scopeone.reporting.sec.module.scoperepository.entity.enums.ObligorOrIssuerIdentifierSchemeType;
import de.scope.scopeone.reporting.sec.module.scoperepository.entity.enums.SecCategoryType;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <h1>ISD - Issuer Details</h1>
 * The container element for an issuer. It contains information about the organization that issued instruments that are being rated.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ISSUER")
public class Issuer implements ScopeReportJpaEntity {

  /**
   * Scope Issuer ID. It is not part of the report file
   */
  @Id
  @Column(name = "ID")
  private Integer id;

  /**
   * <h1>SSC - SEC Category</h1>
   * This item indicates issuer subclass as defined by the SEC. This can be one of the following: Financial, Insurance, Corporate, RMBS, CMBS, CLO, CDO, ABCP,
   * Other ABS, Other SFP, Sovereign, US Public or INT Public.
   */
  @NotNull
  @Column(name = "SEC_CATEGORY")
  @Convert(converter = SecCategoryType.SecCategoryTypeConverter.class)
  private SecCategoryType secCategory;

  /**
   * <h1>ISSNAME - Issuer Name</h1>
   * Legal name of the issuer.
   */
  @NotBlank
  @Column(name = "NAME")
  private String name;

  /**
   * <h1>[IND's] - List of Instrument Details</h1>
   * The container element for an instrument. It is the equivalent of a record. It contains information about the issued instruments that are being rated.
   * <p>
   * TO_DO:validate at least one Instrument must be provided
   */
  @NotEmpty
  @OneToMany(fetch = FetchType.EAGER)
  @JoinColumn(name = "ISSUER_ID")
  private Collection<@Valid Instrument> instruments;

  /**
   * <h1>IG - Issuer Industry Group</h1>
   * Used to identify the specific industry of the issuer rather than the broad heading provided by the SEC Rating class. For example this could be Bank,
   * Property Insurance etc. The values used are the proprietary values used by the NRSRO.
   */
  @Column(name = "INDUSTRY_GROUP")
  private String industryGroup;

  /** TO_DO: valiation: exactly one obligor ID must be provided. If there are more, select the first in this order
   *  TO_DO ({@link #lei}, {@link #cik}, {@link #issuerIdentifiers}) --> start block */

  /**
   * <h1>LEI - Legal Entity Identifier</h1>
   * The 20-character alphanumeric value Legal Entity Identifier issued by a utility endorsed or otherwise governed by the Global LEI Regulatory Oversight
   * Committee or the Global LEI Foundation (LEI) to the obligor, if available.
   */
  @Column(name = "LEI")
  private String lei;

  /**
   * <h1>CIK - Central Index Key</h1>
   * A unique 10-digit SEC-issued value to identify entities that have filed in EDGAR, if available it is used only when no {@link #lei} is availablefor the
   * Obligor.
   */
  @Column(name = "CIK")
  private String cik;

  /**
   * <h1>[OI + OIS || OI + OIOS] - Obligor Identifiers</h1>
   * <p>
   * If an obligor has multiple identifiers for the legal entities contained then multiple obligor identifiers can be used. Obligor identifier is used only when
   * neither {@link #lei} nor {@link #cik} of the Issuer is available!
   * <p>
   * In XBRL report file, each Obligor must have one pair of identifiers (OI + OIS or OI + OIOS). If there is more than one pair of Obligor Identifiers in
   * database use the one with highest priority. For evaluation of priority, use ranking in {@link ObligorOrIssuerIdentifierSchemeType#getRanking()})
   */
  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "ISSUER_ID")
  private Collection<@Valid IssuerIdentifier> issuerIdentifiers;

  /* TO_DO <-- end block */

  /**
   * <h1>ROCRA - "Record of Credit Rating Actions"</h1>
   */
  @NotNull
  @ManyToOne
  @JoinColumn(name = "CREDIT_RATING_AGENCY_INFO_ID", referencedColumnName = "ID")
  private @Valid CreditRatingAgencyInfo ratingAgencyInfo;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Issuer issuer = (Issuer) o;
    return Objects.equal(id, issuer.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("name", name)
        .toString();
  }
}

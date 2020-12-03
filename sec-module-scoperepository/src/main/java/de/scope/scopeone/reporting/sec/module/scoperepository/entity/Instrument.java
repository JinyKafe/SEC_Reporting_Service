package de.scope.scopeone.reporting.sec.module.scoperepository.entity;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import de.scope.scopeone.reporting.sec.module.scoperepository.entity.enums.RatedObjectType;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <h1>IND - Instrument Detail</h1>
 * The container element for an instrument. It is the equivalent of a record. It contains information about the issued instruments that are being rated.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "INSTRUMENT")
public class Instrument {

  /**
   * Internal Scope instrument id. Not part of reporting.
   */
  @Id
  @Column(name = "ID")
  private Integer id;

  /**
   * <h1>INSTNAME - The name used to describe the instrument</h1>
   * The security or money market instrument name.
   * <p></p>
   * At Scope COmpany: Name of the Instrument (see CrdBankBond, CrdCoveredBond, CrdCommercialPaperProgram etc)
   */
  @NotBlank
  @Column(name = "NAME")
  private String name;

  /**
   * <h1>OBT - Object Type</h1>
   * This item indicates whether the rating applies to one of the enumerated values "Program", "Instrument", "Shelf" or "Other".
   * <p></p>
   * At Scope Company: SubCategory
   */
  @NotNull
  @Column(name = "RATED_OBJECT_TYPE")
  @Convert(converter = RatedObjectType.RatedObjectTypeConverter.class)
  private RatedObjectType ratedObjectType;

  /**
   * <h1>CUSIP - ID defined by Committee on Uniform Securities Identification Procedures</h1>
   * The nine-character unique securities identifier assigned to the instrument.
   * <p></p>
   * At Scope Company: does not exist at the moment, but would be helpful
   */
  @Column(name = "CUSIP")
  private String cusip;

  /**
   * <h1>CR - Coupon Rate</h1>
   * Coupon rate stated in the contractual debt agreement. This is not a percentage, it is a decimal value. For example, a 4% rate is represented as “.04”.
   * <p></p>
   * At Scope Company: CouponPercent of the Instrument (see CrdBankBond, CrdCoveredBond, CrdCommercialPaperProgram etc)
   */
  @Column(name = "COUPON_RATE")
  private BigDecimal couponRate;

  /**
   * <h1>ISUD -  Issuance date of the instrument</h1>
   * Issuance Date of the instrument, in ISO 8601 format YYYY-MM-DD.
   * <p></p>
   * At Scope Company: IssueDate of the Instrument (see CrdBankBond, CrdCoveredBond, CrdCommercialPaperProgram etc)
   */
  @Column(name = "ISSUANCE_DATE")
  private Date issuanceDate;

  /**
   * <h1>MD - The Maturity Date of the Instrument</h1>
   * The Maturity Date of the instrument, in ISO 8601 format YYYY-MM-DD.
   * <p></p>
   * At Scope Company: MaturityDate of the Instrument (see CrdBankBond, CrdCoveredBond, CrdCommercialPaperProgram etc)
   */
  @Column(name = "MATURITY_DATE")
  private Date maturityDate;

  /**
   * <h1>PV - The par (face) value of the debt instrument</h1>
   * The Par (face) Value of the debt instrument. The number is not scaled; for example, if the value is one million, the content is “1000000”. If element <PV>
   * is present there must be at least one <xbrli:unit> child element of<xbrli:xbrl>.
   * <p></p>
   * At Scope Company: does not exist in SW at the moment (needs to be discussed)
   */
  @Column(name = "PAR_VALUE")
  private Long parValue;

  /**
   * Currency value for the element {@link #parValue}. It will be attached as xml attribute
   */
  @Column(name = "PAR_VALUE_CURRENCY_CODE")
  private String parValueCurrencyCode;


  /**
   * <h1>RODC - Rating Organization Debt Category</h1>
   * Class of debt using the NRSROs own categorization method to identify the class of debt such as senior, subordinated or other properties of the debt
   * issued.
   * <p/>
   * at Scope: maybe Subcategory (needs to be discussed)
   */
  @Column(name = "DEBT_CATEGORY")
  private String debtCategory;

  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "INSTRUMENT_ID")
  private Collection<@Valid InstrumentIdentifier> identifiers;

  @OneToMany(fetch = FetchType.EAGER)
  @JoinColumn(name = "INSTRUMENT_ID")
  private Collection<@Valid InstrumentRatingAction> ratingActions;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Instrument that = (Instrument) o;
    return Objects.equal(id, that.id);
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

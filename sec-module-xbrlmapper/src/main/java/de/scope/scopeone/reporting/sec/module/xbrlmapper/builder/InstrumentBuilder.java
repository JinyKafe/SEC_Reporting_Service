package de.scope.scopeone.reporting.sec.module.xbrlmapper.builder;

import static de.scope.scopeone.reporting.sec.module.xbrlgenerator.util.ObjectFactorInitializer.getObjectFactory;
import static java.util.Objects.isNull;
import static org.springframework.util.StringUtils.isEmpty;

import gov.sec.ratings.Context;
import gov.sec.ratings.CusipItemType;
import gov.sec.ratings.DateItemType;
import gov.sec.ratings.IND;
import gov.sec.ratings.INIS;
import gov.sec.ratings.MonetaryItemType;
import gov.sec.ratings.OBT;
import gov.sec.ratings.PureItemType;
import gov.sec.ratings.RatingDetailType;
import gov.sec.ratings.StringItemType;
import gov.sec.ratings.Unit;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Map;
import javax.xml.bind.JAXBElement;

public class InstrumentBuilder {

  public static final BigDecimal ONE_HUNDERD = BigDecimal.valueOf(100);
  private IND ind;

  private Context contextRef;

  private Map<String, Unit> unitMap;

  public InstrumentBuilder() {
    ind = getObjectFactory().createIND();
  }

  public IND build() {
    return ind;
  }

  public InstrumentBuilder addContextRef(Context contextRef) {
    this.contextRef = contextRef;
    return this;
  }

  public InstrumentBuilder addUnitRef(Map<String, Unit> unitMap) {
    this.unitMap = unitMap;
    return this;
  }

  public InstrumentBuilder addOBT(String objectType) {
    if (isEmpty(objectType)) {
      return this;
    }
    OBT obt = (OBT) new StringItemTypeBuilder(getObjectFactory().createOBT()).setContextRef(contextRef).setValue(objectType).build();
    return addToContext(getObjectFactory().createOBT(obt));
  }

  public InstrumentBuilder addName(String name) {
    if (isEmpty(name)) {
      return this;
    }
    return addToContext(getObjectFactory().createINSTNAME(buildStringType(name)));
  }

  /**
   * The CUSIP of the security or money market instrument.
   *
   * @param secOrMarketInstrument
   * @return
   */
  public InstrumentBuilder addCUSIP(String secOrMarketInstrument) {
    if (isEmpty(secOrMarketInstrument)) {
      return this;
    }
    CusipItemType itemType = (CusipItemType) new StringItemTypeBuilder(getObjectFactory().createCusipItemType()).setContextRef(contextRef)
        .setValue(secOrMarketInstrument).build();
    return addToContext(getObjectFactory().createCUSIP(itemType));
  }

  /**
   * An Instrument Identifier within a scheme other than CUSIP and is used only when a CUSIP is not available.
   *
   * @param instrumentIdentifier
   * @return
   */
  public InstrumentBuilder addINI(String instrumentIdentifier) {
    if (isEmpty(instrumentIdentifier)) {
      return this;
    }
    return addToContext(getObjectFactory().createINI(buildStringType(instrumentIdentifier)));
  }

  /**
   * The scheme of the identifier in <INI> and must be one of the allowed instrument identifier scheme values
   *
   * @param instrumentIdentifierSchema
   * @return
   */
  public InstrumentBuilder addINIS(String instrumentIdentifierSchema) {
    if (isEmpty(instrumentIdentifierSchema)) {
      return this;
    }
    INIS inis = (INIS) new StringItemTypeBuilder(getObjectFactory().createINIS()).setContextRef(contextRef).setValue(instrumentIdentifierSchema).build();
    return addToContext(getObjectFactory().createINIS(inis));
  }

  /**
   * The scheme of the identifier in <INI> if such scheme is not identifiable by a listed standard instrument identifier scheme.
   *
   * @param instrumentIdentifierSchemaAlternative
   * @return
   */
  public InstrumentBuilder addINIOS(String instrumentIdentifierSchemaAlternative) {
    if (isEmpty(instrumentIdentifierSchemaAlternative)) {
      return this;
    }
    return addToContext(getObjectFactory().createINIOS(buildStringType(instrumentIdentifierSchemaAlternative)));
  }

  /**
   * The Instrument Rate (coupon) Type Description as being fixed, variable, stepped, zero, index plus spread, floating, none, etc.
   *
   * @param rateTypeDescription
   * @return
   */
  public InstrumentBuilder addIRTD(BigDecimal rateTypeDescription) {
    if (isEmpty(rateTypeDescription)) {
      return this;
    }
    return addToContext(getObjectFactory().createIRTD(buildStringType(rateTypeDescription.divide(ONE_HUNDERD).toString())));
  }

  /**
   * Coupon Rate stated in the contractual debt agreement. This is not a percentage, it is a decimal value. For example, a 4% rate is represented as “.04”.
   *
   * @param couponRate
   * @return
   */
  public InstrumentBuilder addCR(BigDecimal couponRate) {
    if (couponRate == null) {
      return this;
    }
    PureItemType itemType = new PureItemTypeBuilder().setContextRef(contextRef)
        .setUnitRef(getPureUnitRef())
        .setValue(couponRate).build();
    return addToContext(getObjectFactory().createCR(itemType));
  }

  /**
   * The Maturity Date of the instrument, in ISO 8601 format YYYY-MM-DD.
   *
   * @param maturityDate
   * @return
   */

  public InstrumentBuilder addMD(Date maturityDate) {
    if (isNull(maturityDate)) {
      return this;
    }
    DateItemType itemType = new DateItemTypeBuilder().setContextRef(contextRef).setValue(maturityDate).build();
    return addToContext(getObjectFactory().createMD(itemType));
  }

  public InstrumentBuilder addMD(LocalDateTime maturityDate) {
    if (isNull(maturityDate)) {
      return this;
    }
    DateItemType itemType = new DateItemTypeBuilder().setContextRef(contextRef).setValue(maturityDate).build();
    return addToContext(getObjectFactory().createMD(itemType));
  }

  /**
   * The Par (face) Value of the debt instrument. The number is not scaled; for example, if the value is one million, the content is “1000000”. If element <PV>
   * is present there must be at least one <xbrli:unit> child element of <xbrli:xbrl>.
   *
   * @param parValue
   * @return
   */
  public InstrumentBuilder addPV(BigDecimal parValue, String currency) {
    if (parValue == null) {
      return this;
    }
    MonetaryItemType itemType = new MonetaryItemTypeBuilder().setContextRef(contextRef).setUnitRef(getUnitRef(currency)).setValue(parValue).build();
    return addToContext(getObjectFactory().createPV(itemType));
  }

  public InstrumentBuilder addPV(Long parValue, String currency) {
    if (parValue == null) {
      return this;
    }
    return addPV(BigDecimal.valueOf(parValue), currency);
  }

  /**
   * Issuance Date of the instrument, in ISO 8601 format YYYY-MM-DD.
   *
   * @param issuanceDate
   * @return
   */
  public InstrumentBuilder addISUD(Date issuanceDate) {
    if (isNull(issuanceDate)) {
      return this;
    }
    DateItemType itemType = new DateItemTypeBuilder().setContextRef(contextRef).setValue(issuanceDate).build();
    return addToContext(getObjectFactory().createISUD(itemType));
  }

  public InstrumentBuilder addRODC(String otherDebtCategory) {
    if (isEmpty(otherDebtCategory)) {
      return this;
    }
    return addToContext(getObjectFactory().createRODC(buildStringType(otherDebtCategory)));
  }

  public InstrumentBuilder addINRD(RatingDetailType ratingDetailType) {
    return addToContext(getObjectFactory().createINRD(ratingDetailType));
  }

  private InstrumentBuilder addToContext(JAXBElement object) {
    ind.getContent().add(object);
    return this;
  }

  private Unit getPureUnitRef() {
    return getUnitRef(UnitBuilder.PURE);
  }

  private Unit getUnitRef(String key) {
    if (!unitMap.containsKey(key)) {
      unitMap.put(key, new UnitBuilder().setId(key).build());
    }
    return unitMap.get(key);
  }

  private StringItemType buildStringType(String strType) {
    return new StringItemTypeBuilder().setContextRef(contextRef).setValue(strType).build();
  }
}

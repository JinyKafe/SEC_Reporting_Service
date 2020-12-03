package de.scope.scopeone.reporting.sec.module.xbrlmapper.builder;

import static de.scope.scopeone.reporting.sec.module.xbrlgenerator.util.ObjectFactorInitializer.getObjectFactory;

import gov.sec.ratings.CentralIndexKey2ItemType;
import gov.sec.ratings.Context;
import gov.sec.ratings.IssuerIdentifierSchemeItemType;
import gov.sec.ratings.LeiItemType;
import gov.sec.ratings.OD;
import gov.sec.ratings.RatingDetailType;
import gov.sec.ratings.SECCategoryItemType;
import gov.sec.ratings.StringItemType;
import javax.xml.bind.JAXBElement;

/**
 * Obligor details
 */
public class ObligorBuilder {

  private OD od;

  private Context contextRef;

  public ObligorBuilder() {
    od = getObjectFactory().createOD();
  }

  public OD build() {
    return od;
  }

  public ObligorBuilder addContextRef(Context contextRef) {
    this.contextRef = contextRef;
    return this;
  }

  /**
   * The SEC Category or Subcategory applicable to the Obligor
   *
   * @param sectorCategory
   * @return
   */
  public ObligorBuilder addOSC(String sectorCategory) {
    SECCategoryItemType itemType = (SECCategoryItemType) new StringItemTypeBuilder(getObjectFactory().createSECCategoryItemType()).setContextRef(contextRef)
        .setValue(sectorCategory).build();
    return addToContext(getObjectFactory().createOSC(itemType));
  }

  /**
   * Obligor Industry Group within the broad heading provided by the SEC Category or Subcategory. For example, an Obligor associated with the “Financial” SEC
   * Category could be further identified as a “Bank”, “Broker” or another applicable identifier. The values used are the proprietary values used by the NRSRO
   *
   * @param industryGroup
   * @return
   */
  public ObligorBuilder addOIG(String industryGroup) {
    return addToContext(getObjectFactory().createOIG(buildStringType(industryGroup)));
  }

  /**
   * The Obligor Name
   *
   * @param name
   * @return
   */
  public ObligorBuilder addName(String name) {
    return addToContext(getObjectFactory().createOBNAME(buildStringType(name)));
  }

  public ObligorBuilder addLEI(String legalEntityIdentifier) {
    LeiItemType itemType = (LeiItemType) new StringItemTypeBuilder(new LeiItemType()).setContextRef(contextRef).setValue(legalEntityIdentifier).build();
    return addToContext(getObjectFactory().createLEI(itemType));
  }

  public ObligorBuilder addCIK(String centralIndexKey) {
    CentralIndexKey2ItemType itemType = (CentralIndexKey2ItemType) new StringItemTypeBuilder(getObjectFactory().createCentralIndexKey2ItemType())
        .setContextRef(contextRef).setValue(centralIndexKey).build();
    return addToContext(getObjectFactory().createCIK(itemType));
  }

  /**
   * The content of this element is an Obligor Identifier in a scheme other than LEI or CIK; it is used only when neither LEI nor CIK is available for the
   * Obligor.
   *
   * @param identifier
   * @return
   */
  public ObligorBuilder addOI(String identifier) {
    return addToContext(getObjectFactory().createOI(buildStringType(identifier)));
  }

  public ObligorBuilder addOIS(String identifierSchema) {
    IssuerIdentifierSchemeItemType itemType = (IssuerIdentifierSchemeItemType) new StringItemTypeBuilder(
        getObjectFactory().createIssuerIdentifierSchemeItemType()).setContextRef(contextRef).setValue(identifierSchema).build();
    return addToContext(getObjectFactory().createOIS(itemType));
  }

  /**
   * The scheme of the identifier in <OI> if such scheme is not identifiable by a listed standard obligor or issuer identifier scheme value.
   *
   * @param alternatifIdentifierSchema
   * @return
   */
  public ObligorBuilder addOIOS(String alternatifIdentifierSchema) {
    return addToContext(getObjectFactory().createOIOS(buildStringType(alternatifIdentifierSchema)));
  }

  /**
   * Obligor rating details
   *
   * @param ratingDetails
   * @return
   */
  public ObligorBuilder addORD(RatingDetailType ratingDetails) {
    return addToContext(getObjectFactory().createORD(ratingDetails));
  }

  private ObligorBuilder addToContext(JAXBElement object) {
    od.getContent().add(object);
    return this;
  }

  private StringItemType buildStringType(String strType) {
    return new StringItemTypeBuilder().setContextRef(contextRef).setValue(strType).build();
  }

}

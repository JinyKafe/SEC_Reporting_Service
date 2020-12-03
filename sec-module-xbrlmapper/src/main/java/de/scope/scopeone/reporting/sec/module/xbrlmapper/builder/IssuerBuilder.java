package de.scope.scopeone.reporting.sec.module.xbrlmapper.builder;

import static de.scope.scopeone.reporting.sec.module.xbrlgenerator.util.ObjectFactorInitializer.getObjectFactory;

import de.scope.scopeone.reporting.sec.module.xbrlgenerator.util.ObjectFactorInitializer;
import gov.sec.ratings.CentralIndexKey2ItemType;
import gov.sec.ratings.Context;
import gov.sec.ratings.IND;
import gov.sec.ratings.ISD;
import gov.sec.ratings.IssuerIdentifierSchemeItemType;
import gov.sec.ratings.LeiItemType;
import gov.sec.ratings.SECCategoryItemType;
import gov.sec.ratings.StringItemType;
import javax.xml.bind.JAXBElement;

/**
 * Issuer details
 */
public class IssuerBuilder {

  private ISD isd;

  private Context contextRef;

  public IssuerBuilder() {
    isd = getObjectFactory().createISD();
  }

  public ISD build() {
    return isd;
  }

  public IssuerBuilder addContextRef(Context contextRef) {
    this.contextRef = contextRef;
    return this;
  }

  /**
   * The SEC Category or Subcategory applicable to the Issuer
   *
   * @param secCategory
   * @return
   */
  public IssuerBuilder addSSC(String secCategory) {
    SECCategoryItemType itemType = (SECCategoryItemType) new StringItemTypeBuilder(getObjectFactory().createSECCategoryItemType()).setContextRef(contextRef)
        .setValue(secCategory).build();
    return addToContext(getObjectFactory().createSSC(itemType));
  }

  /**
   * The Issuer Industry Group within the broad heading provided by the SEC Category or Subcategory. For example, an Issuer associated with the “Financial” SEC
   * Category could be further identified as a “Bank”, “Broker” or another applicable identifier. The values used are the proprietary values used by the NRSRO.
   *
   * @param industryGroup
   * @return
   */
  public IssuerBuilder addIG(String industryGroup) {
    StringItemType itemType = new StringItemTypeBuilder()
        .setContextRef(contextRef)
        .setValue(industryGroup).build();
    return addToContext(getObjectFactory().createIG(itemType));
  }

  public IssuerBuilder addName(String name) {
    StringItemType itemType = new StringItemTypeBuilder().setContextRef(contextRef).setValue(name).build();
    return addToContext(getObjectFactory().createISSNAME(itemType));
  }

  public IssuerBuilder addLEI(String legalEntityIdentifier) {
    LeiItemType itemType = (LeiItemType) new StringItemTypeBuilder(new LeiItemType()).setContextRef(contextRef).setValue(legalEntityIdentifier).build();
    return addToContext(getObjectFactory().createLEI(itemType));
  }

  /**
   * The 10-digit Central Index Key (CIK) number of the Obligor, if available; it is used only when no LEI is available for the Obligor.
   *
   * @param centralIndexKey
   * @return
   */
  public IssuerBuilder addCIK(String centralIndexKey) {
    CentralIndexKey2ItemType itemType = (CentralIndexKey2ItemType) new StringItemTypeBuilder<>(
        ObjectFactorInitializer.getObjectFactory().createCentralIndexKey2ItemType()).setContextRef(contextRef).setValue(centralIndexKey).build();
    return addToContext(getObjectFactory().createCIK(itemType));
  }

  /**
   * An Issuer Identifier in a scheme other than LEI or CIK and is used only when neither LEI nor CIK of the Issuer is available.
   *
   * @param issueIdentifier
   * @return
   */
  public IssuerBuilder addISI(String issueIdentifier) {
    return addToContext(getObjectFactory().createISI(buildStringType(issueIdentifier)));
  }

  /**
   * The scheme of the identifier in <ISI>; it must be one of the allowed Obligor or Issuer identifier scheme values
   *
   * @param issueIdentifierSchema
   * @return
   */
  public IssuerBuilder addISIS(String issueIdentifierSchema) {
    IssuerIdentifierSchemeItemType itemType = (IssuerIdentifierSchemeItemType) new StringItemTypeBuilder(
        getObjectFactory().createIssuerIdentifierSchemeItemType()).setContextRef(contextRef).setValue(issueIdentifierSchema).build();
    return addToContext(getObjectFactory().createISIS(itemType));
  }

  /**
   * The scheme of the identifier in <ISI> if such scheme is not identifiable by a listed standard Issuer identifier scheme.
   *
   * @param undefinedIdentifierSchema
   * @return
   */
  public IssuerBuilder addISIOS(String undefinedIdentifierSchema) {
    return addToContext(getObjectFactory().createISIOS(buildStringType(undefinedIdentifierSchema)));
  }

  public IssuerBuilder addIND(IND instrumentDetails) {
    return addToContext(getObjectFactory().createIND(instrumentDetails));
  }

  private IssuerBuilder addToContext(JAXBElement object) {
    isd.getContent().add(object);
    return this;
  }

  private StringItemType buildStringType(String strType) {
    return new StringItemTypeBuilder().setContextRef(contextRef).setValue(strType).build();
  }
}

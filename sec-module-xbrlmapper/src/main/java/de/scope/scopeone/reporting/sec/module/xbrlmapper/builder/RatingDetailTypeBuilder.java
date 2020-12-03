package de.scope.scopeone.reporting.sec.module.xbrlmapper.builder;

import static de.scope.scopeone.reporting.sec.module.xbrlgenerator.util.ObjectFactorInitializer.getObjectFactory;
import static java.util.Objects.isNull;

import gov.sec.ratings.BooleanItemType;
import gov.sec.ratings.Context;
import gov.sec.ratings.DateItemType;
import gov.sec.ratings.RAC;
import gov.sec.ratings.RatingDetailType;
import gov.sec.ratings.StringItemType;
import java.util.Date;
import javax.xml.bind.JAXBElement;
import org.springframework.util.StringUtils;

/**
 * Obligor-Instrument rating details
 */
public class RatingDetailTypeBuilder {

  private RatingDetailType ratingDetailType;

  private Context contextRef;

  public RatingDetailTypeBuilder() {
    ratingDetailType = getObjectFactory().createRatingDetailType();
  }

  public RatingDetailTypeBuilder addContextRef(Context contextRef) {
    this.contextRef = contextRef;
    return this;
  }

  /**
   * Issuer Paid. True if the rating was issuer-paid, otherwise False.
   *
   * @param isPaid
   * @return
   */
  public RatingDetailTypeBuilder addIp(boolean isPaid) {
    BooleanItemType ip = new BooleanItemTypeBuilder().setContextRef(contextRef).setValue(isPaid).build();
    return addToContext(getObjectFactory().createIP(ip));
  }

  /**
   * The credit rating symbol, number, or score in the applicable rating scale of the NRSRO AAA, A+ or Ba
   *
   * @param ratingSymbol
   * @return
   */
  public RatingDetailTypeBuilder addR(String ratingSymbol) {
    if (StringUtils.isEmpty(ratingSymbol)) {
      return this;
    }
    return addToContext(getObjectFactory().createR(buildStringType(ratingSymbol)));
  }


  /**
   * The date of the credit rating action, in ISO 8601 YYYY-MM-DD format. Example value: 2014-10-28.
   *
   * @param ratingActionDate
   * @return
   */
  public RatingDetailTypeBuilder addRAD(Date ratingActionDate) {
    if (isNull(ratingActionDate)) {
      return this;
    }
    DateItemType rad = new DateItemTypeBuilder().setContextRef(contextRef).setValue(ratingActionDate).build();
    return addToContext(getObjectFactory().createRAD(rad));
  }

  /**
   * Rating Action Classification (see “Rating Action Classification Values”, 2.4.4 below) as required by Rule 17g-7(b)(v).
   *
   * @param ratingActionClassification
   * @return
   */
  public RatingDetailTypeBuilder addRAC(String ratingActionClassification) {
    if (StringUtils.isEmpty(ratingActionClassification)) {
      return this;
    }
    RAC rac = (RAC) new StringItemTypeBuilder(getObjectFactory().createRAC()).setContextRef(contextRef).setValue(ratingActionClassification).build();
    return addToContext(getObjectFactory().createRAC(rac));
  }

  /**
   * Watch Status. This item records watch list status such as Positive, Negative, Evolving, Developing, and Stable. The NRSRO should use its own standard
   * terminology.
   *
   * @param watchStatus
   * @return
   */
  public RatingDetailTypeBuilder addWST(String watchStatus) {
    if (StringUtils.isEmpty(watchStatus)) {
      return this;
    }
    return addToContext(getObjectFactory().createWST(buildStringType(watchStatus)));
  }

  /**
   * Rating Outlook. This item is used to record Outlook, such as Positive, Negative, Evolving, and Developing. The NRSRO should use its own standard
   * terminology.
   *
   * @param ratingOutlook
   * @return
   */
  public RatingDetailTypeBuilder addROL(String ratingOutlook) {
    if (StringUtils.isEmpty(ratingOutlook)) {
      return this;
    }
    return addToContext(getObjectFactory().createROL(buildStringType(ratingOutlook)));
  }

  /**
   * The description of an Other Announcement type not classifiable under <RAC>, <WST>, or <ROL>. The NRSRO should use its own standard terminology. Example
   * values: “Company name change”, “Affirm”.
   *
   * @param anotherAnnouncementType
   * @return
   */
  public RatingDetailTypeBuilder addOAN(String anotherAnnouncementType) {
    if (StringUtils.isEmpty(anotherAnnouncementType)) {
      return this;
    }
    return addToContext(getObjectFactory().createOAN(buildStringType(anotherAnnouncementType)));
  }

  /**
   * The rating type. The NRSRO should use its own standard terminology. Example values: “Bank Financial Strength”.
   *
   * @param ratingType
   * @return
   */
  public RatingDetailTypeBuilder addRT(String ratingType) {
    if (StringUtils.isEmpty(ratingType)) {
      return this;
    }
    return addToContext(getObjectFactory().createRT(buildStringType(ratingType)));
  }

  /**
   * Sub type of the rating type used by the NRSRO. This is used to further classify a Rating Type if necessary.
   *
   * @param subtypeRatingType
   * @return
   */
  public RatingDetailTypeBuilder addRST(String subtypeRatingType) {
    if (StringUtils.isEmpty(subtypeRatingType)) {
      return this;
    }
    return addToContext(getObjectFactory().createRST(buildStringType(subtypeRatingType)));
  }

  /**
   * The Rating Type Term; the NRSRO should use its own standard terminology. Example values: “short-term”, “long-term
   *
   * @param ratingTermType
   * @return
   */
  public RatingDetailTypeBuilder addRTT(String ratingTermType) {
    if (StringUtils.isEmpty(ratingTermType)) {
      return this;
    }
    return addToContext(getObjectFactory().createRTT(buildStringType(ratingTermType)));
  }

  private StringItemType buildStringType(String strType) {
    return new StringItemTypeBuilder().setContextRef(contextRef).setValue(strType).build();
  }

  public RatingDetailType build() {
    return ratingDetailType;
  }

  private RatingDetailTypeBuilder addToContext(JAXBElement object) {
    ratingDetailType.getContent().add(object);
    return this;
  }
}

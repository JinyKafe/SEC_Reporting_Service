package de.scope.scopeone.reporting.sec.module.scoperepository.entity;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import de.scope.scopeone.reporting.sec.module.scoperepository.entity.enums.SecRatingActionType;
import java.sql.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <h1>INRD - Instrument Rating Details</h1>
 * Contains the detailed rating information that is specific to a financial instrument or program being rated.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "INSTRUMENT_RATING_ACTION")
public class InstrumentRatingAction {

  @EmbeddedId
  private InstrumentRatingActionPK id;

  /**
   * <h1>IP - Issuer Paid</h1>
   * True for an issuer-paid rating, otherwise false.
   * <p/>
   * At Scope: DemandType (see History.CrdRatingAction)
   */
  @NotNull
  @Column(name = "ISSUER_PAID")
  private Boolean issuerPaid;

  /**
   * <h1>R - Rating</h1>
   * The opinion assigned to the instrument or issuer using the proprietary rating symbology employed by each rating organization such as AAA, A+ or Ba. This
   * item is also used to record watch list status changes such as Positive, Negative, Evolving, Developing, and Stable. In addition, this item is used to
   * record changes in Outlook such as Positive, Negative, Evolving, and Developing. This item is not normalized and rating agencies use their own definitions
   * for this item. This item should be used in conjunction with the Rating Type, Rating Action, and Announcement Type items to determine the nature of the
   * rating opinion e.g. rating, outlook, watch list or trend.
   * <p/>
   * RatingScaleNotchLabel (see History.CrdRatingAction)
   */
  @NotBlank
  @Column(name = "RATING")
  private String rating;

  /**
   * <h1>RAD - Rating Action Date</h1>
   * The date that the rating was published.
   * <p/>
   * At Scope: ValidityDateTime (see History.CrdRatingAction)
   */
  @NotNull
  @Column(name = "RATING_DATE")
  private Date ratingDate;

  /* TO_DO:start of validation block - at least one of {@link #ratingActionType}, {@link #watchReview}, {@link #outlookTrend} or {@link #otherRatingActionType}
   must appear in each rating action */

  /**
   * <h1>RAC - Rating Action Classification/Type</h1>
   * Rating action classification. This item uses the classification of rating actions from rule 17g-2(2)(b)(v).
   * <p/>
   * At Scope: ActionType (see History.CrdRatingAction)
   */
  @Enumerated(EnumType.STRING)
  @Column(name = "RATING_ACTION_TYPE")
  private SecRatingActionType ratingActionType;

  /**
   * <h1>WST - Watch Status</h1>
   * Watch Status. This item records watch list status such as Positive, Negative, Evolving, Developing, and Stable. The NRSRO should use its own standard
   * terminology.
   * <p/>
   * at Scope: WatchReview (see History.CrdRatingAction)
   */
  @Column(name = "WATCH_REVIEW")
  private String watchReview;

  /**
   * <h1>ROL - Rating Outlook</h1>
   * Rating Outlook. This item is used to record Outlook, such as Positive, Negative, Evolving, and Developing. The NRSRO should use its own standard
   * terminology.
   * <p/>
   * at Scope: OutlookTrend (see History.CrdRatingAction)
   */
  @Column(name = "OUTLOOK_TREND")
  private String outlookTrend;

  /**
   * <h1>RAC - Rating Action Classification/Type</h1>
   * Rating action classification. This item uses the classification of rating actions from rule 17g-2(2)(b)(v).
   * <p/>
   * At Scope: ActionType (see History.CrdRatingAction)
   */
  @Column(name = "OTHER_RATING_ACTION_TYPE")
  private String otherRatingActionType;

  /* TO_DO:end of validation block - at least one of {@link #ratingActionType}, {@link #watchReview}, {@link #outlookTrend} or {@link #otherRatingActionType}
      must appear in each rating action */

  /**
   * <h1>RT - Rating Type</h1>
   * Description of the type of rating being assigned, for example, Bank Fundamental Strength Rating. The NRSRO should use its own standard terminology. The
   * rating type can be further classified using the Rating Term Type and the Rating Subtype.
   * <p/>
   * at Scope: RatingType (see History.CrdRatingAction)
   */
  @Column(name = "RATING_TYPE")
  private String ratingType;

  /**
   * <h1>RST - Rating Subtype Scheme</h1>
   * Sub type of the rating type used by the NRSO. This is used to further classify a rating type if necessary. For example, Argentine Rating Scale.
   * <p/>
   * at Scope: also RatingType (see History.CrdRatingAction)
   * <p/>
   * TO_DO:validate - 0..1 and may only appear if {@link #ratingType} is present
   */
  @Column(name = "RATING_SUB_TYPE_SCHEME")
  private String ratingSubTypeScheme;

  /**
   * <h1>RTT - Rating Type Term</h1>
   * Description of the term of the rating being assigned, for example, long-term, short-term. The NRSRO should use its own standard terminology.
   * <p/>
   * at Scope: RatingScaleTimeHorizon  (see History.CrdRatingAction)
   */
  @Column(name = "RATING_TYPE_TERM")
  private String ratingTypeTerm;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InstrumentRatingAction that = (InstrumentRatingAction) o;
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
        .add("issuerPaid", issuerPaid)
        .add("rating", rating)
        .add("ratingDate", ratingDate)
        .add("ratingActionType", ratingActionType)
        .add("otherRatingActionType", otherRatingActionType)
        .add("ratingType", ratingType)
        .add("ratingTypeTerm", ratingTypeTerm)
        .add("ratingSubTypeScheme", ratingSubTypeScheme)
        .add("outlookTrend", outlookTrend)
        .add("watchReview", watchReview)
        .omitNullValues()
        .toString();
  }
}

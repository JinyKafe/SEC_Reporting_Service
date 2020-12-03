package de.scope.scopeone.reporting.sec.module.xbrlmapper.service;

import de.scope.scopeone.reporting.sec.common.error.SecErrorCode;
import de.scope.scopeone.reporting.sec.common.error.SecInternalError;
import de.scope.scopeone.reporting.sec.module.scoperepository.entity.Instrument;
import de.scope.scopeone.reporting.sec.module.scoperepository.entity.InstrumentIdentifier;
import de.scope.scopeone.reporting.sec.module.scoperepository.entity.InstrumentRatingAction;
import de.scope.scopeone.reporting.sec.module.scoperepository.entity.Issuer;
import de.scope.scopeone.reporting.sec.module.scoperepository.entity.IssuerIdentifier;
import de.scope.scopeone.reporting.sec.module.scoperepository.entity.enums.RatedObjectType;
import de.scope.scopeone.reporting.sec.module.scoperepository.entity.enums.SecRatingActionType;
import de.scope.scopeone.reporting.sec.module.scoperepository.repository.IssuerIdentifierRepository;
import de.scope.scopeone.reporting.sec.module.xbrlmapper.builder.InstrumentBuilder;
import de.scope.scopeone.reporting.sec.module.xbrlmapper.builder.IssuerBuilder;
import de.scope.scopeone.reporting.sec.module.xbrlmapper.builder.RatingDetailTypeBuilder;
import de.scope.scopeone.reporting.sec.module.xbrlmapper.mapper.ContextMapper;
import de.scope.scopeone.reporting.sec.module.xbrlmapper.mapper.XbrlMapper;
import gov.sec.ratings.Context;
import gov.sec.ratings.IND;
import gov.sec.ratings.ISD;
import gov.sec.ratings.RatingDetailType;
import gov.sec.ratings.Unit;
import gov.sec.ratings.Xbrl;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
@Slf4j
public class IssuerMapperSerivceImpl implements IssuerMapperSerivce {

  private final IssuerIdentifierRepository issuerIdentifierRepository;

  private static IND getInstrumentDetails(Instrument instrument, Context context, Map<String, Unit> unitMap) {
    InstrumentBuilder builder = new InstrumentBuilder()
        .addContextRef(context)
        .addUnitRef(unitMap)
        .addOBT(getRateObjectTypeValue(instrument.getRatedObjectType()))
        .addName(instrument.getName());
    addCUSIPorINI(instrument.getCusip(), instrument.getIdentifiers(), builder);
    builder
        .addIRTD(instrument.getCouponRate())
        // TO_DO: wil be enabled in verion 2
        .addCR(instrument.getCouponRate())
        .addMD(instrument.getMaturityDate())
        // TO_DO: wil be enabled in verion 2
        .addPV(instrument.getParValue(), instrument.getParValueCurrencyCode())
        .addISUD(instrument.getIssuanceDate())
        .addRODC(instrument.getDebtCategory());
//    instrument.getParValueCurrencyCode()
    addRatingActions(instrument.getRatingActions(), context, builder);
    return builder.build();
  }

  private static void addRatingActions(Collection<InstrumentRatingAction> ratingActions, Context context, InstrumentBuilder builder) {
    ratingActions.stream().filter(Objects::nonNull)
        .map(ra -> getRatingDetailsType(ra, context))
        .forEach(builder::addINRD);
  }

  private static String getRateObjectTypeValue(RatedObjectType ratedObjectType) {
    return ratedObjectType != null ? ratedObjectType.getValue() : null;
  }

  private static void addCUSIPorINI(String cusip, Collection<InstrumentIdentifier> instrumentIdentifiers, InstrumentBuilder builder) {
    if (!StringUtils.isEmpty(cusip)) {
      builder.addCUSIP(cusip);
    } else if (instrumentIdentifiers != null && !instrumentIdentifiers.isEmpty()) {
      InstrumentIdentifier id = getInstrumentIdentifierWithHighestPriority(instrumentIdentifiers);
      builder.addINI(id.getIdentifier());
      builder.addINIS(id.getId().getIdentifierScheme().name());
      // TO_DO INIOS where to find and set it
//        builder.addIdentifierSchemaAlternative()
    }
  }

  protected static InstrumentIdentifier getInstrumentIdentifierWithHighestPriority(Collection<InstrumentIdentifier> instrumentIdentifiers) {
    return instrumentIdentifiers.stream()
        .min(Comparator.comparingInt((InstrumentIdentifier id) -> id.getId().getIdentifierScheme().getRanking()))
        .orElseThrow(() -> new SecInternalError(SecErrorCode.TODO, "no instruments provided. TODO should be validated before"));
  }

  private static RatingDetailType getRatingDetailsType(InstrumentRatingAction ratingAction, Context context) {
    RatingDetailTypeBuilder builder = new RatingDetailTypeBuilder()
        .addContextRef(context)
        .addIp(ratingAction.getIssuerPaid())
        .addR(Objects.requireNonNull(ratingAction.getRating(), "Rating (R) must not be null "))
        .addRAD(ratingAction.getRatingDate())
        .addRAC(addRatingAcion(ratingAction.getRatingActionType()))
        .addWST(ratingAction.getWatchReview())
        .addROL(ratingAction.getOutlookTrend())
        .addOAN(ratingAction.getOtherRatingActionType())
        .addRT(ratingAction.getRatingType())
        .addRST(ratingAction.getRatingSubTypeScheme())
        .addRTT(Objects.requireNonNull(ratingAction.getRatingTypeTerm(), "Rating (RTT) Type must not be null"));

    return builder.build();
  }

  private static String addRatingAcion(SecRatingActionType actionType) {
    return actionType != null ? actionType.toString() : null;

  }

  @Override
  public Xbrl toXmlObject(Issuer issuer, LocalDateTime creationDate) {
    List<Date> ratingsDates =
        issuer.getInstruments().stream()
            .map(Instrument::getRatingActions)
            .flatMap(Collection::stream)
            .map(InstrumentRatingAction::getRatingDate)
            .collect(Collectors.toList());

    Map<String, Unit> unitMap = new HashMap<>();

    Context contextReference = ContextMapper
        .initializeContext(issuer.getRatingAgencyInfo().getName(), ratingsDates);

    ISD isd = getIssuer(issuer, contextReference, unitMap);
    return XbrlMapper.toXmlObject(isd, contextReference, issuer.getRatingAgencyInfo().getName(), unitMap, creationDate);
  }

  private ISD getIssuer(Issuer issuer, Context contextRef, Map<String, Unit> unitMap) {
    IssuerBuilder builder = new IssuerBuilder().addContextRef(contextRef)
        .addSSC(issuer.getSecCategory().getValue())
        .addIG(issuer.getIndustryGroup())
        .addName(issuer.getName());
    addLeiOrCikOrIdentifierAndIdSchema(builder, issuer);
    issuer.getInstruments().stream().filter(Objects::nonNull)
        .map(ins -> getInstrumentDetails(ins, contextRef, unitMap))
        .forEach(builder::addIND);
    return builder.build();
  }

  private IssuerBuilder addLeiOrCikOrIdentifierAndIdSchema(IssuerBuilder builder, Issuer issuer) {
    if (!StringUtils.isEmpty(issuer.getLei())) {
      return builder.addLEI(issuer.getLei());
    } else if (!StringUtils.isEmpty(issuer.getCik())) {
      return builder.addCIK(issuer.getCik());
    } else {
      List<IssuerIdentifier> issuerIdentifiers = issuerIdentifierRepository.findAllByIssuerId(issuer.getId());
      if (!issuerIdentifiers.isEmpty()) {
        IssuerIdentifier issuerIdentifier = issuerIdentifiers.get(0);
        return builder.addISI(issuerIdentifier.getIdentifier())
            .addISIS(issuerIdentifier.getId().getIdentifierScheme().name());
      }
      // TO_DO ISIOS how to determine and set?
    }
    log.error("LeiOrCikOrIdentifier is missing for {}", issuer.getId());
    return builder;
  }
}


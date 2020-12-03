package de.scope.scopeone.reporting.sec.module.xbrlmapper.service;

import de.scope.scopeone.reporting.sec.module.scoperepository.entity.Obligor;
import de.scope.scopeone.reporting.sec.module.scoperepository.entity.ObligorIdentifier;
import de.scope.scopeone.reporting.sec.module.scoperepository.entity.ObligorRatingAction;
import de.scope.scopeone.reporting.sec.module.scoperepository.entity.enums.SecRatingActionType;
import de.scope.scopeone.reporting.sec.module.scoperepository.repository.ObligatorIdentifierRepository;
import de.scope.scopeone.reporting.sec.module.xbrlmapper.builder.ObligorBuilder;
import de.scope.scopeone.reporting.sec.module.xbrlmapper.builder.RatingDetailTypeBuilder;
import de.scope.scopeone.reporting.sec.module.xbrlmapper.mapper.ContextMapper;
import de.scope.scopeone.reporting.sec.module.xbrlmapper.mapper.XbrlMapper;
import gov.sec.ratings.Context;
import gov.sec.ratings.OD;
import gov.sec.ratings.RatingDetailType;
import gov.sec.ratings.Xbrl;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
@RequiredArgsConstructor
public class ObligorMapperServiceImpl implements ObligorMapperService {

  private final ObligatorIdentifierRepository obligatorIdentifierRepository;

  private static RatingDetailType getRatingDetailsType(ObligorRatingAction ratingAction, Context context) {
    RatingDetailTypeBuilder builder = new RatingDetailTypeBuilder()
        .addContextRef(context)
        .addIp(ratingAction.getIssuerPaid())
        .addR(ratingAction.getRating())
        .addRAD(ratingAction.getRatingDate())
        .addRAC(getActionType(ratingAction.getRatingActionType()))
        .addWST(ratingAction.getWatchReview())
        .addROL(ratingAction.getOutlookTrend())
        .addOAN(ratingAction.getOtherRatingActionType())
        .addRT(ratingAction.getRatingType())
        .addRST(ratingAction.getRatingSubTypeScheme())
        .addRTT(ratingAction.getRatingTypeTerm());

    return builder.build();
  }

  private static String getActionType(SecRatingActionType actionType) {
    return actionType != null ? actionType.toString() : null;
  }

  @Override
  public Xbrl toXmlObject(Obligor obligor, LocalDateTime creationDate) {
    List<Date> ratingsDates = obligor.getRatingActions()
        .stream()
        .map(ObligorRatingAction::getRatingDate)
        .filter(Objects::nonNull)
        .collect(Collectors.toList());

    String agencyName = obligor.getRatingAgencyInfo().getName();
    Context contextReference = ContextMapper
        .initializeContext(agencyName, ratingsDates);
    return XbrlMapper.toXmlObject(getObligor(obligor, contextReference), contextReference, agencyName, creationDate);
  }

  private OD getObligor(Obligor obligor, Context contextRef) {
    ObligorBuilder builder = new ObligorBuilder().addContextRef(contextRef)
        .addOSC(obligor.getSecCategory().getValue())
        .addOIG(obligor.getIndustryGroup())
        .addName(obligor.getName());
    addLeiOrCikOrIdentifier(builder, obligor);
    obligor.getRatingActions()
        .stream()
        .filter(Objects::nonNull)
        .sorted(Comparator.comparing(ObligorRatingAction::getRatingDate))
        .map(ra -> getRatingDetailsType(ra, contextRef))
        .forEach(builder::addORD);

    return builder.build();
  }

  private ObligorBuilder addLeiOrCikOrIdentifier(ObligorBuilder builder, Obligor obligor) {
    if (!StringUtils.isEmpty(obligor.getLei())) {
      return builder.addLEI(obligor.getLei());
    } else if (!StringUtils.isEmpty(obligor.getCik())) {
      return builder.addCIK(obligor.getCik());
    } else {
      List<ObligorIdentifier> obligorIdentifiers = obligatorIdentifierRepository.findAllByObligorId(obligor.getId());
      if (!obligorIdentifiers.isEmpty()) {
        ObligorIdentifier obligorIdentifier = obligorIdentifiers.get(0);
        return builder.addOI(obligorIdentifier.getIdentifier())
            .addOIS(obligorIdentifier.getId().getIdentifierScheme().name());
        // builder.addOIOS()
        // TO_DO check how we can set this
      }
    }

    log.error("LeiOrCikOrIdentifier is missing for {}", obligor.getId());
    return builder;
  }
}


package de.scope.scopeone.reporting.sec.module.xbrlgenerator.service;

import static org.assertj.core.api.Assertions.assertThat;

import de.scope.scopeone.reporting.sec.module.xbrlgenerator.util.ObjectFactorInitializer;
import gov.sec.ratings.BooleanItemType;
import gov.sec.ratings.Context;
import gov.sec.ratings.ContextEntityType;
import gov.sec.ratings.ContextEntityType.Identifier;
import gov.sec.ratings.ContextPeriodType;
import gov.sec.ratings.DateItemType;
import gov.sec.ratings.LeiItemType;
import gov.sec.ratings.OD;
import gov.sec.ratings.ObjectFactory;
import gov.sec.ratings.RAC;
import gov.sec.ratings.ROCRA;
import gov.sec.ratings.RatingDetailType;
import gov.sec.ratings.SECCategoryItemType;
import gov.sec.ratings.SimpleType;
import gov.sec.ratings.StringItemType;
import gov.sec.ratings.Xbrl;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.xml.bind.JAXBElement;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

@SpringBootTest
@ComponentScan(basePackageClasses = XbrlGeneratorService.class)
public class XbrlValidationServiceImplTest {

  @Autowired
  private XbrlGeneratorService xbrlGeneratorService;

  private ObjectFactory objectFactory = ObjectFactorInitializer.getObjectFactory();


  @Test
  void contextLoads() {
    assertThat(xbrlGeneratorService).isNotNull();
  }

  @Test
  public void marshalWithSpecialCharacters() throws DatatypeConfigurationException {

    Xbrl xbrl = buildXbrl();
    String value = xbrlGeneratorService.generateXbrlReport(xbrl);
    assertThat(value).isNotBlank();
  }

  private Xbrl buildXbrl() throws DatatypeConfigurationException {
    Context context = buildContext();
    Xbrl xbrl = objectFactory.createXbrl();
    xbrl.getSchemaRef().add(buildSimpleType());
    xbrl.getItemOrTupleOrContext().add(context);
    xbrl.getItemOrTupleOrContext().add(getRocra(context));
    return xbrl;
  }


  private SimpleType buildSimpleType() {
    SimpleType simpleType = objectFactory.createSimpleType();
    simpleType.setType("simple");
    simpleType.setHref("http://xbrl.sec.gov/rocr/2015/ratings-2015-03-31.xsd");
    return simpleType;
  }

  private Context buildContext() {
    Context context = objectFactory.createContext();
    context.setId("m");
    context.setPeriod(buildContextPeriodType());
    context.setEntity(buildContextEntityType());

    return context;
  }

  private ContextEntityType buildContextEntityType() {
    ContextEntityType contextEntityType = objectFactory.createContextEntityType();
    contextEntityType.setIdentifier(buildIdentifier());
    return contextEntityType;
  }

  private Identifier buildIdentifier() {
    Identifier identifier = objectFactory.createContextEntityTypeIdentifier();
    identifier.setScheme("http://www.sec.gov/NRSRO");
    identifier.setValue("name");
    return identifier;
  }

  private ContextPeriodType buildContextPeriodType() {
    ContextPeriodType contextPeriodType = objectFactory.createContextPeriodType();
    contextPeriodType.setStartDate(LocalDate.now().toString());
    contextPeriodType.setEndDate(LocalDate.now().toString());
    return contextPeriodType;
  }


  private JAXBElement<ROCRA> getRocra(Context context) throws DatatypeConfigurationException {
    ROCRA rocra = objectFactory.createROCRA();
    rocra.setRAN(buildStringItemType(context, "ratingAgencyName"));
    rocra.setFCD(buildDateItemType(context));
    rocra.getOD().add(buildObligor(context));
    return objectFactory.createROCRA(rocra);
  }

  private OD buildObligor(Context context) throws DatatypeConfigurationException {

    OD od = objectFactory.createOD();
    od.getContent().add(objectFactory.createOSC(buildSECCategoryItemType(context, "Insurance")));
    od.getContent().add(objectFactory.createOIG(buildStringItemType(context, "OIG")));
    od.getContent().add(objectFactory.createOBNAME(buildStringItemType(context, "OB")));
    od.getContent().add(objectFactory.createLEI(buildLei(context, "529900AQBND3S6YJLY83")));
    od.getContent().add(objectFactory.createORD(buildRatingDetails(context)));

    return od;
  }


  private DateItemType buildDateItemType(Context context) throws DatatypeConfigurationException {
    DateItemType dateItemType = objectFactory.createDateItemType();
    dateItemType.setContextRef(context);
    XMLGregorianCalendar xmlGregorianCalendar = DatatypeFactory.newInstance()
        .newXMLGregorianCalendar(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE));
    dateItemType.setValue(xmlGregorianCalendar);
    return dateItemType;
  }


  private StringItemType buildStringItemType(Context context, String value) {
    StringItemType stringItemType = objectFactory.createStringItemType();
    stringItemType.setValue(value);
    stringItemType.setContextRef(context);
    return stringItemType;
  }

  private SECCategoryItemType buildSECCategoryItemType(Context context, String value) {
    SECCategoryItemType stringItemType = objectFactory.createSECCategoryItemType();
    stringItemType.setValue(value);
    stringItemType.setContextRef(context);
    return stringItemType;
  }

  private LeiItemType buildLei(Context context, String value) {
    LeiItemType stringItemType = objectFactory.createLeiItemType();
    stringItemType.setValue(value);
    stringItemType.setContextRef(context);
    return stringItemType;

  }

  private RatingDetailType buildRatingDetails(Context context) throws DatatypeConfigurationException {

    RatingDetailType ratingDetailType = objectFactory.createRatingDetailType();

    BooleanItemType ip = objectFactory.createBooleanItemType();
    ip.setContextRef(context);
    ip.setValue(true);
    ratingDetailType.getContent().add(objectFactory.createIP(ip));
    ratingDetailType.getContent().add(objectFactory.createR(buildStringItemType(context, "R")));
    ratingDetailType.getContent().add(objectFactory.createRAD(buildDateItemType(context)));
    RAC rac = objectFactory.createRAC();
    rac.setValue("HS");
    rac.setContextRef(context);
    ratingDetailType.getContent().add(objectFactory.createRAC(rac));
    ratingDetailType.getContent().add(objectFactory.createWST(buildStringItemType(context, "</watchStatus>")));
    ratingDetailType.getContent().add(objectFactory.createROL(buildStringItemType(context, "' ratingOutlook with ' : ; & ^")));
    ratingDetailType.getContent().add(objectFactory.createOAN(buildStringItemType(context, "! % $ < @ ! # % / ?'> , .")));
    ratingDetailType.getContent().add(objectFactory.createRT(buildStringItemType(context, "ä ö ü")));
    ratingDetailType.getContent().add(objectFactory.createRST(buildStringItemType(context, " ù û ü ÿ € à â æ ç é è ê ë ï î ô")));
    ratingDetailType.getContent().add(objectFactory.createRTT(buildStringItemType(context, "A	Á	B	C	Cs	D	Dz	Dzs	E	É	F	G	Gy	H	I	Í	J	K	L	Ly	M	N	Ny	O	Ó	Ö	Ő	P	Q	R	S	Sz	T	Ty	U	Ú	Ü	Ű	V	W	X	Y	Z	Zs")));

    return ratingDetailType;
  }
}

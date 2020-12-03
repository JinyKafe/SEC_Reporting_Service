package de.scope.scopeone.reporting.sec.module.xbrlgenerator.config;

import gov.sec.ratings.Xbrl;
import java.util.HashMap;
import java.util.Map;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.Resource;

@SuppressWarnings("squid:S1191" /* Classes from "sun.*" packages should not be used */)
@Configuration
@ComponentScan(basePackages = "de.scope.scopeone.reporting.sec.module.xbrlgenerator")
public class XbrlGeneratorConf {

  private static final ThreadLocal<SchemaFactory> SCHEMA_FACTORY = ThreadLocal.withInitial(() -> SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI));
  @Value("${app.config.xbrlreports.xsd:classpath:/xbrlschema/ratings-2015-03-31.xsd}")
  private Resource resource;

  @SneakyThrows
  @Bean
  @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  Marshaller xbrlMarchaller(final com.sun.xml.bind.marshaller.NamespacePrefixMapper xbrlNamespacePrefixMapper) {
    JAXBContext context = JAXBContext.newInstance(Xbrl.class);

    Marshaller marshaller = context.createMarshaller();
    SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    Schema schema = sf.newSchema(resource.getFile());
    marshaller.setSchema(schema);
    marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-16");
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
    marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION,
        "http://xbrl.sec.gov/ratings/2015-03-31 http://xbrl.sec.gov/rocr/2015/ratings-2015-03-31.xsd");
    marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", xbrlNamespacePrefixMapper);
    return marshaller;
  }

  @Bean
  public com.sun.xml.bind.marshaller.NamespacePrefixMapper xbrlNamespacePrefixMapper() {
    return new XbrlNamespacePrefixMapper();
  }


  @SneakyThrows
  @Bean
  Validator xbrlValidator() {
    Schema schema = SCHEMA_FACTORY.get().newSchema(resource.getFile());
    return schema.newValidator();
  }

  public static class XbrlNamespacePrefixMapper extends com.sun.xml.bind.marshaller.NamespacePrefixMapper {

    private Map<String, String> namespaceMap = new HashMap<>();

    /**
     * Create mappings.
     */
    public XbrlNamespacePrefixMapper() {
      namespaceMap.put("http://www.w3.org/2001/XMLSchema", "xs");
      namespaceMap.put("http://xbrl.sec.gov/ratings/2015-03-31", "r");
      namespaceMap.put("http://www.xbrl.org/2003/instance", "xbrli");
      namespaceMap.put("http://www.xbrl.org/2003/linkbase", "link");
      namespaceMap.put("http://www.w3.org/2001/XMLSchema-instance", "xsi");
      namespaceMap.put("http://www.w3.org/1999/xlink", "xlink");
      namespaceMap.put("http://www.xbrl.org/2003/XLink", "XLink");
      namespaceMap.put("http://www.xbrl.org/2003/iso4217", "ISO4217");
    }


    @Override
    public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
      return namespaceMap.getOrDefault(namespaceUri, suggestion);
    }
  }

}

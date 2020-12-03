package de.scope.scopeone.reporting.sec.module.xbrlgenerator.service;

import com.google.common.base.Preconditions;
import de.scope.scopeone.reporting.sec.common.error.SecBusinessException;
import de.scope.scopeone.reporting.sec.common.error.SecErrorCode;
import de.scope.scopeone.reporting.sec.common.error.SecInternalError;
import de.scope.scopeone.reporting.sec.common.error.TODOReminderError;
import gov.sec.ratings.Xbrl;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import lombok.SneakyThrows;
import org.apache.commons.io.output.StringBuilderWriter;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

@Service
public class XbrlGeneratorServiceImpl implements XbrlGeneratorService {

  private final ObjectProvider<Marshaller> xbrlMarchallerProvider;

  public XbrlGeneratorServiceImpl(ObjectProvider<Marshaller> xbrlMarchallerProvider) {
    this.xbrlMarchallerProvider = xbrlMarchallerProvider;
  }

  private static void validateRequestArguments(Xbrl xbrl) {
    try {
      // TO_DO do the validation. Consider using javax.validation framework and annotations like @NotNull and other validation constraints
      Preconditions.checkNotNull(xbrl, "root node must not be null");
    } catch (Exception e) {
      throw new SecBusinessException(SecErrorCode.INVALID_DATA, "JAXB validtaion error", e);
    }
  }

  @SneakyThrows
  @Override
  public String generateXbrlReport(Xbrl xbrl) {
    validateRequestArguments(xbrl);

    try (StringBuilderWriter writer = new StringBuilderWriter()) {
      newMarshaller().marshal(xbrl, writer);
      return writer.toString();
    } catch (JAXBException e) {
      throw new TODOReminderError(SecErrorCode.INVALID_DATA, "JAXB validtaion error: TODO handle it before marshalling", e);
    } catch (Exception e) {
      throw new SecInternalError(SecErrorCode.UNKNOWN_ERROR, "Unable to marshall xbrl object", e);
    }
  }

  private Marshaller newMarshaller() {
    return xbrlMarchallerProvider.getIfAvailable(() -> {
      throw new SecInternalError(SecErrorCode.BUG, "Unable to obtain Marshaller bean");
    });
  }
}

package de.scope.scopeone.reporting.sec.module.xbrlmapper.builder;

import de.scope.scopeone.reporting.sec.module.xbrlgenerator.util.ObjectFactorInitializer;
import gov.sec.ratings.Context;
import gov.sec.ratings.ISD;
import gov.sec.ratings.OD;
import gov.sec.ratings.ROCRA;
import java.time.LocalDateTime;

/**
 * Record of credit rating action
 */
public class RocraBuilder {

  private ROCRA rocra;

  private Context contextRef;

  public RocraBuilder() {
    rocra = ObjectFactorInitializer.getObjectFactory().createROCRA();
  }

  public ROCRA build() {
    return rocra;
  }

  public RocraBuilder addContextRef(Context contextRef) {
    this.contextRef = contextRef;
    return this;
  }


  /**
   * The Rating Agency Name, equal to the text content of <xbrli:identifier>
   *
   * @param ratingAgencyName
   * @return
   */
  public RocraBuilder addRAN(String ratingAgencyName) {
    rocra.setRAN(new StringItemTypeBuilder<>().setContextRef(contextRef).setValue(ratingAgencyName).build());
    return this;
  }

  /**
   * The File Creation Date in ISO 8601 format YYYY-MM-DD; the date the file was created and made available on the NRSRO’s web site. Example content:
   * “2015-03-01”.
   *
   * @param fcd
   * @return
   */

  public RocraBuilder setFCD(LocalDateTime creationDate) {
    if (creationDate == null) {
      return this;
    }
    rocra.setFCD(new DateItemTypeBuilder().setContextRef(contextRef).setValue(creationDate).build());
    return this;
  }

  public RocraBuilder addOD(OD od) {
    rocra.getOD().add(od);
    return this;
  }

  public RocraBuilder addISD(ISD isd) {
    rocra.getISD().add(isd);
    return this;
  }
}

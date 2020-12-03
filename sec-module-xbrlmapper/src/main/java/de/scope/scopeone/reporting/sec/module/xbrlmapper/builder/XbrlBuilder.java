package de.scope.scopeone.reporting.sec.module.xbrlmapper.builder;

import static de.scope.scopeone.reporting.sec.module.xbrlgenerator.util.ObjectFactorInitializer.getObjectFactory;

import gov.sec.ratings.Context;
import gov.sec.ratings.ROCRA;
import gov.sec.ratings.SimpleType;
import gov.sec.ratings.Unit;
import gov.sec.ratings.Xbrl;
import javax.xml.bind.JAXBElement;

public class XbrlBuilder {

  private Xbrl xbrl;

  public XbrlBuilder() {
    xbrl = getObjectFactory().createXbrl();
  }

  public Xbrl build() {
    return xbrl;
  }

  public XbrlBuilder addSchemaRef(SimpleType schemaRef) {
    if (schemaRef == null) {
      return this;
    }
    xbrl.getSchemaRef().add(schemaRef);
    return this;
  }

  public XbrlBuilder addContext(Context context) {
    if (context == null) {
      return this;
    }
    xbrl.getItemOrTupleOrContext().add(context);
    return this;
  }

  public XbrlBuilder addUnit(Unit unit) {
    if (unit == null) {
      return this;
    }
    xbrl.getItemOrTupleOrContext().add(unit);
    return this;
  }

  public XbrlBuilder addRocra(ROCRA rocra) {
    if (rocra == null) {
      return null;
    }
    xbrl.getItemOrTupleOrContext().add(getObjectFactory().createROCRA(rocra));
    return this;
  }

  public XbrlBuilder addItem(JAXBElement item) {
    xbrl.getItemOrTupleOrContext().add(item);
    return this;
  }

}

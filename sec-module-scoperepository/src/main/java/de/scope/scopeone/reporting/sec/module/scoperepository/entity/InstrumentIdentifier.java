package de.scope.scopeone.reporting.sec.module.scoperepository.entity;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "INSTRUMENT_IDENTIFIER")
public class InstrumentIdentifier {

  @EmbeddedId
  private InstrumentIdentifierPK id;

  @NotNull
  @Column(name = "IDENTIFIER")
  private String identifier;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InstrumentIdentifier that = (InstrumentIdentifier) o;
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
        .add("identifier", identifier)
        .toString();
  }
}

package de.scope.scopeone.reporting.sec.module.scoperepository.entity.enums;

public enum ObligorOrIssuerIdentifierSchemeType {


  /**
   * Dun & Bradstreet D-U-N-S Number
   */
  DUNS(0),

  /**
   * Business Identifier Code (ISO 9362)
   */
  BIC(1),

  /**
   * nine-digit numeric or nine-character alphanumeric code that identifies a North American financial security for the purposes of facilitating clearing and
   * settlement of trades. The CUSIP was adopted as an American National Standard under Accredited Standards X9.6
   */
  CUSIP(2),

  /**
   * Securities Identification Code Committee. For Japan
   */
  SICC(3),
  /**
   * Obligor or issuer identifier scheme that is specific to the reporting NRSRO. In our case Scope Rating internal identifiers
   */
  NRSRO(4);


  /**
   * Ranking of the Scheme. The lowest number, the highest priority (ranking)
   */
  private final int ranking;

  ObligorOrIssuerIdentifierSchemeType(int ranking) {
    this.ranking = ranking;
  }

  public int getRanking() {
    return ranking;
  }
}

package de.scope.scopeone.reporting.sec.module.scoperepository.entity.enums;

public enum InstrumentIdentifierSchemeType {

  /**
   * International Securities Identification Number (ISO 9166)
   */
  ISIN(0),

  /**
   * Stock Exchange Daily Official List (UK)
   */
  SEDOL(1),

  /**
   * Valorennummer (CH)
   */
  VALOR(2),

  /**
   * Wertpapierkennnummer (DE)
   */
  WK(3),

  /**
   * Securities Identification Code Committee (JP)
   */
  SICC(4),

  /**
   * Instrument identifier scheme that is specific to the reporting NRSRO
   */
  NRSRO(5);


  /**
   * Ranking of the Scheme. The lowest number, the highest priority (ranking) of the scheme
   */
  private final int ranking;

  InstrumentIdentifierSchemeType(int ranking) {
    this.ranking = ranking;
  }

  public int getRanking() {
    return ranking;
  }
}

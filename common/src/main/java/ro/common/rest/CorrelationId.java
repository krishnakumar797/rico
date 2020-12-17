/* Licensed under Apache-2.0 */
package ro.common.rest;

/**
 * Correlation id interface for the API requests
 *
 * @author krishna
 */
public interface CorrelationId {

  String getCorrelationId();

  void setCorrelationId(String correlationId);
}

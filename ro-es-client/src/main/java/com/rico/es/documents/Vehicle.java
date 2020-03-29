/* Licensed under Apache-2.0 */
package com.rico.es.documents;

import java.util.Date;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import ro.common.elasticsearch.ESDoc;

/**
 * Elastic search sample document structure with dynamic index name. Here index name is based on
 * dates. This types of indexes are used to store large volume of data generated per day.
 *
 * <p>Helps to retrieve easily when we give dates
 *
 * @author r.krishnakumar
 */
@Document(createIndex = true, indexName = "vehicle_#{@currentDate}", shards = 2, replicas = 1)
public class Vehicle extends ESDoc {

  @Field(type = FieldType.Text)
  private String make;

  @Field(type = FieldType.Text)
  private String model;

  @Field(type = FieldType.Integer)
  private Integer manufacturingYear;

  @Field(
      type = FieldType.Date,
      index = true,
      store = true,
      format = DateFormat.date_hour_minute_second_millis)
  private Date createdDate;

  public String getMake() {
    return make;
  }

  public void setMake(String make) {
    this.make = make;
  }

  public String getModel() {
    return model;
  }

  public void setModel(String model) {
    this.model = model;
  }

  public Date getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }

  public Integer getManufacturingYear() {
    return manufacturingYear;
  }

  public void setManufacturingYear(Integer manufacturingYear) {
    this.manufacturingYear = manufacturingYear;
  }
}

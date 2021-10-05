/* Licensed under Apache-2.0 */
package ro.common.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Abstract class for all Entity class
 *
 * @author r.krishnakumar
 */
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class EntityDoc implements Doc {

  @Id
  @GeneratedValue(generator = "key-generator")
  @GenericGenerator(name = "key-generator", strategy = "ro.common.utils.RandomGenerator")
  private Long id;

  @Version private Long version;

  @CreatedBy
  @Column(name = "created_by")
  private String createdBy;

  @CreatedDate
  @Column(name = "created_date", columnDefinition = "DATE")
  private LocalDate createdDate;

  @CreationTimestamp
  @Column(name = "created_date_time", columnDefinition = "TIMESTAMP")
  private LocalDateTime createdDateTime;

  @LastModifiedBy
  @Column(name = "last_modified_by")
  private String lastModifiedBy;

  @LastModifiedDate
  @Column(name = "last_modified_date", columnDefinition = "DATE")
  private LocalDate lastModifiedDate;

  @UpdateTimestamp
  @Column(name = "last_modified_date_time", columnDefinition = "TIMESTAMP")
  private LocalDateTime lastModifiedDateTime;
}

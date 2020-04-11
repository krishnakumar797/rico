package ro.common.utils;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Getter;
import lombok.Setter;

/**
 * Abstract class for all Entity class
 *
 * @author r.krishnakumar
 */
@Getter
@Setter
@MappedSuperclass
public abstract class EntityDoc implements Doc {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Version private Long version;

  @CreationTimestamp
  @Column(name = "create_date", columnDefinition = "TIMESTAMP")
  private LocalDateTime createDateTime;

  @UpdateTimestamp
  @Column(name = "modify_date", columnDefinition = "TIMESTAMP")
  private LocalDateTime modifyDateTime;
}

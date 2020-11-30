package ro.common.rest;

import org.modelmapper.ModelMapper;

import ro.common.utils.EntityDoc;

/**
 * Common DTO converter class
 *
 * @author krishna
 */
@SuppressWarnings("unchecked")
public class CommonDTOConverter {

  private ModelMapper mapper;

  public CommonDTOConverter(ModelMapper mapper) {
    this.mapper = mapper;
  }

  /**
   * Method to convert an entity of type EntityDoc to Dto of type CommonDTO
   *
   * @param <D>
   * @param <E>
   * @param e
   * @param dto
   * @return
   */
  public <D extends CommonDTO, E extends EntityDoc> D convertToDto(
      E e, Class<? extends CommonDTO> dto) {
    return (D) mapper.map(e, dto);
  }

  /**
   * Method to convert a Dto of type CommonDTO to the entity of type EntityDoc
   *
   * @param <D>
   * @param <E>
   * @param dto
   * @param entity
   * @return
   */
  public <D extends CommonDTO, E extends EntityDoc> E convertToEntity(
      D dto, Class<? extends EntityDoc> entity) {
    return (E) mapper.map(dto, entity);
  }
}

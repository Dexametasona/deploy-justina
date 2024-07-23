package com.no_country.justina.service.implementation;

import com.no_country.justina.model.entities.Indication;
import com.no_country.justina.model.enums.DrugStatus;
import com.no_country.justina.repository.IndicationRepository;
import com.no_country.justina.service.interfaces.IIndicationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class IndicationServiceImp implements IIndicationService {
  private final IndicationRepository indicationRepo;

  @Override
  public Indication create(Indication indication) {
    return this.indicationRepo.save(indication);
  }

  @Transactional
  @Override
  public Set<Indication> createAll(Set<Indication> indications) {
    this.verifyAllHaveValidDate(indications);
    this.verifyAllSamePrescription(indications);
    var response = this.indicationRepo.saveAll(indications);
    return new HashSet<>(response);
  }

  @Override
  public Indication getById(Long id) {
    return this.indicationRepo.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Indicación no encontrada, id:" + id));
  }

  @Override
  public Page<Indication> getAll(Pageable pageable) {
    return this.indicationRepo.findAll(pageable);
  }

  @Override
  public Indication update(Indication indication) {
    this.verifyIndicationExist(indication.getId());
    return this.indicationRepo.save(indication);
  }

  @Override
  public void deleteById(Long id) {
    this.verifyIndicationExist(id);
    this.indicationRepo.deleteById(id);
  }

  @Override
  public Indication stopMedication(long id) {
    this.indicationRepo.stopIndication(id, DrugStatus.SUSPEND);
    return this.getById(id);
  }

  private void verifyIndicationExist(long id) {
    boolean existIndication = this.indicationRepo.existsById(id);
    if (!existIndication) throw new EntityNotFoundException("Indicación no encontrada, id:" + id);
  }

  private void verifyAllSamePrescription(Set<Indication> indications) {
    boolean allMatches = indications.stream()
            .map(Indication::getId)
            .distinct()
            .count() == 1;
    if (!allMatches) throw new IllegalArgumentException("No todas las indicaciones pertenecen a la misma receta");
  }

  private void verifyAllHaveValidDate(Set<Indication> indications) {
    indications.forEach(indication->{
      if(indication.getStartDate().isBefore(LocalDate.now())){
        throw new IllegalArgumentException("Algunas indicaciones tienen fechas en tiempo pasado");
      }
    });
  }
}

package com.no_country.justina.repository;

import com.no_country.justina.model.entities.Shift;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, Long>, JpaSpecificationExecutor<Shift> {

  @Query("SELECT s FROM Shift s WHERE s.doctor.id = :id AND YEAR(s.startDate) = :year AND MONTH(s.startDate) = :month ORDER BY s.startDate ASC")
  List<Shift> findByDoctorAndMonth(Long id, int year, int month);

  @Query("SELECT s FROM Shift s WHERE s.doctor.id = :id AND YEAR(s.startDate) = :year AND MONTH(s.startDate) = :month AND DAY(s.startDate) = :day")
  List<Shift> findByDayAndDoctor(Long id, int year, int month, int day);

  @Query("SELECT s FROM Shift s WHERE s.specialty.name = :specialty AND YEAR(s.startDate) = :year AND MONTH(s.startDate) = :month")
  List<Shift> findBySpecialtyAndMonth(String specialty, int year, int month);

  @Query("select s from Shift s where s.startDate between ?1 and ?2")
  Page<Shift> findShiftsMonthBetween(LocalDateTime startDateStart, LocalDateTime startDateEnd, Pageable pageable);

  @Transactional
  @Modifying
  @Query("update Shift s set s.appointment = s.appointment - :quantity where s.idShift = :id")
  int updateAppointmentShift(long id, int quantity);

  default Page<Shift> findAllByDoctorOrSpecialty(Pageable pageable,
                                                 Long doctorId,
                                                 String specialty,
                                                 LocalDateTime start,
                                                 LocalDateTime end) {
    return findAll((Root<Shift> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
      List<Predicate> predicates = new ArrayList<>();
      if (doctorId != null) {
        predicates.add(builder.equal(root.get("doctor").get("id"), doctorId));
      }
      if (specialty != null) {
        predicates.add(builder.equal(root.get("specialty").get("name"), specialty));
      }
      predicates.add(builder.between(root.get("startDate"), start, end));
      return builder.and(predicates.toArray(new Predicate[0]));

    }, pageable);
  }
}

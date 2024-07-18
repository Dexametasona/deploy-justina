package com.no_country.justina.repository;

import com.no_country.justina.model.entities.Patient;
import com.no_country.justina.model.enums.Genre;
import com.no_country.justina.model.enums.MaritalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
  @Transactional
  @Modifying
  @Query("""
          update Patient p set p.docIdentity = ?1, p.phone = ?2, p.address = ?3, p.birthdate = ?4, p.maritalStatus = ?5, p.genre = ?6
          where p.idPatient = ?7""")
  int updateById(String docIdentity,
                 String phone,
                 String address,
                 LocalDate birthdate,
                 MaritalStatus maritalStatus,
                 Genre genre,
                 Long idPatient);
}

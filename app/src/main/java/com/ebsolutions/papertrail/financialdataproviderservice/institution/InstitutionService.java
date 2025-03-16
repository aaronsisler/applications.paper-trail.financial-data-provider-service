package com.ebsolutions.papertrail.financialdataproviderservice.institution;

import com.ebsolutions.papertrail.financialdataproviderservice.common.exception.DataConstraintException;
import com.ebsolutions.papertrail.financialdataproviderservice.common.exception.DataProcessingException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class InstitutionService {
  private final InstitutionRepository institutionRepository;

  public List<Institution> getAll() {
    try {
      return institutionRepository.findAll();
    } catch (Exception exception) {
      log.error("Error getting all", exception);
      throw new DataProcessingException("Something went wrong while fetching all institutions");
    }
  }

  public Optional<Institution> get(Integer institutionId) {
    try {
      return institutionRepository.findById(institutionId.longValue());
    } catch (Exception exception) {
      log.error("Error getting by id", exception);
      throw new DataProcessingException("Something went wrong while fetching the institution");
    }
  }

  public List<Institution> createAll(List<Institution> institutions) {
    try {
      if (institutions.isEmpty()) {
        throw new DataConstraintException(
            Collections.singletonList("Institutions cannot be empty"));
      }

      if (!institutions.stream()
          .filter(institution -> institution.getId() != null)
          .toList()
          .isEmpty()) {

        List<String> existingUserErrorMessages =
            institutions.stream()
                .filter(institution -> institution.getId() != null)
                .map(institution -> "Institution Id cannot be populated: ".concat(
                    Integer.toString(institution.getId())))
                .toList();

        throw new DataConstraintException(existingUserErrorMessages);
      }

      return institutionRepository.saveAll(institutions);
    } catch (DataConstraintException dataConstraintException) {
      throw dataConstraintException;
    } catch (Exception exception) {
      log.error("Error creating", exception);
      throw new DataProcessingException("Something went wrong while saving all institutions");
    }
  }

  public Institution update(Institution institution) {
    try {
      if (institution.getId() <= 0) {
        List<String> existingInstitutionsErrorMessages =
            Collections.singletonList("Institution Id must be positive and non-zero");

        throw new DataConstraintException(existingInstitutionsErrorMessages);
      }

      boolean doesInstitutionExist =
          institutionRepository.findById((long) institution.getId()).isPresent();

      if (!doesInstitutionExist) {
        List<String> existingInstitutionsErrorMessages =
            Collections.singletonList(
                "Institution Id does not exist: ".concat(
                    Integer.toString(institution.getId())));

        throw new DataConstraintException(existingInstitutionsErrorMessages);
      }

      return institutionRepository.save(institution);
    } catch (DataConstraintException dataConstraintException) {
      throw dataConstraintException;
    } catch (Exception exception) {
      log.error("Error saving", exception);
      throw new DataProcessingException("Something went wrong while saving the institution");
    }
  }

  public void delete(Integer institutionId) {
    try {
      institutionRepository.deleteById(institutionId.longValue());
    } catch (DataConstraintException dataConstraintException) {
      throw dataConstraintException;
    } catch (Exception exception) {
      log.error("Error deleting", exception);
      throw new DataProcessingException("Something went wrong while deleting the institution");
    }
  }
}

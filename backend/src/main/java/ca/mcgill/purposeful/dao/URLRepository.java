package ca.mcgill.purposeful.dao;

import ca.mcgill.purposeful.model.URL;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

/** Repository for URL */
public interface URLRepository extends CrudRepository<URL, Integer> {

  URL findURLById(String id);
  ArrayList<URL> findURLByURL(String URL);
}

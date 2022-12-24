package ro.ananimarius.allride.allride.CRUDinterfaces;

import org.springframework.data.repository.CrudRepository;
import ro.ananimarius.allride.allride.rating.Rating;

public interface RatingRepository extends CrudRepository<Rating, Long> {
}

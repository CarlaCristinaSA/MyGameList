package br.com.jovirds.repository;

import br.com.jovirds.model.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GameRepository extends JpaRepository<Game, Long> {

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Game g SET g.finished = true WHERE g.id =:id")
    void finishGame(@Param("id") Long id);

    @Query("SELECT g FROM Game g WHERE LOWER(g.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Game> findGamesByName(@Param("name") String name, Pageable pageable);

}

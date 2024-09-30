package com.example.tournament.repository;

import com.example.tournament.model.BallByBall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BallByBallRepository extends JpaRepository<BallByBall, Integer> {
    //Calculate overs bowled by a bowler in an innings.
    @Query("SELECT count(distinct(bb.overNumber)) from BallByBall bb where bb.bowler = :bowlerId and bb.iid = :iid")
    int countByBowlerId(@Param("bowlerId") String bowlerId, @Param("iid") int iid);

    //Find all by innings id.
    List<BallByBall> findByIid(int iid);
}

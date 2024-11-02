package com.peterparker.model;

import javax.persistence.*;

@Entity
@Table(name = "Space")
public class Space {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "spaceId")
    private Integer spaceId;

    @Column(name = "parkingId", nullable = false)
    private Integer parkingId;

    // Constructors
    public Space() {
    }

    public Space(Integer parkingId) {
        this.parkingId = parkingId;
    }

    // Getters and Setters
    public Integer getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(Integer spaceId) {
        this.spaceId = spaceId;
    }

    public Integer getParkingId() {
        return parkingId;
    }

    public void setParkingId(Integer parkingId) {
        this.parkingId = parkingId;
    }

    @Override
    public String toString() {
        return "Space{" +
                "spaceId=" + spaceId +
                ", parkingId=" + parkingId +
                '}';
    }
}

package com.tibame.peterparker.model;

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

    @Column(name = "spaceNo", length = 5)
    private String spaceNo;

    // Constructors
    public Space() {
    }

    public Space(Integer parkingId, String spaceNo) {
        this.parkingId = parkingId;
        this.spaceNo = spaceNo;
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

    public String getSpaceNo() {
        return spaceNo;
    }

    public void setSpaceNo(String spaceNo) {
        this.spaceNo = spaceNo;
    }

    @Override
    public String toString() {
        return "Space{" +
                "spaceId=" + spaceId +
                ", parkingId=" + parkingId +
                ", spaceNo='" + spaceNo + '\'' +
                '}';
    }
}

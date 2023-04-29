package com.driver.model;

import javax.persistence.*;

@Entity
@Table(name="reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int numberOfHours;
    //relations
    @ManyToOne
    @JoinColumn
    Spot spot;
    @ManyToOne
    @JoinColumn
    User user;
    @OneToOne(mappedBy = "reservation",cascade = CascadeType.ALL)
    Payment payment;
    //constructors and getter and setters
    Reservation(){}

    public Reservation(int numberOfHours, Spot spot, User user) {
        this.numberOfHours = numberOfHours;
        this.spot = spot;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumberOfHours() {
        return numberOfHours;
    }

    public void setNumberOfHours(int numberOfHours) {
        this.numberOfHours = numberOfHours;
    }

    public Spot getSpot() {
        return spot;
    }

    public void setSpot(Spot spot) {
        this.spot = spot;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }
}

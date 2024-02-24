package com.amigowallet.entity;


import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "OFFER")
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "CASHBACK_PERCENTAGE")
    private Integer cashBackPercentage;


    @Column(name = "MAX_CASHBACK")
    private Integer maxCashBack;

    @Column(name = "MIN_TRAN_REQ")
    private Integer minTranReq;

    @OneToOne
    @JoinColumn(name = "SERVICE_TYPE_ID")
    private MerchantServiceType catagory;

    @Column(name = "STATUS")
    private Boolean isActive;

    @ManyToMany(mappedBy = "offers")
    private List<User> users;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCashBackPercentage() {
        return cashBackPercentage;
    }

    public void setCashBackPercentage(Integer cashBackPercentage) {
        this.cashBackPercentage = cashBackPercentage;
    }

    public Integer getMaxCashBack() {
        return maxCashBack;
    }

    public void setMaxCashBack(Integer maxCashBack) {
        this.maxCashBack = maxCashBack;
    }

    public Integer getMinTranReq() {
        return minTranReq;
    }

    public void setMinTranReq(Integer minTranReq) {
        this.minTranReq = minTranReq;
    }

    public MerchantServiceType getCatagory() {
        return catagory;
    }

    public void setCatagory(MerchantServiceType catagory) {
        this.catagory = catagory;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}

package com.dressca.infrastructure.repository.mybatis.generated.entity;

public class BasketEntity {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column baskets.id
     *
     * @mbg.generated Mon Feb 19 14:48:46 JST 2024
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column baskets.buyer_id
     *
     * @mbg.generated Mon Feb 19 14:48:46 JST 2024
     */
    private String buyerId;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column baskets.id
     *
     * @return the value of baskets.id
     *
     * @mbg.generated Mon Feb 19 14:48:46 JST 2024
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column baskets.id
     *
     * @param id the value for baskets.id
     *
     * @mbg.generated Mon Feb 19 14:48:46 JST 2024
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column baskets.buyer_id
     *
     * @return the value of baskets.buyer_id
     *
     * @mbg.generated Mon Feb 19 14:48:46 JST 2024
     */
    public String getBuyerId() {
        return buyerId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column baskets.buyer_id
     *
     * @param buyerId the value for baskets.buyer_id
     *
     * @mbg.generated Mon Feb 19 14:48:46 JST 2024
     */
    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }
}
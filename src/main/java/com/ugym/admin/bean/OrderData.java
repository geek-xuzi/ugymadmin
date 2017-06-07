package com.ugym.admin.bean;

/**
 * @author zheng.xu
 * @since 2017-06-06
 */
public class OrderData {

    private String order_id;
    private String order_product;
    private String main_order;
    private String create_date;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_product() {
        return order_product;
    }

    public void setOrder_product(String order_product) {
        this.order_product = order_product;
    }

    public String getMain_order() {
        return main_order;
    }

    public void setMain_order(String main_order) {
        this.main_order = main_order;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

}

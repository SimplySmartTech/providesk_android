package com.simplysmart.lib.model.walllet;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shekhar on 03/02/17.
 */
public class WalletCredentialResponse implements Parcelable {

    private String key;
    private String txnid;
    private String amount;
    private String productinfo;
    private String firstname;
    private String lastname;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String country;
    private String zipcode;
    private String email;
    private String phone;
    private String udf1;
    private String udf2;
    private String udf3;
    private String udf4;
    private String udf5;
    private String surl;
    private String furl;
    private String payment_hash;
    private String vas_for_mobile_sdk_hash;
    private String payment_related_details_for_mobile_sdk_hash;
    private String service_provider;
    private String merchant_id;

    private boolean sandbox;

    protected WalletCredentialResponse(Parcel in) {
        key = in.readString();
        txnid = in.readString();
        amount = in.readString();
        productinfo = in.readString();
        firstname = in.readString();
        lastname = in.readString();
        address1 = in.readString();
        address2 = in.readString();
        city = in.readString();
        state = in.readString();
        country = in.readString();
        zipcode = in.readString();
        email = in.readString();
        phone = in.readString();
        udf1 = in.readString();
        udf2 = in.readString();
        udf3 = in.readString();
        udf4 = in.readString();
        udf5 = in.readString();
        surl = in.readString();
        furl = in.readString();
        payment_hash = in.readString();
        vas_for_mobile_sdk_hash = in.readString();
        payment_related_details_for_mobile_sdk_hash = in.readString();
        service_provider = in.readString();
        merchant_id = in.readString();
        sandbox = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(txnid);
        dest.writeString(amount);
        dest.writeString(productinfo);
        dest.writeString(firstname);
        dest.writeString(lastname);
        dest.writeString(address1);
        dest.writeString(address2);
        dest.writeString(city);
        dest.writeString(state);
        dest.writeString(country);
        dest.writeString(zipcode);
        dest.writeString(email);
        dest.writeString(phone);
        dest.writeString(udf1);
        dest.writeString(udf2);
        dest.writeString(udf3);
        dest.writeString(udf4);
        dest.writeString(udf5);
        dest.writeString(surl);
        dest.writeString(furl);
        dest.writeString(payment_hash);
        dest.writeString(vas_for_mobile_sdk_hash);
        dest.writeString(payment_related_details_for_mobile_sdk_hash);
        dest.writeString(service_provider);
        dest.writeString(merchant_id);
        dest.writeByte((byte) (sandbox ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WalletCredentialResponse> CREATOR = new Creator<WalletCredentialResponse>() {
        @Override
        public WalletCredentialResponse createFromParcel(Parcel in) {
            return new WalletCredentialResponse(in);
        }

        @Override
        public WalletCredentialResponse[] newArray(int size) {
            return new WalletCredentialResponse[size];
        }
    };

    public boolean isSandbox() {
        return sandbox;
    }

    public void setSandbox(boolean sandbox) {
        this.sandbox = sandbox;
    }

    public String getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTxnid() {
        return txnid;
    }

    public void setTxnid(String txnid) {
        this.txnid = txnid;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getProductinfo() {
        return productinfo;
    }

    public void setProductinfo(String productinfo) {
        this.productinfo = productinfo;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUdf1() {
        return udf1;
    }

    public void setUdf1(String udf1) {
        this.udf1 = udf1;
    }

    public String getUdf2() {
        return udf2;
    }

    public void setUdf2(String udf2) {
        this.udf2 = udf2;
    }

    public String getUdf3() {
        return udf3;
    }

    public void setUdf3(String udf3) {
        this.udf3 = udf3;
    }

    public String getUdf4() {
        return udf4;
    }

    public void setUdf4(String udf4) {
        this.udf4 = udf4;
    }

    public String getUdf5() {
        return udf5;
    }

    public void setUdf5(String udf5) {
        this.udf5 = udf5;
    }

    public String getSurl() {
        return surl;
    }

    public void setSurl(String surl) {
        this.surl = surl;
    }

    public String getFurl() {
        return furl;
    }

    public void setFurl(String furl) {
        this.furl = furl;
    }

    public String getService_provider() {
        return service_provider;
    }

    public void setService_provider(String service_provider) {
        this.service_provider = service_provider;
    }

    public String getPayment_hash() {
        return payment_hash;
    }

    public void setPayment_hash(String payment_hash) {
        this.payment_hash = payment_hash;
    }

    public String getVas_for_mobile_sdk_hash() {
        return vas_for_mobile_sdk_hash;
    }

    public void setVas_for_mobile_sdk_hash(String vas_for_mobile_sdk_hash) {
        this.vas_for_mobile_sdk_hash = vas_for_mobile_sdk_hash;
    }

    public String getPayment_related_details_for_mobile_sdk_hash() {
        return payment_related_details_for_mobile_sdk_hash;
    }

    public void setPayment_related_details_for_mobile_sdk_hash(String payment_related_details_for_mobile_sdk_hash) {
        this.payment_related_details_for_mobile_sdk_hash = payment_related_details_for_mobile_sdk_hash;
    }
}

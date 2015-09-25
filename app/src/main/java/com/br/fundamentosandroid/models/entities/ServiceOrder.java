package com.br.fundamentosandroid.models.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class ServiceOrder implements Parcelable {

    private Integer mId;
    private String mClient;
    private String mPhone;
    private String mAddress;
    private Date mDate;
    private double mValue;
    private boolean mPaid;
    private String mDescription;
    private boolean mActive;
    private User mUser;

    public boolean isActive() {
        return mActive;
    }

    public void setActive(boolean active) {
        mActive = active;
    }

    public ServiceOrder() {
        super();
    }

    public Integer getId() {
        return mId;
    }

    public void setId(Integer id) {
        mId = id;
    }

    public String getClient() {
        return mClient;
    }

    public void setClient(String client) {
        mClient = client;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String phone) {
        mPhone = phone;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public double getValue() {
        return mValue;
    }

    public void setValue(double value) {
        mValue = value;
    }

    public boolean isPaid() {
        return mPaid;
    }

    public void setPaid(boolean paid) {
        mPaid = paid;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ServiceOrder that = (ServiceOrder) o;

        if (Double.compare(that.mValue, mValue) != 0) return false;
        if (mPaid != that.mPaid) return false;
        if (mActive != that.mActive) return false;
        if (mId != null ? !mId.equals(that.mId) : that.mId != null) return false;
        if (mClient != null ? !mClient.equals(that.mClient) : that.mClient != null) return false;
        if (mPhone != null ? !mPhone.equals(that.mPhone) : that.mPhone != null) return false;
        if (mAddress != null ? !mAddress.equals(that.mAddress) : that.mAddress != null)
            return false;
        if (mDate != null ? !mDate.equals(that.mDate) : that.mDate != null) return false;
        return !(mDescription != null ? !mDescription.equals(that.mDescription) : that.mDescription != null);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = mId != null ? mId.hashCode() : 0;
        result = 31 * result + (mClient != null ? mClient.hashCode() : 0);
        result = 31 * result + (mPhone != null ? mPhone.hashCode() : 0);
        result = 31 * result + (mAddress != null ? mAddress.hashCode() : 0);
        result = 31 * result + (mDate != null ? mDate.hashCode() : 0);
        temp = Double.doubleToLongBits(mValue);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (mPaid ? 1 : 0);
        result = 31 * result + (mDescription != null ? mDescription.hashCode() : 0);
        result = 31 * result + (mActive ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ServiceOrder{" +
                "mId=" + mId +
                ", mClient='" + mClient + '\'' +
                ", mPhone='" + mPhone + '\'' +
                ", mAddress='" + mAddress + '\'' +
                ", mDate=" + mDate +
                ", mValue=" + mValue +
                ", mPaid=" + mPaid +
                ", mDescription='" + mDescription + '\'' +
                ", mActive=" + mActive +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    private ServiceOrder(Parcel in) {
        mId = (Integer) in.readValue(Integer.class.getClassLoader());
        mClient = in.readString();
        mPhone = in.readString();
        mAddress = in.readString();
        long tmpDate = in.readLong();
        mDate = tmpDate == -1 ? null : new Date(tmpDate);
        mValue = in.readDouble();
        mPaid = in.readByte() != 0;
        mDescription = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(mId);
        dest.writeString(mClient);
        dest.writeString(mPhone);
        dest.writeString(mAddress);
        dest.writeLong(mDate != null ? mDate.getTime() : -1);
        dest.writeDouble(mValue);
        dest.writeByte(mPaid ? (byte) 1 : (byte) 0);
        dest.writeString(mDescription);
    }

    public static final Creator<ServiceOrder> CREATOR = new Creator<ServiceOrder>() {
        @Override
        public ServiceOrder createFromParcel(Parcel in) {
            return new ServiceOrder(in);
        }

        @Override
        public ServiceOrder[] newArray(int size) {
            return new ServiceOrder[size];
        }
    };

}

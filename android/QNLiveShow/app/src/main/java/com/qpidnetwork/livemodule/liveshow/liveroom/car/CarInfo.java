package com.qpidnetwork.livemodule.liveshow.liveroom.car;

/**
 * Description:
 * <p>
 * Created by Harry on 2017/9/6.
 */

public class CarInfo {

    public String nickName;
    public String userId;
    public String riderId;
    public String riderName;
    public String riderUrl;

    public CarInfo(){

    }

    public CarInfo(String nickName,String userId,String riderId,String riderName,String riderUrl){
        this.nickName = nickName;
        this.userId = userId;
        this.riderId = riderId;
        this.riderName = riderName;
        this.riderUrl = riderUrl;
    }
}

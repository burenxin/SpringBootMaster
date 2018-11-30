package cn.tycoding.dto;

import cn.tycoding.entity.SeckillOrder;
import cn.tycoding.enums.SeckillStatEnum;

public class SeckillExecution {
    private Long seckillId;
    private int state;//秒杀执行结果状态
    private String stateInfo;//状态表示
    private SeckillOrder seckillOrder;//秒杀成功的订单对象
    public SeckillExecution(Long seckillId, SeckillStatEnum seckillStatEnum,SeckillOrder seckillOrder){
        this.seckillId=seckillId;
        this.state=seckillStatEnum.getState();
        this.stateInfo=seckillStatEnum.getStateInfo();
        this.seckillOrder=seckillOrder;
    }
    public SeckillExecution(Long seckillId,SeckillStatEnum seckillStatEnum){
        this.seckillId=seckillId;
        this.state=seckillStatEnum.getState();
        this.stateInfo=seckillStatEnum.getStateInfo();
    }

    public Long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(Long seckillId) {
        this.seckillId = seckillId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public SeckillOrder getSeckillOrder() {
        return seckillOrder;
    }

    public void setSeckillOrder(SeckillOrder seckillOrder) {
        this.seckillOrder = seckillOrder;
    }

    @Override
    public String toString() {
        return "SeckillExecution{" +
                "seckillId=" + seckillId +
                ", state=" + state +
                ", stateInfo='" + stateInfo + '\'' +
                ", seckillOrder=" + seckillOrder +
                '}';
    }
}

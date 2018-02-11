package com.tamu.jcabelloc.visitmanager;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by jcabelloc on 2/10/2018.
 */

public class VisitTask {
    String id;
    String agent;
    String address;
    LatLng location;
    String status;
    String result;
    String reason;

    public VisitTask() {
    }
    public VisitTask(String agent, String address, LatLng location, String reason, String status) {
        this.agent = agent;
        this.address = address;
        this.location = location;
        this.reason = reason;
        this.status = status;
    }
    public VisitTask(String agent, String address, LatLng location, String reason) {
        this.agent = agent;
        this.address = address;
        this.location = location;
        this.reason = reason;
    }

    public VisitTask(String id, String agent, String address, LatLng location, String reason) {
        this.id = id;
        this.agent = agent;
        this.address = address;
        this.location = location;
        this.reason = reason;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "VisitTask{" +
                "id=" + id +
                ", agent='" + agent + '\'' +
                ", address='" + address + '\'' +
                ", location=" + location +
                ", status=" + status +
                ", result='" + result + '\'' +
                '}';
    }
}

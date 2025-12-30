package com.anyi.common.qfu;

import java.security.PrivateKey;
import java.security.PublicKey;

public class Credentials {
    private PrivateKey privateKey;

    private PublicKey publicKey;

    private String vendor;

    private String agency;

    public Credentials(PrivateKey privateKey, PublicKey publicKey, String vendor) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
        this.vendor = vendor;
    }

    public Credentials(PrivateKey privateKey, PublicKey publicKey, String vendor, String agency) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
        this.vendor = vendor;
        this.agency = agency;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }
}
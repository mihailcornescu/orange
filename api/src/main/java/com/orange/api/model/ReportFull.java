package com.orange.api.model;

public class ReportFull {
    private String name;
    private String cnp;
    private String iban;
    private ReportPerType ibanToIban;
    private ReportPerType ibanToWallet;
    private ReportPerType walletToIban;
    private ReportPerType walletToWallet;

    public ReportFull() {
        name = null;
        cnp = null;
        iban = null;
        ibanToIban = new ReportPerType();
        ibanToWallet = new ReportPerType();
        walletToIban = new ReportPerType();
        walletToWallet = new ReportPerType();
    }

    public ReportFull(String name, String cnp, String iban, ReportPerType ibanToIban, ReportPerType ibanToWallet, ReportPerType walletToIban, ReportPerType walletToWallet) {
        this.name = name;
        this.cnp = cnp;
        this.iban = iban;
        this.ibanToIban = ibanToIban;
        this.ibanToWallet = ibanToWallet;
        this.walletToIban = walletToIban;
        this.walletToWallet = walletToWallet;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCnp() {
        return cnp;
    }

    public void setCnp(String cnp) {
        this.cnp = cnp;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public ReportPerType getIbanToIban() {
        return ibanToIban;
    }

    public void setIbanToIban(ReportPerType ibanToIban) {
        this.ibanToIban = ibanToIban;
    }

    public ReportPerType getIbanToWallet() {
        return ibanToWallet;
    }

    public void setIbanToWallet(ReportPerType ibanToWallet) {
        this.ibanToWallet = ibanToWallet;
    }

    public ReportPerType getWalletToIban() {
        return walletToIban;
    }

    public void setWalletToIban(ReportPerType walletToIban) {
        this.walletToIban = walletToIban;
    }

    public ReportPerType getWalletToWallet() {
        return walletToWallet;
    }

    public void setWalletToWallet(ReportPerType walletToWallet) {
        this.walletToWallet = walletToWallet;
    }
}

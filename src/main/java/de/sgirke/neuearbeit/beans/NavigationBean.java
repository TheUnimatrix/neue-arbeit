package de.sgirke.neuearbeit.beans;

public class NavigationBean {

    public String indexPage() {
        System.out.println("irgendwas");
        return "index?faces-redirect=true";
    }

    public String workingDaysPage() {
        System.out.println("irgendwas anderes");
        return "working-days?faces-redirect=true";
    }

}
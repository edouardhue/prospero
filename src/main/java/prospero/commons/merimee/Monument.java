package prospero.commons.merimee;

import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public final class Monument {
    private String ref;

    private String etud;

    private String loca;

    private String reg;

    private String dept;

    private String com;

    private String insee;

    private String tico;

    private String adrs;

    private String stat;

    private String affe;

    private String ppro;

    private String dpro;

    private String autr;

    private String scle;
    
    private final List<Protection> protections;
    
    private final Set<Protection.Type> protectionTypes;

    public Monument() {
        this.protections = new LinkedList<>();
        this.protectionTypes = EnumSet.noneOf(Protection.Type.class);
    }
    
    public String getRef() {
        return ref;
    }

    public void setRef(final String ref) {
        this.ref = ref;
    }

    public String getEtud() {
        return etud;
    }

    public void setEtud(final String etud) {
        this.etud = etud;
    }

    public String getLoca() {
        return loca;
    }

    public void setLoca(final String loca) {
        this.loca = loca;
    }

    public String getReg() {
        return reg;
    }

    public void setReg(final String reg) {
        this.reg = reg;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(final String dept) {
        this.dept = dept;
    }

    public String getCom() {
        return com;
    }

    public void setCom(final String com) {
        this.com = com;
    }

    public String getInsee() {
        return insee;
    }

    public void setInsee(final String insee) {
        this.insee = insee;
    }

    public String getTico() {
        return tico;
    }

    public void setTico(final String tico) {
        this.tico = tico;
    }

    public String getAdrs() {
        return adrs;
    }

    public void setAdrs(final String adrs) {
        this.adrs = adrs;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(final String stat) {
        this.stat = stat;
    }

    public String getAffe() {
        return affe;
    }

    public void setAffe(final String affe) {
        this.affe = affe;
    }

    public String getPpro() {
        return ppro;
    }

    public void setPpro(final String ppro) {
        this.ppro = ppro;
    }

    public String getDpro() {
        return dpro;
    }

    public void setDpro(final String dpro) {
        this.dpro = dpro;
    }

    public String getAutr() {
        return autr;
    }

    public void setAutr(final String autr) {
        this.autr = autr;
    }

    public String getScle() {
        return scle;
    }

    public void setScle(final String scle) {
        this.scle = scle;
    }
    
    public List<Protection> getProtections() {
        return Collections.unmodifiableList(this.protections);
    }
    
    public void addProtection(final Protection protection) {
        this.protections.add(protection);
        this.protectionTypes.add(protection.getType());
    }
    
    public Set<Protection.Type> getProtectionTypes() {
        return Collections.unmodifiableSet(this.protectionTypes);
    }

}
